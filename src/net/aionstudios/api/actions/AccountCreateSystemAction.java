package net.aionstudios.api.actions;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.file.MultipartFile;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.AccountServices;

/**
 * A run-once {@link Action} that creates the AOS System API token and key for servers or administrators during setup.
 * 
 * @author wrpar
 *
 */
public class AccountCreateSystemAction extends Action {

	public AccountCreateSystemAction() {
		super("createSystem");
	}

	@Override
	public void doAction(Response response, String requestContext, Map<String, String> getQuery,
			Map<String, String> postQuery, List<MultipartFile> mfs) throws JSONException {
		String s = AccountServices.setupAccountsIfEmpty();
		if(s!=null) {
			String[] t = s.split("\\.");
			response.putData("key", t[0]);
			response.putData("secret", t[1]);
			response.putDataResponse(ResponseStatus.SUCCESS, "Created System account!");
		} else {
			response.putErrorResponse(InternalErrors.unauthorizedAccessError, "System has already been setup!");
		}
	}

}