package net.aionstudios.api.error;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.api.service.ResponseServices;

/**
 * A class designed for handling errors that prevent the return of valid data for a request to the {@link APIServer}.
 * 
 * @author Winter Roberts
 *
 */
public class AOSError {
	
	private String name;
	private String desc;
	private int responseCode;
	
	/**
	 * Creates a new {@link AOSError}
	 * 
	 * @param errorName The name of the error, in which no-spaced upper-camel-case is preferred.
	 * @param httpResponseCode The HTTP response code that should be returned with this error to the client.
	 * @param errorDesc A description of what behavior causes this type of error.
	 */
	public AOSError(String errorName, int httpResponseCode, String errorDesc) {
		name = errorName;
		desc = errorDesc;
		responseCode = httpResponseCode;
	}
	
	/**
	 * A method called when an error occurs to redact any previous data and modified the output of a {@link Response} to the client.
	 * 
	 * @param error The error {@link JSONObject}, provided by the {@link ContextHandler}.
	 * @param message A specific method that defines explicitly what caused the error.
	 */
	public void throwError(JSONObject error, String message) {
		ResponseServices.clearJsonObject(error);
		try {
			error.put("status", "E"+ErrorManager.getErrorNumber(name));
			error.put("status_type", name);
			error.put("status_desc", desc);
			error.put("message", message);
		} catch (JSONException e) {
			System.err.println("Erroring failed! Error '"+name+"'");
			e.printStackTrace();
		}
	}

	/**
	 * @return The name of this error.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The description of this error.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return The http response code of this error.
	 */
	public int getResponseCode() {
		return responseCode;
	}
	
}
