package net.aionstudios.api.service;

import java.util.List;
import java.util.Map;

import net.aionstudios.api.util.DatabaseUtils;

/**
 * A class providing services relating to AOS's rate limiting.
 * 
 * @author Winter Roberts
 *
 */
public class RateLimitServices {
	
	private static int baseLimit = 1000;
	private static int accountLimit = 100000;
	private static boolean useRates = false;
	
	/**
	 * Disables or enables rate limiting.
	 * 
	 * @param use A boolean, whether or not to use rate limits.
	 */
	public static void useRateLimits(boolean use) {
		useRates = use;
	}
	
	/**
	 * @return True if rate limits are enable, false otherwise.
	 */
	public static boolean usingRateLimit() {
		return useRates;
	}
	
	/**
	 * @return the current base (IP) rate limit setting.
	 */
	public static int getBaseLimit() {
		return baseLimit;
	}
	
	/**
	 * Sets the base (IP) rate limit setting.
	 * 
	 * @param baseLimit An integer limit (in requests per hour).
	 */
	public static void setBaseLimit(int baseLimit) {
		RateLimitServices.baseLimit = baseLimit;
	}
	
	/**
	 * @return the current account rate limit setting.
	 */
	public static int getAccountLimit() {
		return accountLimit;
	}
	
	/**
	 * Sets the account rate limit setting.
	 * 
	 * @param accountLimit An integer limit (in requests per hour).
	 */
	public static void setAccountLimit(int accountLimit) {
		RateLimitServices.accountLimit = accountLimit;
	}
	
	private static String ipEventCountLastHourQuery = "Select COUNT(*) from `AOSEvents` WHERE `ip`=? AND `startTime` > DATE_SUB(NOW(),INTERVAL 1 HOUR);";
	private static String apiKeyEventCountLastHourQuery = "Select COUNT(*) from `AOSEvents` WHERE `apiKey`=? AND `startTime` > DATE_SUB(NOW(),INTERVAL 1 HOUR);";
	
	/**
	 * Checks if a given IP address has exceeded the maximum requests per hour setting.
	 * 
	 * @param ipAddress The IP address to check.
	 * @return True if rate limits have been exceeded, false otherwise.
	 */
	public static boolean isIPOverLimit(String ipAddress) {
		if(useRates&&(long) getIPEventsLastHour(ipAddress)>baseLimit) return true;
		return false;
	}
	
	/**
	 * Checks if a given account has exceeded the maximum requests per hour setting.
	 * 
	 * @param apiKey The apiKey to check.
	 * @return True if rate limits have been exceeded, false otherwise.
	 */
	public static boolean isAccountOverLimit(String apiKey) {
		if(useRates&&(long) getAccountEventsLastHour(apiKey)>baseLimit) return true;
		return false;
	}
	
	/**
	 * Returns the number of requests made by an IP address in the last hour.
	 * 
	 * @param ipAddress The IP address to check.
	 * @return A long, the number of requests occuring from this source in the last hour.
	 */
	public static long getIPEventsLastHour(String ipAddress) {
		List<Map<String, Object>> lim = DatabaseUtils.prepareAndExecute(ipEventCountLastHourQuery, false, ipAddress).get(0).getResults();
		return (long) lim.get(0).get("COUNT(*)");
	}
	
	/**
	 * Returns the number of requests made by an account in the last hour.
	 * 
	 * @param apiKey The apiKey to check.
	 * @return A long, the number of requests occuring from this source in the last hour.
	 */
	public static long getAccountEventsLastHour(String apiKey) {
		List<Map<String, Object>> lim = DatabaseUtils.prepareAndExecute(apiKeyEventCountLastHourQuery, false, apiKey).get(0).getResults();
		return (long) lim.get(0).get("COUNT(*)");
	}
	
}
