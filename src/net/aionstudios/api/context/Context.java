package net.aionstudios.api.context;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.server.APIServer;

/**
 * A {@link Context} is invoked by the {@link ContextHandler} running at the head of the {@link APIServer} and may either interpret an {@link Action} provided in the request or return information without extended client input.
 * 
 * @author Winter Roberts
 *
 */
public abstract class Context {
	
	private String context;
	private Map<String, Action> contextActions = new HashMap<>();
	
	/**
	 * Creates a new AOS {@link Context}, associated with the {@link ContextHandler} of the running {@link APIServer}.
	 * 
	 * @param actionName The url-invocable name of this context.
	 */
	public Context(String handlerContext) {
		context = "/"+handlerContext;
	}

	/**
	 * Registers an {@link Action} to this {@link Context}.
	 * 
	 * @param action The {@link Action} to be registered to this {@link Context}.
	 * @return True if the {@link Action} was registered and none other by the same name was already registered to the {@link Context}, false otherwise.
	 */
	public void registerAction(Action action) {
		contextActions.put(action.getAction(), action);
	}
	
	/**
	 * Removes an {@link Action} from the {@link Context}.
	 * 
	 * @param action The {@link Action} to be removed from this {@link Context}.
	 * @return True if the {@link Action} was removed from the {@link Context}, false otherwise.
	 */
	public Action removeAction(Action action) {
		return contextActions.remove(action.getAction());
	}
	
	/**
	 * Locates an {@link Action} within the {@link Context}.
	 * 
	 * @param action The string name of the {@link Action} to be found.
	 * @return An {@link Action} registered to the {@link Context} matching the provided name, or null if no such entry exists.
	 */
	public Action findAction(String action) {
		return contextActions.get(action);
	}
	
	/**
	 * A method that is called if the {@link Context} is requested but no {@link Action} is provided by the request.
	 * Not all {@link Context}s need to have a default response.
	 * 
	 * @param response An instance of {@link Response}, used to construct a response from this {@link Context}.
	 * @param requestContext The name of this {@link Context}, forwarded by the {@link ContextHandler}.
	 * @throws JSONException Allows AOS to handle any possible JSON issues reducing perceived difficulty in creating custom {@link Context}s.
	 */
	public abstract void contextDefault(Response response, String requestContext) throws JSONException;
	
	/**
	 * @return The string name of this {@link Context}.
	 */
	public String getContext() {
		return context;
	}

}
