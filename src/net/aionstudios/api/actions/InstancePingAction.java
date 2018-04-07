package net.aionstudios.api.actions;

import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.response.Response;

public class InstancePingAction extends Action {

	public InstancePingAction() {
		super("ping");
	}

	@Override
	public void doAction(Response e, String requestContext, Map<String, String> getQuery,
			Map<String, String> postQuery) throws JSONException {
		e.putDataResponse(ResponseStatus.SUCCESS, "pong");
	}

}
