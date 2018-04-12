package net.aionstudios.api.service;

import java.util.List;
import java.util.Map;

import javax.xml.ws.spi.http.HttpExchange;

import net.aionstudios.api.util.DatabaseUtils;

public class RateLimitServices {
	
	private static int baseLimit = 1000;
	private static int accountLimit = 100000;
	private static boolean useRates = false;
	
	public static void useRateLimits(boolean use) {
		useRates = use;
	}
	
	public static boolean usingRateLimit() {
		return useRates;
	}
	
	public static int getBaseLimit() {
		return baseLimit;
	}
	
	public static void setBaseLimit(int baseLimit) {
		RateLimitServices.baseLimit = baseLimit;
	}
	
	public static int getAccountLimit() {
		return accountLimit;
	}
	
	public static void setAccountLimit(int accountLimit) {
		RateLimitServices.accountLimit = accountLimit;
	}
	
	private static String ipEventCountLastHourQuery = "Select COUNT(*) from `AOSEvents` WHERE `ip`=? AND `startTime` > DATE_SUB(NOW(),INTERVAL 1 HOUR);";
	private static String apiKeyEventCountLastHourQuery = "Select COUNT(*) from `AOSEvents` WHERE `apiKey`=? AND `startTime` > DATE_SUB(NOW(),INTERVAL 1 HOUR);";
	
	public static boolean isIPOverLimit(String ipAddress) {
		if(useRates&&(long) getIPEventsLastHour(ipAddress)>baseLimit) return true;
		return false;
	}
	
	public static boolean isAccountOverLimit(String apiKey) {
		if(useRates&&(long) getAccountEventsLastHour(apiKey)>baseLimit) return true;
		return false;
	}
	
	public static long getIPEventsLastHour(String ipAddress) {
		List<Map<String, Object>> lim = DatabaseUtils.prepareAndExecute(ipEventCountLastHourQuery, false, ipAddress).get(0).getResults();
		return (long) lim.get(0).get("COUNT(*)");
	}
	
	public static long getAccountEventsLastHour(String apiKey) {
		List<Map<String, Object>> lim = DatabaseUtils.prepareAndExecute(apiKeyEventCountLastHourQuery, false, apiKey).get(0).getResults();
		return (long) lim.get(0).get("COUNT(*)");
	}
	
}
