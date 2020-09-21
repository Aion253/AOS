package net.aionstudios.api.service;

import java.util.List;

import net.aionstudios.api.account.AccessLevel;
import net.aionstudios.api.account.AppType;
import net.aionstudios.api.database.QueryResults;
import net.aionstudios.api.util.DatabaseUtils;
import net.aionstudios.api.util.SecurityUtils;

/**
 * A class providing services relating to AOS's accounts system.
 * 
 * @author Winter Roberts
 *
 */
public class AccountServices {
	
	private static String tokenToKeyQuery = "SELECT `apiKey` FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	private static String keyToTokenQuery = "SELECT `apiToken` FROM `AOSSessions` WHERE `apiKey`=? LIMIT 1;";
	private static String tokenInUseQuery = "SELECT COUNT(*) FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	private static String keyHasTokenQuery = "SELECT COUNT(*) FROM `AOSSessions` WHERE `apiKey`=? LIMIT 1;";
	private static String credentialsValidQuery = "SELECT COUNT(*) FROM `AOSAccounts` WHERE `apiKey`=? AND `apiSecret`=? LIMIT 1;";
	
	private static String insertTokenSession = "INSERT INTO `AOSSessions` (`apiKey`,`apiToken`,`validFrom`,`validTo`) VALUES (?,?,?,?);";
	private static String purgeExpiredSessions = "DELETE FROM `AOSSessions` WHERE `validTo`<=?;";
	
	private static String validSecondsQuery = "SELECT (UNIX_TIMESTAMP(validTo)) FROM `AOSSessions` WHERE `apiToken`=? LIMIT 1;";
	
	//Account setup
	private static String countAccountsQuery = "SELECT COUNT(*) FROM `AOSAccounts`;";
	private static String keyExistsQuery = "SELECT COUNT(*) FROM `AOSAccounts` WHERE `apiKey` = ?;";
	private static String createAccountQuery = "INSERT INTO `AOSAccounts` (`apiKey`, `apiSecret`, `appDesc`, `appName`, `appType`, `callback`, `organization`, `website`, `privacy`, `terms`, `owner`, `accessLevel`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	/**
	 * Retrieves a client's apiKey given their apiToken.
	 * 
	 * @param token A leased apiToken.
	 * @return The apiKey associated with the apiToken or an empty string if none exist.
	 */
	public static String getApiKeyFromToken(String token) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(tokenToKeyQuery, true, token);
		return q.size()>0 ? (String) q.get(0).getResults().get(0).get("apiKey") : "";
	}
	
	/**
	 * Retrieves a client's apiToken given their apiKey.
	 * 
	 * @param apiKey An apiKey.
	 * @return The apiToken associated with the apiKey or an empty string if none exist.
	 */
	public static String getTokenFromApiKey(String apiKey) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(keyToTokenQuery, true, apiKey);
		return q.size()>0 ? (String) q.get(0).getResults().get(0).get("apiToken") : "";
	}
	
	/**
	 * Checks apiToken availability.
	 * 
	 * @param token A generated apiToken to check.
	 * @return True if the apiToken is in use, false otherwise.
	 */
	public static boolean isTokenInUse(String token) {
		return ((Long) DatabaseUtils.prepareAndExecute(tokenInUseQuery, true, token).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	/**
	 * Checks if an apiKey already has an active apiToken.
	 * 
	 * @param apiKey An apiKey.
	 * @return True if the apiKey is valid and has an active apiToken, false otherwise.
	 */
	public static boolean tokenExistsForKey(String apiKey) {
		return ((Long) DatabaseUtils.prepareAndExecute(keyHasTokenQuery, true, apiKey).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	/**
	 * Generates a securely random 512 byte string.
	 * 
	 * @return A 512 byte string.
	 */
	public static String genNewUniqueToken() {
		String token = SecurityUtils.genToken(512);
		while(isTokenInUse(token)) {
			token = SecurityUtils.genToken(512);
		}
		return token;
	}
	
	/**
	 * Checks for how many seconds longer an apiToken will be valid
	 * 
	 * @param token An apiToken.
	 * @return A long number of seconds or 0 if it has expired or does not exist.
	 */
	public static long getValidSeconds(String token) {
		List<QueryResults> q = DatabaseUtils.prepareAndExecute(validSecondsQuery, true, token);
		return q.size()>0 ? ((Long) q.get(0).getResults().get(0).get("(UNIX_TIMESTAMP(validTo))"))-DateTimeServices.getUnixTimestamp() : 0;
	}
	
	/**
	 * Removes expired apiTokens from the database.
	 */
	public static void purgeExpiredSessions() {
		DatabaseUtils.prepareAndExecute(purgeExpiredSessions, true, DateTimeServices.getMysqlCompatibleDateTime());
	}
	
	/**
	 * Checks the validity of a key-secret pair.
	 * 
	 * @param apiKey An apiKey.
	 * @param apiSecret An apiSecret.
	 * @return True if this is a valid key-secret pait, false otherwise.
	 */
	public static boolean areCredentialsValid(String apiKey, String apiSecret) {
		return ((Long) DatabaseUtils.prepareAndExecute(credentialsValidQuery, true, apiKey, apiSecret).get(0).getResults().get(0).get("COUNT(*)"))>0;
	}
	
	/**
	 * Generates a new apiToken and returns it for a valid login.
	 * 
	 * @param apiKey An apiKey.
	 * @param apiSecret An apiSecret.
	 * @return An apiToken if the credentials were valid, an empty string otherwise.
	 */
	public static String loginGetToken(String apiKey, String apiSecret) {
		if(areCredentialsValid(apiKey, apiSecret)) {
			String token = genNewUniqueToken();
			DatabaseUtils.prepareAndExecute(insertTokenSession, false, apiKey, token, DateTimeServices.getMysqlCompatibleDateTime(), DateTimeServices.getThirtyAddedDT());
			return token;
		}
		return "";
	}
	
	/**
	 * Creates the system app key and secret if it does not already exist.
	 * 
	 * @return Null, of the system account information if it was just created.
	 */
	public static String setupAccountsIfEmpty() {
		return ((Long) DatabaseUtils.prepareAndExecute(countAccountsQuery, true).get(0).getResults().get(0).get("COUNT(*)") == 0) ? createAccountQuery("AOS SYSTEM KEY", "SYSTEM", AppType.PARTNER, null, "AOS", null, null, null, null, AccessLevel.SYSTEM) : null;
	}
	
	/**
	 * Creates a new AOS app account.
	 * 
	 * @param appDesc			The description for the app/account to be created.
	 * @param appName			The name for the app/account to be created.
	 * @param appType			The {@link AppType} for the app/account to be created.
	 * @param callback			The callback URL for the app/account to be created.
	 * @param organization		The organization for the app/account to be created.
	 * @param website			The website URL for the app/account to be created.
	 * @param privacy			The privacy URL for the app/account to be created.
	 * @param terms				The terms of service URL for the app/account to be created.
	 * @param ownerID			The owner for the app/account to be created.
	 * @param accessLevel		The {@link AccessLevel} for the app/account to be created.
	 * @return The key and secret for the create app/account.
	 */
	public static String createAccountQuery(String appDesc, String appName, AppType appType, String callback, String organization, String website, String privacy, String terms, String ownerID, AccessLevel accessLevel) {
		String tryKey = SecurityUtils.genToken(64);
		boolean keyValid = false;
		while(!keyValid) {
			QueryResults qr = DatabaseUtils.prepareAndExecute(keyExistsQuery, false, tryKey).get(0);
			if((Long) qr.getResults().get(0).get("COUNT(*)") == 0) {
				keyValid = true;
			} else {
				tryKey = SecurityUtils.genToken(64);
			}
		}
		String secret = SecurityUtils.genToken(256);
		DatabaseUtils.prepareAndExecute(createAccountQuery, false, tryKey, secret, appDesc, appName, appType.getValue(), callback, organization, website, privacy, terms, ownerID, accessLevel.getValue());
		return tryKey+"."+secret;
	}
	
}
