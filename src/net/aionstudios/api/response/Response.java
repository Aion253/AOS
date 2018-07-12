package net.aionstudios.api.response;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.error.AOSError;
import net.aionstudios.api.service.ResponseServices;
import net.aionstudios.api.util.RequestUtils;

public class Response {
	
	private JSONObject data;
	private JSONObject error;
	private Map<String, Object> dataMap;
	
	private boolean dataFetched = false;
	private boolean errorFetched = false;
	private int httpResponseCode = 200;
	
	private String requestIP = "";
	
	public Response(HttpExchange he) {
		requestIP = RequestUtils.getRequestIP(he);
		data = ResponseServices.getLinkedJsonObject();
		error = ResponseServices.getLinkedJsonObject();
		dataMap = new HashMap<String, Object>();
	}
	
	public void putDataResponse(ResponseStatus status, String message) {
		httpResponseCode = 200;
		ResponseServices.clearJsonObject(data);
		try {
			data.put("status", "S"+status.getStatus());
			data.put("status_type", status.getStatusType());
			data.put("status_desc", status.getStatusDesc());
			data.put("message", message);
			if(dataMap!=null&&dataMap.size()>0) {
				for(int i = 0; i < dataMap.size(); i++) {
					data.put((String) dataMap.keySet().toArray()[i], dataMap.get(dataMap.keySet().toArray()[i]));
				}
			}
		} catch (JSONException e) {
			System.out.println("A JSONException prevented the reply!");
			e.printStackTrace();
		}
	}
	
	public void putDataResponse(ResponseStatus status, int httpResponseCode, String message) {
		this.httpResponseCode = httpResponseCode;
		ResponseServices.clearJsonObject(data);
		try {
			data.put("status", "S"+status.getStatus());
			data.put("status_type", status.getStatusType());
			data.put("status_desc", status.getStatusDesc());
			data.put("message", message);
			if(dataMap!=null&&dataMap.size()>0) {
				for(int i = 0; i < dataMap.size(); i++) {
					data.put((String) dataMap.keySet().toArray()[i], dataMap.get(dataMap.keySet().toArray()[i]));
				}
			}
		} catch (JSONException e) {
			System.out.println("A JSONException prevented the reply!");
			e.printStackTrace();
		}
	}
	
	public void putData(String key, Object value) {
		dataMap.put(key, value);
	}
	
	public void removeData(String key) {
		dataMap.remove(key);
	}
	
	public void putErrorResponse(AOSError aosError, String message) {
		httpResponseCode = aosError.getResponseCode();
		aosError.throwError(error, message);
	}
	
	public JSONObject oneTimeDataFetch() {
		if(!dataFetched) {
			dataFetched = true;
			return data;
		}
		return null;
	}
	
	public JSONObject oneTimeErrorFetch() {
		if(!errorFetched) {
			errorFetched = true;
			return error;
		}
		return null;
	}
	
	public String getRequestIP() {
		return requestIP;
	}
	
	public int getResponseCode() {
		return httpResponseCode;
	}

}
