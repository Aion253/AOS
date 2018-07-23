package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to the absence of a request {@link Action} in the requested {@link Context}.
 * 
 * @author Winter Roberts
 *
 */
public class NoSuchActionError extends AOSError {

	public NoSuchActionError() {
		super("NoSuchAction", 404, "The request action was not recognized by the context!");
	}

}
