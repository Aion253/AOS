package net.aionstudios.api.actions;

import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.AccountServices;

/**
 * An action that enables user-authenticated requesting to the API, which can be enabled or disabled by programmers.
 */
public class AccountLoginAction extends Action {

	public AccountLoginAction() {
		super("login");
		this.setPostRequiredParams("apiKey", "apiSecret");
	}

	@Override
	public void doAction(Response e, String requestContext, Map<String, String> getQuery,
			Map<String, String> postQuery) throws JSONException {
		String token = AccountServices.loginGetToken(postQuery.get("apiKey"), postQuery.get("apiSecret"));
		if(token!="") {
			e.putData("apiToken", token);
			e.putDataResponse(ResponseStatus.SUCCESS, "Provided token will be valid for the next thirty minutes.");
		} else {
			e.putErrorResponse(InternalErrors.invalidCredentialsError, "Credentials mismatch or no entry exists.");
		}
	}

}
