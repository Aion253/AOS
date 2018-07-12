package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class MissingPostParametersError extends AOSError {

	public MissingPostParametersError() {
		super("MissingPostParameters", 422, "The provided post parameters did not satisfy the action!");
	}

}
