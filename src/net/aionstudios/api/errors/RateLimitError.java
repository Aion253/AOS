package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to the excessive use of the API.
 * 
 * @author Winter Roberts
 *
 */
public class RateLimitError extends AOSError {

	public RateLimitError() {
		super("RateLimitError", 429, "Exceeded the imposed maximum calls within the period of an hour.");
	}

}
