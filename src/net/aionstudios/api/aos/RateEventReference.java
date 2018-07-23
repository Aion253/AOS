package net.aionstudios.api.aos;

import net.aionstudios.api.service.RateLimitServices;

/**
 * Enumerates possible rate-limiting options
 * 
 * @author Winter Roberts
 *
 */
public enum RateEventReference {
	
	IP(1),
	ACCOUNT(2);

	private final int value;

	/**
	 * Creates a {@link RateEventReference} by value.
	 * 
	 * @param newValue An integer representing the value of an enum in this class.
	 */
	RateEventReference(final int newValue) {
		value = newValue;
	}
	
	/**
	 * @return A string representing the name of this {@link RateEventReference}.
	 */
	public String getTypeName() {
		if(value==1) {
			return "IP";
		}
		if(value==2) {
			return "Account";
		}
		return "Unrecognized";
	}
	
	/**
	 * @return A long representing the maximal number of requests allowed by this {@link RateEventReference}.
	 */
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
