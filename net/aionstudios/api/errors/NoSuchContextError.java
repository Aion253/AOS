package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class NoSuchContextError extends AOSError {

	public NoSuchContextError() {
		super("NoSuchContext", "The request context was not recognized!");
	}

}
