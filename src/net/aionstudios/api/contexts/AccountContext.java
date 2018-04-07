package net.aionstudios.api.contexts;

import org.json.JSONException;

import net.aionstudios.api.context.Context;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.response.Response;

public class AccountContext extends Context {

	public AccountContext() {
		super("account");
	}

	@Override
	public void contextDefault(Response e, String requestContext) throws JSONException {
		InternalErrors.noContextDefaultError(e, requestContext);
	}

}
