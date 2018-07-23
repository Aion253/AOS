package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to a lack of required get parameters.
 * 
 * @author Winter Roberts
 *
 */
public class MissingGetParametersError extends AOSError {

	public MissingGetParametersError() {
		super("MissingGetParameters", 422, "The provided get parameters did not satisfy the action!");
	}

}
