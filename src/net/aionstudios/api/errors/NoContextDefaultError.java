package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

/**
 * An {@link AOSError} encountered when a request fails due to the absence of a default response and no provided {@link Action} to a {@link Context}.
 * 
 * @author Winter Roberts
 *
 */
public class NoContextDefaultError extends AOSError {

	public NoContextDefaultError() {
		super("NoContextDefault", 422, "The context does not provide a default and therefore requires an action!");
	}

}
