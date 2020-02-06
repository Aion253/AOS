package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class MissingFileParametersError extends AOSError {

	public MissingFileParametersError() {
		super("MissingFileParameters", 422, "The provided file parameters did not satisfy the action!");
	}

}
