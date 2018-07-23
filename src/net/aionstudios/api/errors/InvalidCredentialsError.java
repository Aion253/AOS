package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when client authentication fails due to an invalid key-secret pair.
 * 
 * @author Winter Roberts
 *
 */
public class InvalidCredentialsError extends AOSError {

	public InvalidCredentialsError() {
		super("InvalidCredentialsError", 401, "Valid credentials were not provided!");
	}

}
