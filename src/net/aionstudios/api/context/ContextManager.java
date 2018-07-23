package net.aionstudios.api.context;

import java.util.ArrayList;
import java.util.List;

import net.aionstudios.api.action.Action;

/**
 * A class that accumulates active {@link Context}s on the {@link APIServer} so that they can be located by the {@link ContextHandler} when they're requested.
 * 
 * @author Winter Roberts
 *
 */
public class ContextManager {
	
	private static List<Context> contexts = new ArrayList<Context>();
	
	/**
	 * Registers a {@link Context} to the {@link ContextManager}.
	 * 
	 * @param con the {@link Context} to be registered to the {@link ContextManager}.
	 * @return True if the {@link Context} was registered and none other by the same name was already registered to the {@link ContextManager}, false otherwise.
	 */
	public static boolean registerContext(Context con) {
		for(Context c: contexts) {
			if(con.getContext().equals(c.getContext())) {
				System.err.println("Attempt to register duplicate context '"+con.getContext()+"' failed! A context by the same name has already reserved the call!");
				return false;
			}
		}
		contexts.add(con);
		return true;
	}
	
	/**
	 * Removes a {@link Context} from the {@link ContextManager}.
	 * 
	 * @param con The {@link Context} to be removed from this {@link ContextManager}.
	 * @return True if the {@link Context} was removed from the {@link ContextManager}, false otherwise.
	 */
	public static boolean removeContext(Context con) {
		return contexts.remove(con);
	}
	
	/**
	 * Locates a {@link Context} within the {@link ContextManager}.
	 * 
	 * @param context The string name of the {@link Context} to be found.
	 * @return A {@link Context} registered to the {@link ContextManager} matching the provided name, or null if no such entry exists.
	 */
	public static Context findContext(String context) {
		for(Context c: contexts) {
			if(c.getContext().equals(context)) {
				return c;
			}
		}
		return null;
	}

}
