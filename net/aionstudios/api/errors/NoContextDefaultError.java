package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class NoContextDefaultError extends AOSError {

	public NoContextDefaultError() {
		super("NoContextDefault", "The context does not provide a default and therefore requires an action!");
	}

}
