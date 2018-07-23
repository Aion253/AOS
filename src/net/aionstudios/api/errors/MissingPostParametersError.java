package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to a lack of required post parameters.
 * 
 * @author Winter Roberts
 *
 */
public class MissingPostParametersError extends AOSError {

	public MissingPostParametersError() {
		super("MissingPostParameters", 422, "The provided post parameters did not satisfy the action!");
	}

}
