package net.aionstudios.api.context;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.AOSInfo;
import net.aionstudios.api.aos.RateEventReference;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.AccountServices;
import net.aionstudios.api.service.DateTimeServices;
import net.aionstudios.api.service.RateLimitServices;
import net.aionstudios.api.service.ResponseServices;
import net.aionstudios.api.util.DatabaseUtils;
import net.aionstudios.api.util.RequestUtils;
import net.aionstudios.api.util.SecurityUtils;

public class ContextHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		long nanoStart = System.nanoTime();
		JSONObject response = ResponseServices.getLinkedJsonObject();
		JSONObject request = ResponseServices.getLinkedJsonObject();
		Response resp = new Response(he);
		if(he.getRequestURI().toString().equals("/")) {
			ResponseServices.generateHTTPResponse(he, AOSInfo.getBlankRootString());
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
		if(he.getRequestMethod().equalsIgnoreCase("POST")) {
			postQuery = RequestUtils.resolvePostQuery(he);
		}
		/*if(postQuery.get((String) postQuery.keySet().toArray()[0])==getQuery.get((String) getQuery.keySet().toArray()[0])) {
			postQuery = null;
		}*/
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
			rateLimited = RateLimitServices.isIPOverLimit(he.getRemoteAddress().getAddress().getHostAddress());
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
								try {
									action.doAction(resp, requestContext, getQuery, postQuery);
								} catch (JSONException e) {
									System.err.println("Failed converting JSON to String");
									e.printStackTrace();
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
			String eventDuplicateQuery = "SELECT COUNT(*) FROM `aosevents` WHERE `eventID` = ?;";
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
			String insertEventQuery = "INSERT INTO `aosevents` "
					+ "(`eventID`, `apiKey`, `ip`, `startTime`, `context`, `getData`, `postData`, `status`, `statusType`, `statusDesc`, `message`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			DatabaseUtils.prepareAndExecute(insertEventQuery, false, eventID, apiKey, he.getRemoteAddress().getAddress().getHostAddress(),
					DateTimeServices.getMysqlCompatibleDateTime(), requestContext, getQuery.toString(), postQuery.toString(), 
					eventStatus, eventStatusType, eventStatusDesc, eventMessage);
			ResponseServices.generateHTTPResponse(he, response.toString(2));
			return;
		} catch (JSONException e) {
			System.err.println("Failed converting JSON to String");
			ResponseServices.generateHTTPResponse(he, "Service Failed!");
			e.printStackTrace();
			return;
		}
	}

}
