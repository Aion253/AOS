package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class RateLimitError extends AOSError {

	public RateLimitError() {
		super("RateLimitError", "Exceeded the imposed maximum calls within the period of an hour.");
	}

}
