package net.aionstudios.api.errors;

import net.aionstudios.api.error.AOSError;

public class UncaughtExceptionError extends AOSError {

	public UncaughtExceptionError() {
		super("UncaughtExceptionError", 500, "Internal Uncaught Error");
		// TODO Auto-generated constructor stub
	}

}
