package net.aionstudios.api.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.AOSInfo;
import net.aionstudios.api.aos.RateEventReference;
import net.aionstudios.api.compression.CompressionEncoding;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.file.MultipartFile;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.AccountServices;
import net.aionstudios.api.service.DateTimeServices;
import net.aionstudios.api.service.RateLimitServices;
import net.aionstudios.api.service.ResponseServices;
import net.aionstudios.api.util.DatabaseUtils;
import net.aionstudios.api.util.RequestUtils;
import net.aionstudios.api.util.SecurityUtils;

/**
 * A class that handles all incoming HTTP requests to the {@link APIServer}, usually forwarding them for completion to a {@link Context} and often from there, to and {@link Action}.
 * 
 * @author Winter Roberts
 *
 */
public class ContextHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		long nanoStart = System.nanoTime();
		JSONObject response = ResponseServices.getLinkedJsonObject();
		JSONObject request = ResponseServices.getLinkedJsonObject();
		Response resp = new Response(he);
		CompressionEncoding ce = CompressionEncoding.NONE;
		if(he.getRequestHeaders().containsKey("Accept-Encoding")) {
			String accept = he.getRequestHeaders().getFirst("Accept-Encoding");
			if(accept.contains("gzip")&&!accept.contains("gzip;q=0")&&!accept.contains("gzip; q=0")){
				ce = CompressionEncoding.GZIP;
			} else if (accept.contains("deflate")&&!accept.contains("deflate;q=0")&&!accept.contains("deflate; q=0")) {
				ce = CompressionEncoding.DEFLATE;
			}
		}
		if(he.getRequestURI().toString().equals("/")) {
			ResponseServices.generateHTTPResponse(he, resp.getResponseCode(), AOSInfo.getBlankRootString(), ce);
			return;
		}
		String[] requestSplit;
		if(he.getRequestURI().toString().contains("?")) {
			requestSplit = he.getRequestURI().toString().split("\\?", 2);
		} else {
			requestSplit = new String[1];
			requestSplit[0] = he.getRequestURI().toString();
		}
		String requestContext = he.getRequestURI().toString();
		Map<String, String> getQuery = new HashMap<String, String>();
		if(requestSplit.length>1) {
			requestContext = requestSplit[0];
			getQuery = RequestUtils.resolveGetQuery(requestSplit[1]);
		}
		Map<String, String> postQuery = new HashMap<String, String>();
//		if(he.getRequestMethod().equalsIgnoreCase("POST")) {
//			postQuery = RequestUtils.resolvePostQuery(he);
//		}
		/*if(postQuery.get((String) postQuery.keySet().toArray()[0])==getQuery.get((String) getQuery.keySet().toArray()[0])) {
			postQuery = null;
		}*/
		//File Uploads
		List<MultipartFile> mfs = new ArrayList<MultipartFile>();
		List<FileItem> deleteLater = new ArrayList<>();
		final String cT = he.getRequestHeaders().containsKey("Content-Type") ? he.getRequestHeaders().getFirst("Content-Type") : "text/html";
		if(cT.contains("multipart/form-data")||cT.contains("multipart/stream")) {
			DiskFileItemFactory d = new DiskFileItemFactory();
				try {
					ServletFileUpload up = new ServletFileUpload(d);
					List<FileItem> result = up.parseRequest(new RequestContext() {

						@Override
						public String getCharacterEncoding() {
							return "UTF-8";
						}

						@Override
						public int getContentLength() {
							return 0; //tested to work with 0 as return
						}

						@Override
						public String getContentType() {
							return cT;
						}

						@Override
						public InputStream getInputStream() throws IOException {
							return he.getRequestBody();
						}

					});
					for(FileItem fi : result) {
						if(!fi.isFormField()) {
			        		mfs.add(new MultipartFile(fi.getFieldName(), fi.getName(), fi.getContentType(), fi.getInputStream(), fi.getSize()));
			        		deleteLater.add(fi);
			        	} else {
			        		postQuery.put(fi.getFieldName(), fi.getString());
			        	}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if(he.getRequestMethod().equalsIgnoreCase("POST")) {
				postQuery = RequestUtils.resolvePostQuery(he);
			}
		}
		boolean rateLimited = false;
		boolean ipReason = false;
		String apiToken = "";
		String apiKey = "";
		if (postQuery.containsKey("apiToken")) {
			apiToken = postQuery.get("apiToken");
			if(apiToken.length()==512) {
				apiKey = AccountServices.getApiKeyFromToken(apiToken);
				rateLimited = RateLimitServices.isAccountOverLimit(apiKey);
			} else {
				rateLimited = true;
			}
		} else {
			rateLimited = RateLimitServices.isIPOverLimit(RequestUtils.getRequestIP(he));
			ipReason=true;
		}
		if(!rateLimited) {
			Context context = ContextManager.findContext(requestContext);
			if(context!=null) {
				if(getQuery!=null&&getQuery.get("action")!=null) {
					Action action = context.findAction(getQuery.get("action"));
					if(action!=null) {
						if(action.hasGetRequirements(getQuery)) {
							if(action.hasPostRequirements(postQuery)) {
								if(action.hasFileRequirements(mfs)) {
									try {
										action.doAction(resp, requestContext, getQuery, postQuery, mfs);
									} catch (JSONException e) {
										System.err.println("Failed converting JSON to String");
										e.printStackTrace();
									}
								} else {
									InternalErrors.missingFileParameters(resp, requestContext, getQuery, action.getFileRequiredParams());
								}
							} else {
								InternalErrors.missingPostParameters(resp, requestContext, getQuery, action.getPostRequiredParams());
							}
						} else {
							InternalErrors.missingGetParameters(resp, requestContext, getQuery, action.getGetRequiredParams());
						}
					} else {
						InternalErrors.noSuchActionError(resp, requestContext, getQuery);
					}
				} else {
					try {
						context.contextDefault(resp, requestContext);
					} catch (JSONException e) {
						System.err.println("Failed context default");
						e.printStackTrace();
					}
				}
			} else {
				InternalErrors.noSuchContextError(resp, requestContext);
			}
		} else {
			if(ipReason) InternalErrors.rateLimitError(resp, RateEventReference.IP);
			if(!ipReason) {
				if(apiKey.length()!=64) InternalErrors.invalidSessionError(resp);
				else InternalErrors.rateLimitError(resp, RateEventReference.ACCOUNT);
			}
		}
		try {
			JSONObject data = resp.oneTimeDataFetch();
			JSONObject error = resp.oneTimeErrorFetch();
			String eventID = SecurityUtils.genToken(16);
			String eventDuplicateQuery = "SELECT COUNT(*) FROM `AOSEvents` WHERE `eventID` = ?;";
			List<Map<String, Object>> duplicate = DatabaseUtils.prepareAndExecute(eventDuplicateQuery, false, eventID).get(0).getResults();
			while(((long) duplicate.get(0).get("COUNT(*)"))>0) {
				eventID = SecurityUtils.genToken(16);
				duplicate = DatabaseUtils.prepareAndExecute(eventDuplicateQuery, false, eventID).get(0).getResults();
			}
			request.put("event_id", eventID);
			request.put("millis_time", (double) ((double)System.nanoTime()-(double)nanoStart)/1000000.0);
			response.put("request", request);
			String eventStatus;
			String eventStatusType;
			String eventStatusDesc;
			String eventMessage;
			if(error.length()>0) {
				response.put("error", error);
				eventStatus = error.getString("status");
				eventStatusType = error.getString("status_type");
				eventStatusDesc = error.getString("status_desc");
				eventMessage = error.getString("message");
			} else if(data.length()>0) {
				response.put("data", data);
				eventStatus = data.getString("status");
				eventStatusType = data.getString("status_type");
				eventStatusDesc = data.getString("status_desc");
				eventMessage = data.getString("message");
			} else {
				InternalErrors.emptyAPIResponseError(resp, requestContext, getQuery);
				response.put("error", error);
				eventStatus = error.getString("status");
				eventStatusType = error.getString("status_type");
				eventStatusDesc = error.getString("status_desc");
				eventMessage = error.getString("message");
			}
			String insertEventQuery = "INSERT INTO `AOSEvents` "
					+ "(`eventID`, `apiKey`, `ip`, `startTime`, `context`, `getData`, `postData`, `status`, `statusType`, `statusDesc`, `message`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			DatabaseUtils.prepareAndExecute(insertEventQuery, false, eventID, apiKey, RequestUtils.getRequestIP(he),
					DateTimeServices.getMysqlCompatibleDateTime(), requestContext, getQuery.toString(), postQuery.toString(), 
					eventStatus, eventStatusType, eventStatusDesc, eventMessage);
			ResponseServices.generateHTTPResponse(he, resp.getResponseCode(), response.toString(2), ce);
			return;
		} catch (JSONException e) {
			System.err.println("Failed converting JSON to String");
			ResponseServices.generateHTTPResponse(he, 500, "Service Failed!", ce);
			e.printStackTrace();
			return;
		}
	}

}
