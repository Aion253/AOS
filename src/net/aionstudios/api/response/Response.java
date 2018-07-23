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

/**
 * A class that organizes {@link JSONObject}s for responses to client requests.
 * 
 * @author Winter Roberts
 *
 */
public class Response {
	
	private JSONObject data;
	private JSONObject error;
	private Map<String, Object> dataMap;
	
	private boolean dataFetched = false;
	private boolean errorFetched = false;
	private int httpResponseCode = 200;
	
	private String requestIP = "";
	
	/**
	 * Creates a new {@link Response} around the provided request.
	 * 
	 * @param he The request made by the client.
	 */
	public Response(HttpExchange he) {
		requestIP = RequestUtils.getRequestIP(he);
		data = ResponseServices.getLinkedJsonObject();
		error = ResponseServices.getLinkedJsonObject();
		dataMap = new HashMap<String, Object>();
	}
	
	/**
	 * Populates the data {@link JSONObject} with a valid, functional response.
	 * 
	 * @param status The {@link ResponseStatus} to be provided in the response's request information.
	 * @param message The message to include in the response's request information.
	 */
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
	
	/**
	 * Populates the data {@link JSONObject} with a valid, functional response.
	 * 
	 * @param status The {@link ResponseStatus} to be provided in the response's request information.
	 * @param httpResponseCode An alternative HTTP response code to return with the response.
	 * @param message The message to include in the response's request information.
	 */
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
	
	/**
	 * Puts information into the data {@link JSONObject}.
	 * 
	 * @param key The key identifying the attached data.
	 * @param value The object value to be attached to the provided key.
	 */
	public void putData(String key, Object value) {
		dataMap.put(key, value);
	}
	
	/**
	 * Removes information from the data {@link JSONObject}.
	 * 
	 * @param key The key identifying the data to be removed.
	 */
	public void removeData(String key) {
		dataMap.remove(key);
	}
	
	/**
	 * Throws an {@link AOSError} and modifies the client response.
	 * 
	 * @param aosError The {@link AOSError} to be thrown.
	 * @param message An additional descriptive message about the error.
	 */
	public void putErrorResponse(AOSError aosError, String message) {
		httpResponseCode = aosError.getResponseCode();
		aosError.throwError(error, message);
	}
	
	/**
	 * Allows the data {@link JSONObject} to be read once per request, preventing modification that would prompt a NoResponseError.
	 * 
	 * @return A {@link JSONObject} containing response data.
	 */
	public JSONObject oneTimeDataFetch() {
		if(!dataFetched) {
			dataFetched = true;
			return data;
		}
		return null;
	}
	
	/**
	 * Allows the error {@link JSONObject} to be read once per request, preventing modification that would prompt a NoResponseError.
	 * 
	 * @return A {@link JSONObject} containing error info.
	 */
	public JSONObject oneTimeErrorFetch() {
		if(!errorFetched) {
			errorFetched = true;
			return error;
		}
		return null;
	}
	
	/**
	 * The IP address of the request origin.
	 * 
	 * @return A string representing the IP address of the request origin.
	 */
	public String getRequestIP() {
		return requestIP;
	}
	
	/**
	 * The most recently provided response code of the request.
	 * 
	 * @return An integer representing an HTTP response code.
	 */
	public int getResponseCode() {
		return httpResponseCode;
	}

}
