package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class NoSuchActionError extends AOSError {

	public NoSuchActionError() {
		super("NoSuchAction", 404, "The request action was not recognized by the context!");
	}

}
