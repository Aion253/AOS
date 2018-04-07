package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class MissingGetParametersError extends AOSError {

	public MissingGetParametersError() {
		super("MissingGetParameters", "The provided get parameters did not satisfy the action!");
	}

}
