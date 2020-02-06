package net.aionstudios.api.actions;

import java.util.List;
import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.file.MultipartFile;
import net.aionstudios.api.response.Response;

/**
 * A simple {@link Action} that notifies a client that the node is in fact online and functioning.
 */
public class InstancePingAction extends Action {

	public InstancePingAction() {
		super("ping");
	}

	@Override
	public void doAction(Response response, String requestContext, Map<String, String> getQuery,
			Map<String, String> postQuery, List<MultipartFile> mfs) throws JSONException {
		response.putDataResponse(ResponseStatus.SUCCESS, "pong");
	}

}
