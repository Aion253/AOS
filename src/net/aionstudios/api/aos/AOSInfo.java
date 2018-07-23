package net.aionstudios.api.aos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONException;
import org.json.JSONObject;

import net.aionstudios.api.API;
import net.aionstudios.api.service.ResponseServices;

/**
 * A class containing information and fields that should be statically reference-able to all parts of the application at all times.
 * 
 * @author Winter Roberts
 *
 */
public class AOSInfo {
	
	public static final String AOS_VERSION = "1.0.0.";
	public static final String AOS_NAME = "Aion Online Services Server";
	public static final String AOS_DBCONFIG = "./config/aos_db.json";
	
	public static final String JAVA_VERSION = System.getProperty("java.runtime.version");
	
	private static JSONObject dbconfig = ResponseServices.getLinkedJsonObject();
	
	/**
	 * Called by the {@link ContextHandler} if no {@link Context} is provided in a request url.
	 * 
	 * @return A String providing information for those who have reached the API endpoint but failed to make a request.
	 */
	public static String getBlankRootString() {
		return "You've reached an API endpoint identifying itself as '"+API.getName()+"'\n"
				+ "The server is online!\n\n"
				+ "Aion Online Services Server (1.0.0) running on port "+API.getPort();
	}
	
	/**
	 * Reads configurable information when the server starts and handles setup if necessary.
	 * Should a config file not exist it will be created and the application terminated.
	 * 
	 * @return True if the config was available and processed, false otherwise.
	 */
	public static boolean readConfigsAtStart() {
		try {
			File dbcf = new File(AOS_DBCONFIG);
			if(!dbcf.exists()) {
				dbcf.getParentFile().mkdirs();
				dbcf.createNewFile();
				System.err.println("Fill out the config at "+dbcf.getCanonicalPath()+" and restart the server!");
				dbconfig.put("db_hostname", "");
				dbconfig.put("db_port", "");
				dbconfig.put("db_name", "");
				dbconfig.put("db_user", "");
				dbconfig.put("db_pass", "");
				writeConfig(dbconfig, dbcf);
				System.exit(0);
			} else {
				dbconfig = readConfig(dbcf);
			}
			return true;
		} catch (IOException e) {
			System.err.println("Encountered an IOException during config file operations!");
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			System.err.println("Encountered an JSONException during config file operations!");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Writes the provided {@link JSONObject} to the file system, optimistically as a configuration file.
	 * 
	 * @param j	The {@link JSONObject} to be serialized into the file system.
	 * @param f	The {@link File} object identifying where the {@link JSONObject} should be saved onto the file system.
	 * @return True if the file was written without error, false otherwise.
	 */
	public static boolean writeConfig(JSONObject j, File f) {
		try {
			if(!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
				System.out.println("Created config file '"+f.toString()+"'!");
			}
			PrintWriter writer;
			File temp = File.createTempFile("temp_andf", null, f.getParentFile());
			writer = new PrintWriter(temp.toString(), "UTF-8");
			writer.println(j.toString(2));
			writer.close();
			Files.deleteIfExists(f.toPath());
			temp.renameTo(f);
			return true;
		} catch (IOException e) {
			System.err.println("Encountered an IOException while writing config: '"+f.toString()+"'!");
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			System.err.println("Encountered a JSONException while writing config: '"+f.toString()+"'!");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Deserializes a {@link JSONObject} from a file on the file system and returns it.
	 * 
	 * @param f	The {@link File} object, representing a file containing JSON data on the file system.
	 * @return	A {@link JSONObject} representing the file provided or null if it could not be read.
	 */
	public static JSONObject readConfig(File f) {
		if(!f.exists()) {
			System.err.println("Failed reading config: '"+f.toString()+"'. No such file!");
			return null;
		}
		String jsonString = "";
		try (BufferedReader br = new BufferedReader(new FileReader(f.toString()))) {
		    for (String line; (line = br.readLine()) != null;) {
		    	jsonString += line;
		    }
		    br.close();
		    return new JSONObject(jsonString);
		} catch (IOException e) {
			System.err.println("Encountered an IOException while reading config: '"+f.toString()+"'!");
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			System.err.println("Encountered a JSONException while reading config: '"+f.toString()+"'!");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return A {@link JSONObject} containing information from the database config file in a way that the prevents the original from being modified.
	 */
	public static JSONObject getDatabaseInfo() {
		return new JSONObject(dbconfig, JSONObject.getNames(dbconfig));
	}

}
