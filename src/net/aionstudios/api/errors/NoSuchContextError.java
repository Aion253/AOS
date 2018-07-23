package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to the absence of a {@link Context}.
 * 
 * @author Winter Roberts
 *
 */
public class NoSuchContextError extends AOSError {

	public NoSuchContextError() {
		super("NoSuchContext", 404, "The request context was not recognized!");
	}

}
