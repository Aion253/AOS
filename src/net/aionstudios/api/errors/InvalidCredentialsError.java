package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class InvalidCredentialsError extends AOSError {

	public InvalidCredentialsError() {
		super("InvalidCredentialsError", 401, "Valid credentials were not provided!");
	}

}
