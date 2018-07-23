package net.aionstudios.api.contexts;

import org.json.JSONException;

import net.aionstudios.api.API;
import net.aionstudios.api.aos.AOSInfo;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.context.Context;
import net.aionstudios.api.response.Response;

/**
 * An AOS-provided {@link Context} for dealing requests pertaining to information about the containing {@link APIServer}.
 * 
 * @author Winter Roberts
 *
 */
public class InstanceContext extends Context {

	public InstanceContext() {
		super("instance");
	}

	@Override
	public void contextDefault(Response e, String requestContext) throws JSONException {
		e.putData("api_identity", API.getName());
		e.putData("api_port", API.getPort());
		e.putData("aos_version", AOSInfo.AOS_VERSION);
		e.putData("aos_identity", AOSInfo.AOS_NAME);
		e.putData("java_version", AOSInfo.JAVA_VERSION);
		e.putDataResponse(ResponseStatus.SUCCESS, "Information about the connected instance.");
	}

}
