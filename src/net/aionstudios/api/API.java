package net.aionstudios.api;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.aionlog.AnsiOut;
import net.aionstudios.aionlog.Logger;
import net.aionstudios.aionlog.StandardOverride;
import net.aionstudios.api.actions.AccountCreateSystemAction;
import net.aionstudios.api.actions.AccountLoginAction;
import net.aionstudios.api.actions.InstancePingAction;
import net.aionstudios.api.actions.RateLimitsCheckAction;
import net.aionstudios.api.aos.AOSInfo;
import net.aionstudios.api.context.ContextManager;
import net.aionstudios.api.contexts.AccountContext;
import net.aionstudios.api.contexts.InstanceContext;
import net.aionstudios.api.contexts.RateLimitsContext;
import net.aionstudios.api.cron.CronManager;
import net.aionstudios.api.crons.PurgeExpiredSessionsCronJob;
import net.aionstudios.api.database.DatabaseConnector;
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.server.APISecureServer;
import net.aionstudios.api.server.APIServer;
import net.aionstudios.api.service.DatabaseServices;
import net.aionstudios.api.service.ResponseServices;

/**
 * A class that handles the creation of an API through the context of a static method.
 * 
 * @author Winter Roberts
 *
 */
public class API {
	
	private static String name;
	private static boolean log;
	private static String streamPrefix;
	private static boolean allowInsecure;
	
	/*Why do static field modifications within an instanced class modify the fields of all instances as the declaring class? That's dumb.*/
	
	//Ubuntu server needed `iptables -I INPUT -p tcp --dport 224 -j ACCEPT` and `ufw allow PORT_NUM` run in order to make the server publicly accessible.
	//On machines running certain web servers, including Apache, the service will not be available via the use of the public IP.
	//In the case of apache, it is recommended to set a subdomain such that it internally and silently redirects to the service. Via mod_proxy.
	/*My apache2.conf sees the addition of the following lines, preceding any other virtual host statements.
	 * <VirtualHost *:80>
		DocumentRoot "/var/www/example/public_html/api"
    	ServerName api.example.com
		ServerSignature off
    	ProxyPreserveHost On
		
    		# setup the proxy
    		<Proxy *>
        		Order allow,deny
        		Allow from all
    		</Proxy>
    		ProxyPass /api/ http://localhost:port/
    		ProxyPassReverse /api/ http://localhost:port/
		</VirtualHost>

		<VirtualHost *:443>
			DocumentRoot "/var/www/example/public_html/api"
    		ServerName api.example.com
			SSLEngine on
			SSLCertificateFile /etc/apache2/certs/cert.crt
			SSLCertificateKeyFile /etc/apache2/certs/key.key
			ServerSignature off
    		ProxyPreserveHost On

    		# setup the proxy
    		<Proxy *>
        		Order allow,deny
        		Allow from all
    		</Proxy>
    		ProxyPass /api/ http://localhost:port/
    		ProxyPassReverse /api/ http://localhost:port/
		</VirtualHost>
	 */
	//This method will, in addition, make it appear as though the user is connecting over SSL if you have an SSL certificate and effectively secure the application in respect to internet transmission without using a Java truststore.
	
	//Alternatively, I am now using Aion Forefront and will soon be adding ssl support in AOS
	
	private static APIServer server;
	private static APISecureServer secureServer;
	
	/**
	 * A one-time run method that creates a new {@link APIServer}.
	 * 
	 * @param apiName The user-level name of the API.
	 * @param apiPort The port that the service should be broadcasting on.
	 * @param apiLog Whether or not the console should be logged to a file.
	 * @param apiStreamPrefix The name to use before console outputs from the API.
	 * @return True if the service was able to start, false otherwise.
	 */
	public static boolean initAPI(String apiName, int apiPort, boolean apiLog, String apiStreamPrefix) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		if(name!=null) {
			System.err.println("An API was already intialized for this instance! Only one API can be registered!");
			return false;
		} else {
			System.out.println("Starting AOS Server...");
		}
		name = apiName;
		log = apiLog;
		streamPrefix = apiStreamPrefix;
		
		if(log) {
			File f = new File("./logs/");
			f.mkdirs();
			Logger.setup();
		}
		AnsiOut.initialize();
		AnsiOut.setStreamPrefix(streamPrefix);
		StandardOverride.enableOverride();
		
		setupDefaults();
		
		server = new APIServer(apiPort);
		return true;
	}
	
	/**
	 * A one-time run method that creates a new {@link APIServer}.
	 * 
	 * @param apiName The user-level name of the API.
	 * @param apiPort The port that the service should be broadcasting on.
	 * @param apiLog Whether or not the console should be logged to a file.
	 * @param apiStreamPrefix The name to use before console outputs from the API.
	 * @return True if the service was able to start, false otherwise.
	 */
	public static boolean initAPI(String apiName, int apiPort, boolean apiLog, String apiStreamPrefix, int securePort, boolean allowInsecure) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		if(name!=null) {
			System.err.println("An API was already intialized for this instance! Only one API can be registered!");
			return false;
		} else {
			System.out.println("Starting AOS Server...");
		}
		name = apiName;
		log = apiLog;
		streamPrefix = apiStreamPrefix;
		API.allowInsecure = allowInsecure;
		
		if(log) {
			File f = new File("./logs/");
			f.mkdirs();
			Logger.setup();
		}
		File f = new File("./certs/");
		f.mkdirs();
		AnsiOut.initialize();
		AnsiOut.setStreamPrefix(streamPrefix);
		StandardOverride.enableOverride();
		
		setupDefaults();
		
		if(API.allowInsecure) {
			server = new APIServer(apiPort);
		}
		if(!startSecureServer(securePort)&&!API.allowInsecure) {
			System.out.println("Secure mode is disabled or misconfigured! No server started; closing.");
			System.exit(0);
		}
		return true;
	}
	
	//https://stackoverflow.com/questions/11914445/run-a-cron-job-every-minute-only-on-specific-hours
	/**
	 * Sets up AOS internal {@link Action}s, {@link Context}s, {@link AOSError}s and {@link CronJob}s.
	 */
	private static void setupDefaults() {
		AOSInfo.readConfigsAtStart();
		JSONObject databaseInfo = AOSInfo.getDatabaseInfo();
		/*Contexts*/
		InstanceContext instanceContext = new InstanceContext();
		AccountContext accountContext = new AccountContext();
		RateLimitsContext rateLimitsContext = new RateLimitsContext();
		/*Action*/
		InstancePingAction instancePingAction = new InstancePingAction();
		AccountLoginAction accountLoginAction = new AccountLoginAction();
		RateLimitsCheckAction rateLimitsCheckAction = new RateLimitsCheckAction();
		AccountCreateSystemAction accountCreateSystemAction = new AccountCreateSystemAction();
		/*Register Contexts*/
		ContextManager.registerContext(instanceContext);
		ContextManager.registerContext(accountContext);
		ContextManager.registerContext(rateLimitsContext);
		/*Register Actions*/
		instanceContext.registerAction(instancePingAction);
		accountContext.registerAction(accountLoginAction);
		rateLimitsContext.registerAction(rateLimitsCheckAction);
		accountContext.registerAction(accountCreateSystemAction);
		/*Error*/
		InternalErrors.initializeInternalErrors();
		
		/*Database*/
		try {
			DatabaseConnector.setupDatabase(databaseInfo.getString("db_hostname"), databaseInfo.getString("db_name"), databaseInfo.getString("db_port"), databaseInfo.getString("db_user"), databaseInfo.getString("db_pass"), true, "UTC");
		} catch (JSONException e) {
			System.err.println("Encountered a JSONException during database setup!");
			e.printStackTrace();
			System.exit(0);
		}
		DatabaseServices.createTables();
		
		/*Cron*/
		PurgeExpiredSessionsCronJob purgeExpiredSessionsCronJob = new PurgeExpiredSessionsCronJob();
		CronManager.addJob(purgeExpiredSessionsCronJob);
		CronManager.startCron();
	}

	/**
	 * @return The name of the API.
	 */
	public static String getName() {
		return name;
	}

	/**
	 * @return Whether or not logging is enabled.
	 */
	public static boolean isLogging() {
		return log;
	}

	/**
	 * @return The log and console stream prefix.
	 */
	public static String getStreamPrefix() {
		return streamPrefix;
	}

	/**
	 * @return The {@link APIServer} associated with this AOS instance.
	 */
	public static APIServer getServer() {
		return server;
	}
	
	public static APISecureServer getSecureServer() {
		return secureServer;
	}
	
	/**
	 * Initializes a {@link JDCSecureServer} if config files dictate to do so. HTTPS is disabled by default.
	 */
	private static boolean startSecureServer(int port) {
		File certsConfig = new File("./certs.json");
		JSONObject certsJson = ResponseServices.getLinkedJsonObject();
		if(certsConfig.exists()) {
			certsJson = AOSInfo.readConfig(certsConfig);
		} else {
			System.out.println("Certificate config was missing. Secure mode disabled by default!");
			try {
				certsConfig.createNewFile();
				certsJson.put("enable_ssl_server", false);
				certsJson.put("jks_certificate", "cert.jks");
				certsJson.put("store_password", "changeit");
				certsJson.put("key_password", "changeit");
				certsJson.put("cert_alias", "certificate");
				AOSInfo.writeConfig(certsJson, certsConfig);
			} catch (IOException e) {
				System.err.println("Encountered an IOException during config file operations!");
				e.printStackTrace();
			} catch (JSONException e) {
				System.err.println("Encountered an IOException during config file operations!");
				e.printStackTrace();
			}
		}
		try {
			boolean enabled = certsJson.getBoolean("enable_ssl_server");
			if(enabled) {
				String jksCertificate = certsJson.getString("jks_certificate");
				String storePassword = certsJson.getString("store_password");
				String keyPassword = certsJson.getString("key_password");
				String certAlias = certsJson.getString("cert_alias");
				secureServer = new APISecureServer(port);
				secureServer.startServer(jksCertificate, storePassword, keyPassword, certAlias);
				return true;
			}
		} catch (JSONException e1) {
			System.err.println("Failed to interpret processor config!");
			e1.printStackTrace();
		}
		return false;
	}

}
