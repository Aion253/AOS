package net.aionstudios.api.action;

import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.response.Response;

public abstract class Action {
	
	private String action;
	private String[] getNames = new String[0];
	private String[] postNames = new String[0];
	
	public Action(String actionName) {
		action = actionName;
	}
	
	public abstract void doAction(Response response, String requestContext, Map<String, String> getQuery, Map<String, String> postQuery) throws JSONException;
	
	public void setGetRequiredParams(String... getQueryNames) {
		getNames = getQueryNames;
	}
	
	public void setPostRequiredParams(String... postQueryNames) {
		postNames = postQueryNames;
	}
	
	public boolean hasGetRequirements(Map<String, String> getQuery) {
		if(getNames.length>0){
			for(String gn : getNames) {
				boolean good = false;
				if(getQuery.keySet().size()>0) {
					for(String gq : getQuery.keySet()) {
						if(gn.equals(gq)) {
							good = true;
						}
					}
				}
				if(!good) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasPostRequirements(Map<String, String> postQuery) {
		if(postNames.length>0) {
			for(String gn : postNames) {
				boolean good = false;
				if(postQuery.keySet().size()>0) {
					for(String gq : postQuery.keySet()) {
						if(gn.equals(gq)) {
							good = true;
						}
					}
				}
				if(!good) {
					return false;
				}
			}
		}
		return true;
	}
	
	public String getAction() {
		return action;
	}

	public String[] getGetRequiredParams() {
		return getNames;
	}
	
	public String[] getPostRequiredParams() {
		return postNames;
	}

}
