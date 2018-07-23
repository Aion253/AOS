package net.aionstudios.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.aionstudios.api.util.DatabaseUtils;

/**
 * A class for assisting in connecting to and executing queries on MySQL databases using JDBC.
 * 
 * @author Winter Roberts
 *
 */
public class DatabaseConnector {
	
	private static String host = "";
	private static String user = "";
	private static String password = "";
	private static String database = "";
	
	private static boolean loadedJDBC = false;
	
	private static Connection db;
	
	/**
	 * Connects and logs into a MySQL database.
	 * 
	 * @param hostname The hostname of the database server.
	 * @param databaseName The name of database to be logged in to.
	 * @param databasePort The port on which the database accepts connections.
	 * @param databaseUser The username of a database user with access to the provided database.
	 * @param databasePassword The password of the database user provided in the databaseUser argument.
	 * @return True if the database is functioning and accepted the connection, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databasePort, String databaseUser, String databasePassword) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname+":"+databasePort;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
				return false;
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET latin1 COLLATE latin1_general_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
				return false;
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	/**
	 * Connects and logs into a MySQL database using the default port.
	 * 
	 * @param hostname The hostname of the database server.
	 * @param databaseName The name of database to be logged in to.
	 * @param databaseUser The username of a database user with access to the provided database.
	 * @param databasePassword The password of the database user provided in the databaseUser argument.
	 * @return True if the database is functioning and accepted the connection, false otherwise.
	 */
	public static boolean setupDatabase(String hostname, String databaseName, String databaseUser, String databasePassword) {
		if(host=="") {
			host = "jdbc:mysql://"+hostname;
			database = databaseName;
			user = databaseUser;
			password = databasePassword;
			try {
				loadJDBC();
				db = DriverManager.getConnection(host, user, password);
			} catch (SQLException e1) {
				System.err.println("DatabaseConnector failed while connecting to database '"+database+"'!");
				e1.printStackTrace();
			}
			try {
				DatabaseUtils.prepareAndExecute("CREATE DATABASE IF NOT EXISTS `"+database+"` CHARACTER SET latin1 COLLATE latin1_general_cs;", false);
				getDatabase().setCatalog(database);
			} catch (SQLException e) {
				System.err.println("DatabaseConnector failed while switching to database '"+database+"'!");
				e.printStackTrace();
			}
			return true;
		}
		System.err.println("Only one database can be connected per instance.");
		return false;
	}
	
	/**
	 * Loads the database drive for JDBC if it hasn't been already
	 */
	public static void loadJDBC() {
		if(loadedJDBC==false) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				System.out.println("Failed loading JDBC");
				e1.printStackTrace();
			}
		}
		loadedJDBC = true;
	}
	
	/**
	 * Gets an instance of the database to query through
	 * @return	A database connection
	 */
	public static Connection getDatabase() {
		return db;
	}

}
