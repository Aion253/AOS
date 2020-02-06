package net.aionstudios.api.service;
import net.aionstudios.api.util.DatabaseUtils;

/**
 * A class providing services relating to AOS's database management.
 * 
 * @author Winter Roberts
 *
 */
public class DatabaseServices {
	
	private static String createAccountsTable = "CREATE TABLE IF NOT EXISTS `AOSAccounts` (" + 
			" `apiKey` varchar(64) NOT NULL," + 
			" `apiSecret` varchar(256) NOT NULL," + 
			" `appDesc` text NULL," + 
			" `appName` varchar(64) NOT NULL," + 
			" `appType` tinyint(1) NOT NULL DEFAULT '2'," + 
			" `callback` text NULL," + 
			" `organization` varchar(64) NULL," + 
			" `website` text NULL," + 
			" `privacy` text NULL," + 
			" `terms` text NULL," + 
			" `owner` varchar(64) NULL," + 
			" `accessLevel` tinyint(1) NOT NULL DEFAULT '5'," + 
			" PRIMARY KEY (`apiKey`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE latin1_general_cs";
	
	private static String createSessionsTable = "CREATE TABLE IF NOT EXISTS `AOSSessions` (" + 
			" `apiKey` varchar(64) NOT NULL," + 
			" `apiToken` varchar(512) NOT NULL," + 
			" `validFrom` datetime NOT NULL," + 
			" `validTo` datetime NOT NULL," + 
			" PRIMARY KEY (`apiToken`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE latin1_general_cs";
	
	private static String createEventsTable = "CREATE TABLE IF NOT EXISTS `AOSEvents` (" + 
			" `eventID` varchar(16) NOT NULL," + 
			" `apiKey` varchar(64) NULL," + 
			" `ip` varchar(64) NOT NULL," + 
			" `startTime` datetime NOT NULL," +
			" `context` text NOT NULL," +
			" `getData` text NULL," +
			" `postData` text NULL," +
			" `status` varchar(16) NOT NULL," + 
			" `statusType` text NOT NULL," + 
			" `statusDesc` text NOT NULL," + 
			" `message` text NOT NULL," + 
			" PRIMARY KEY (`eventID`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE latin1_general_cs";
	
	/**
	 * Creates database tables necessary to AOS's function.
	 */
	public static void createTables() {
		DatabaseUtils.prepareAndExecute(createAccountsTable, true);
		DatabaseUtils.prepareAndExecute(createSessionsTable, true);
		DatabaseUtils.prepareAndExecute(createEventsTable, true);
	}

}
