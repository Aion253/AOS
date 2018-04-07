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

public class AOSInfo {
	
	public static String AOS_VERSION = "1.0.0.";
	public static String AOS_NAME = "Aion Online Services Server";
	public static String AOS_DBCONFIG = "./config/aos_db.json";
	
	public static String JAVA_VERSION = System.getProperty("java.runtime.version");
	
	private static JSONObject dbconfig = ResponseServices.getLinkedJsonObject();
	
	public static String getBlankRootString() {
		return "You've reached an API endpoint identifying itself as '"+API.getName()+"'\n"
				+ "The server is online!\n\n"
				+ "Aion Online Services Server (1.0.0) running on port "+API.getPort();
	}
	
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
	
	public static JSONObject getDatabaseInfo() {
		return new JSONObject(dbconfig, JSONObject.getNames(dbconfig));
	}

}
