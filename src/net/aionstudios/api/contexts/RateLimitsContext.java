package net.aionstudios.api.contexts;

import org.json.JSONException;

import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.context.Context;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.RateLimitServices;

public class RateLimitsContext extends Context {

	public RateLimitsContext() {
		super("ratelimits");
	}

	@Override
	public void contextDefault(Response response, String requestContext) throws JSONException {
		response.putData("rates_enabled", RateLimitServices.usingRateLimit());
		response.putData("rate_hourly_ip", RateLimitServices.getBaseLimit());
		response.putData("rate_hourly_account", RateLimitServices.getAccountLimit());
		response.putDataResponse(ResponseStatus.SUCCESS, "Rate limit info provided.");
	}

}
