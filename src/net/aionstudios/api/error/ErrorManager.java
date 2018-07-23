package net.aionstudios.api.error;

import java.util.ArrayList;
import java.util.List;

import net.aionstudios.api.context.Context;
import net.aionstudios.api.context.ContextHandler;
import net.aionstudios.api.context.ContextManager;
import net.aionstudios.api.cron.CronDateTime;
import net.aionstudios.api.cron.CronJob;
import net.aionstudios.api.server.APIServer;

/**
 * A class that accumulates {@link AOSError}s for the {@link APIServer} so that they can be thrown when they're encountered.
 * 
 * @author Winter Roberts
 *
 */
public class ErrorManager {
	
	private static List<AOSError> errors = new ArrayList<AOSError>();
	
	/**
	 * Registers an {@link AOSError} to the {@link ErrorManager}.
	 * 
	 * @param con the {@link AOSError} to be registered to the {@link ErrorManager}.
	 * @return True if the {@link AOSError} was registered and none other by the same name was already registered to the {@link ErrorManager}, false otherwise.
	 */
	public static boolean registerError(AOSError error) {
		for(AOSError e : errors) {
			if(e.getName().equals(error.getName())) {
				System.out.println("An error identified as '"+e.getName()+"' was already registered! This new instance couldn't be");
				return false;
			}
		}
		errors.add(error);
		return true;
	}
	
	/**
	 * Locates an {@link AOSError} within the {@link ErrorManager}.
	 * 
	 * @param error The string name of the {@link AOSError} to be found.
	 * @return A {@link AOSError} registered to the {@link ErrorManager} matching the provided name, or null if no such entry exists.
	 */
	public static AOSError getErrorByName(String error) {
		for(AOSError e : errors) {
			if(e.getName().equals(error)) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Gets the number of the named {@link AOSError}
	 * 
	 * @param error The string name of the {@link AOSError} to be found.
	 * @return The error number of the {@link AOSError} matching the provided name, or -1 if no such entry exists.
	 */
	public static int getErrorNumber(String error) {
		for(int i = 0; i < errors.size(); i++) {
			if(errors.get(i).getName().equals(error)) {
				return i+1;
			}
		}
		System.err.println("No such error '"+error+"'");
		return -1;
	}

}
