package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class UnauthorizedAccessError extends AOSError {

	public UnauthorizedAccessError() {
		super("UnauthorizedAccessError", 401, "Access to the context or action requires authorization!");
	}

}
