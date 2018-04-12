package net.aionstudios.api.service;

import java.util.List;

import net.aionstudios.api.database.QueryResults;
import net.aionstudios.api.util.DatabaseUtils;
import net.aionstudios.api.util.SecurityUtils;

public class AccountServices {
	
	private static String tokenToKeyQuery = "SELECT `apiKey` FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	private static String keyToTokenQuery = "SELECT `apiToken` FROM `AOSSessions` WHERE `apiKey`=? LIMIT 1;";
	private static String tokenInUseQuery = "SELECT COUNT(*) FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	private static String keyHasTokenQuery = "SELECT COUNT(*) FROM `AOSSessions` WHERE `apiKey`=? LIMIT 1;";
	private static String credentialsValidQuery = "SELECT COUNT(*) FROM `AOSAccounts` WHERE `apiKey`=? AND `apiSecret`=? LIMIT 1;";
	
	private static String insertTokenSession = "INSERT INTO `AOSSessions` (`apiKey`,`apiToken`,`validFrom`,`validTo`) VALUES (?,?,?,?);";
	private static String purgeExpiredSessions = "DELETE FROM `AOSSessions` WHERE `validTo`<=?;";
	
	private static String validSecondsQuery = "SELECT (UNIX_TIMESTAMP(validTo)) FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	
	public static String getApiKeyFromToken(String token) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(tokenToKeyQuery, true, token);
		return q.size()>0 ? (String) q.get(0).getResults().get(0).get("apiKey") : "";
	}
	
	public static String getTokenFromApiKey(String apiKey) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(keyToTokenQuery, true, apiKey);
		return q.size()>0 ? (String) q.get(0).getResults().get(0).get("apiToken") : "";
	}
	
	public static boolean isTokenInUse(String token) {
		return ((Long) DatabaseUtils.prepareAndExecute(tokenInUseQuery, true, token).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	public static boolean tokenExistsForKey(String apiKey) {
		return ((Long) DatabaseUtils.prepareAndExecute(keyHasTokenQuery, true, apiKey).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	public static String genNewUniqueToken() {
		String token = SecurityUtils.genToken(512);
		while(isTokenInUse(token)) {
			token = SecurityUtils.genToken(512);
		}
		return token;
	}
	
	public static long getValidSeconds(String token) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(validSecondsQuery, true, token);
		return q.size()>0 ? ((Long) q.get(0).getResults().get(0).get("(UNIX_TIMESTAMP(validTo))"))-DateTimeServices.getUnixTimestamp() : 0;
	}
	
	public static void purgeExpiredSessions() {
		DatabaseUtils.prepareAndExecute(purgeExpiredSessions, true, DateTimeServices.getMysqlCompatibleDateTime());
	}
	
	public static boolean areCredentialsValid(String apiKey, String apiSecret) {
		return ((Long) DatabaseUtils.prepareAndExecute(credentialsValidQuery, true, apiKey, apiSecret).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	public static String loginGetToken(String apiKey, String apiSecret) {
		if(areCredentialsValid(apiKey, apiSecret)) {
			String token = genNewUniqueToken();
			DatabaseUtils.prepareAndExecute(insertTokenSession, true, apiKey, token, DateTimeServices.getMysqlCompatibleDateTime(), DateTimeServices.getThirtyAddedDT());
			return token;
		}
		return "";
	}
	
}
