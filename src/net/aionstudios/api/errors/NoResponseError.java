package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to an internal error that prevented any response from being generated.
 * 
 * @author Winter Roberts
 *
 */
public class NoResponseError extends AOSError {

	public NoResponseError() {
		super("NoResponse", 500, "The request was interpreted and processed but the API returned an empty response!");
	}

}
