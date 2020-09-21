package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to an unhandled error in server code.
 * 
 * @author Winter Roberts
 *
 */
public class UncaughtExceptionError extends AOSError {

	public UncaughtExceptionError() {
		super("UncaughtExceptionError", 500, "Internal Uncaught Error");
		// TODO Auto-generated constructor stub
	}

}
