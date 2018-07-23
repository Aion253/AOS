package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to a lack of client authentication.
 * 
 * @author Winter Roberts
 *
 */
public class UnauthorizedAccessError extends AOSError {

	public UnauthorizedAccessError() {
		super("UnauthorizedAccessError", 401, "Access to the context or action requires authorization!");
	}

}
