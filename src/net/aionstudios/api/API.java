package net.aionstudios.api;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.aionlog.AnsiOut;
import net.aionstudios.aionlog.Logger;
import net.aionstudios.aionlog.StandardOverride;
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
import net.aionstudios.api.errors.InternalErrors;
import net.aionstudios.api.external.DatabaseConnector;
import net.aionstudios.api.server.APIServer;
import net.aionstudios.api.service.DatabaseServices;

public class API {
	
	private static String name;
	private static int port;
	private static boolean log;
	private static String streamPrefix;
	
	/*Why do static field modifications within an instanced class modify the fields of all instances as the declaring class? That's dumb.*/
	
	//Ubuntu server needed `iptables -I INPUT -p tcp --dport 224 -j ACCEPT` and `ufw allow 3393` run in order to make the server publicly accessible.
	//On machines running certain webservers, including Apache, the service will not be available via the use of the public IP.
	//In the case of apaache, it is recommended to set a subdomain such that it internally and silently redirects to the service. Via mod_proxy.
	/*My apache2.conf sees the addition of the following lines, preceding any other virutal host statements.
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
	
	private static APIServer server;
	
	public static boolean initAPI(String apiName, int apiPort, boolean apiLog, String apiStreamPrefix) {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		if(name!=null) {
			System.err.println("An API was already intialized for this instance! Only one API can be registered!");
			return false;
		} else {
			System.out.println("Starting AOS Server...");
		}
		name = apiName;
		port = apiPort;
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
		
		try {
			server = new APIServer(port);
		} catch (IOException e) {
			System.err.println("Encountered an IOException during server startup!");
			e.printStackTrace();
		}
		return true;
	}
	
	//https://stackoverflow.com/questions/11914445/run-a-cron-job-every-minute-only-on-specific-hours
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
		/*Register Contexts*/
		ContextManager.addContext(instanceContext);
		ContextManager.addContext(accountContext);
		ContextManager.addContext(rateLimitsContext);
		/*Register Actions*/
		instanceContext.addAction(instancePingAction);
		accountContext.addAction(accountLoginAction);
		rateLimitsContext.addAction(rateLimitsCheckAction);
		/*Error*/
		InternalErrors.initializeInternalErrors();
		
		/*Database*/
		try {
			DatabaseConnector.setupDatabase(databaseInfo.getString("db_hostname"), databaseInfo.getString("db_name"), databaseInfo.getString("db_port"), databaseInfo.getString("db_user"), databaseInfo.getString("db_pass"));
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

	public static String getName() {
		return name;
	}

	public static int getPort() {
		return port;
	}

	public static boolean isLogging() {
		return log;
	}

	public static String getStreamPrefix() {
		return streamPrefix;
	}

	public static APIServer getServer() {
		return server;
	}

}
