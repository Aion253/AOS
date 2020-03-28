package net.aionstudios.api.context;

import java.util.HashMap;
import java.util.Map;

import net.aionstudios.api.server.APIServer;

/**
 * A class that accumulates active {@link Context}s on the {@link APIServer} so that they can be located by the {@link ContextHandler} when they're requested.
 * 
 * @author Winter Roberts
 *
 */
public class ContextManager {
	
	private static Map<String, Context> contexts = new HashMap<>();
	
	/**
	 * Registers a {@link Context} to the {@link ContextManager}.
	 * 
	 * @param con the {@link Context} to be registered to the {@link ContextManager}.
	 * @return True if the {@link Context} was registered and none other by the same name was already registered to the {@link ContextManager}, false otherwise.
	 */
	public static void registerContext(Context con) {
		contexts.put(con.getContext(), con);
	}
	
	/**
	 * Removes a {@link Context} from the {@link ContextManager}.
	 * 
	 * @param con The {@link Context} to be removed from this {@link ContextManager}.
	 * @return True if the {@link Context} was removed from the {@link ContextManager}, false otherwise.
	 */
	public static Context removeContext(Context con) {
		return contexts.remove(con.getContext());
	}
	
	/**
	 * Locates a {@link Context} within the {@link ContextManager}.
	 * 
	 * @param context The string name of the {@link Context} to be found.
	 * @return A {@link Context} registered to the {@link ContextManager} matching the provided name, or null if no such entry exists.
	 */
	public static Context findContext(String context) {
		return contexts.get(context);
	}

}
