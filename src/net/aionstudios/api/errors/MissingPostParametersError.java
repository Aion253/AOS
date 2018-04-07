package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class MissingPostParametersError extends AOSError {

	public MissingPostParametersError() {
		super("MissingPostParameters", "The provided post parameters did not satisfy the action!");
	}

}
