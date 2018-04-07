package net.aionstudios.api.actions;

import java.util.Map;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.aos.ResponseStatus;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.response.Response;
import net.aionstudios.api.service.AccountServices;
import net.aionstudios.api.service.RateLimitServices;

public class RateLimitsCheckAction extends Action {

	public RateLimitsCheckAction() {
		super("check");
	}

	@Override
	public void doAction(Response response, String requestContext, Map<String, String> getQuery,
			Map<String, String> postQuery) throws JSONException {
		if(postQuery.containsKey("apiToken")) {
			String apiKey = AccountServices.getApiKeyFromToken(postQuery.get("apiToken"));
			if(apiKey.length()==64) {
				long lastHour = RateLimitServices.getAccountEventsLastHour(apiKey);
				response.putData("rate_event_type", "account");
				response.putData("events_last_hour", lastHour);
				if(!RateLimitServices.usingRateLimit()) {
					response.putData("events_to_limit", -1);
				} else {
					response.putData("events_to_limit", RateLimitServices.getAccountLimit()-lastHour);
				}
				response.putDataResponse(ResponseStatus.SUCCESS, "Retrieved rate info for account.");
			} else {
				response.putErrorResponse(InternalErrors.invalidSessionError, "Couldn't retrieve rate history because the provided apiToken was not registered to any account!");
			}
		} else {
			long lastHour = RateLimitServices.getIPEventsLastHour(response.getRequestIP());
			response.putData("rate_event_type", "ip");
			response.putData("events_last_hour", lastHour);
			if(!RateLimitServices.usingRateLimit()) {
				response.putData("events_to_limit", -1);
			} else {
				response.putData("events_to_limit", RateLimitServices.getBaseLimit()-lastHour);
			}
			response.putDataResponse(ResponseStatus.SUCCESS, "Retrieved rate info for ip.");
		}
	}

}
