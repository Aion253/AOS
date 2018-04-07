package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class NoResponseError extends AOSError {

	public NoResponseError() {
		super("NoResponse", "The request was interpreted and processed but the API returned an empty response!");
	}

}
