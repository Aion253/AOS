package net.aionstudios.api.aos;

import net.aionstudios.api.service.RateLimitServices;

public enum RateEventReference {
	
	IP(1),
	ACCOUNT(2);

	private final int value;

	RateEventReference(final int newValue) {
		value = newValue;
	}
	
	public String getTypeName() {
		if(value==1) {
			return "IP";
		}
		if(value==2) {
			return "Account";
		}
		return "Unrecognized";
	}
	
	public long getMaxRequestsPerHour() {
		if(value==1) {
			return RateLimitServices.getBaseLimit();
		}
		if(value==1) {
			return RateLimitServices.getAccountLimit();
		}
		return 0;
	}

}
