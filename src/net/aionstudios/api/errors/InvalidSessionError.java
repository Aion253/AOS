package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class InvalidSessionError extends AOSError {

	public InvalidSessionError() {
		super("InvalidSessionError", "The session token provided was not valid!");
	}

}
