package net.aionstudios.api.error;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.api.service.ResponseServices;

public class AOSError {
	
	private String name;
	private String desc;
	private int responseCode;
	
	public AOSError(String errorName, int httpResponseCode, String errorDesc) {
		name = errorName;
		desc = errorDesc;
		responseCode = httpResponseCode;
	}
	
	@SuppressWarnings("unused")
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

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
}
