package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class NoSuchContextError extends AOSError {

	public NoSuchContextError() {
		super("NoSuchContext", 404, "The request context was not recognized!");
	}

}
