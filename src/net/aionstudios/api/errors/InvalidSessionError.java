package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when client authentication fails due to an invalid token.
 * 
 * @author Winter Roberts
 *
 */
public class InvalidSessionError extends AOSError {

	public InvalidSessionError() {
		super("InvalidSessionError", 401, "The session token provided was not valid!");
	}

}
