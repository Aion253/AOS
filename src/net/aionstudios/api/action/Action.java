package net.aionstudios.api.action;

import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.response.Response;

/**
 * An {@link Action} is invoked by a {@link Context} after the {@link Context} is recognized by the {@link ContextHandler} running at the head of the {@link APIServer} to handle requests.
 * It is specified by name in the get parameter 'action' as part of a url.
 * 
 * @author Winter Roberts
 *
 */
public abstract class Action {
	
	private String action;
	private String[] getNames = new String[0];
	private String[] postNames = new String[0];
	
	/**
	 * Creates a new AOS {@link Action}, dissociated from a context until added to any of several.
	 * 
	 * @param actionName The url-invocable name of this action.
	 */
	public Action(String actionName) {
		action = actionName;
	}
	
	/**
	 * A method that is called after {@link ContextHandler} is able to find this {@link Action} within the {@link Context} or a request.
	 * 
	 * @param response An instance of {@link Response}, used to construct a response from this {@link Action}.
	 * @param requestContext The name of the {@link Context} that forwarded the request to this {@link Action}.
	 * @param getQuery A map of the get parameters provided in the request for this {@link Action}.
	 * @param postQuery A map of the post parameters provided in the request for this {@link Action}.
	 * @throws JSONException Allows AOS to handle any possible JSON issues reducing perceived difficulty in creating custom {@link Action}s.
	 */
	public abstract void doAction(Response response, String requestContext, Map<String, String> getQuery, Map<String, String> postQuery) throws JSONException;
	
	/**
	 * Requires get parameters in request a by name.
	 * 
	 * @param getQueryNames A series of get parameter names required by the action.
	 */
	public void setGetRequiredParams(String... getQueryNames) {
		getNames = getQueryNames;
	}
	
	/**
	 * Requires post parameters in request a by name.
	 * 
	 * @param postQueryNames A series of post parameter names required by the action.
	 */
	public void setPostRequiredParams(String... postQueryNames) {
		postNames = postQueryNames;
	}
	
	/**
	 * Checks if a map, from a request, has all of the {@link Action}'s required get parameters.
	 * 
	 * @param getQuery A map of get parameters.
	 * @return True if all required parameters are present, false otherwise.
	 */
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
	
	/**
	 * Checks if a map, from a request, has all of the {@link Action}'s required post parameters.
	 * 
	 * @param postQuery A map of post parameters.
	 * @return True if all required parameters are present, false otherwise.
	 */
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
	
	/**
	 * @return The string name of this {@link Action}.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return A String array of this {@link Action}'s required get parameters.
	 */
	public String[] getGetRequiredParams() {
		return getNames;
	}
	
	/**
	 * @return A String array of this {@link Action}'s required post parameters.
	 */
	public String[] getPostRequiredParams() {
		return postNames;
	}

}
