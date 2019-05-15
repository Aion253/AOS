package net.aionstudios.api.service;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import net.aionstudios.api.compression.CompressionEncoding;
import net.aionstudios.api.compression.DeflateCompressor;
import net.aionstudios.api.compression.GZIPCompressor;

/**
 * A class providing services relating to AOS responses.
 * 
 * @author Winter Roberts
 *
 */
public class ResponseServices {
	
	/**
	 * Generates a response to the client.
	 * 
	 * @param he The HTTPExchange on which to respond.
	 * @param httpResponseCode The HTTP response code.
	 * @param response The response (likely a serialized {@link JSONObject}).
	 * @return True if the response was sent successfully, false otherwise.
	 */
	public static boolean generateHTTPResponse(HttpExchange he, int httpResponseCode, String response, CompressionEncoding ce) {
		try {
			byte[] respBytes;
			Headers respHeaders = he.getResponseHeaders();
			respHeaders.set("Content-Type", "application/json");
			if(ce.getValue()==CompressionEncoding.GZIP.getValue()) {
				respHeaders.set("Content-Encoding", "gzip");
				respBytes = GZIPCompressor.compress(response);
			} else if (ce.getValue()==CompressionEncoding.DEFLATE.getValue()) {
				respHeaders.set("Content-Encoding", "deflate");
				respBytes = DeflateCompressor.compress(response);
			} else {
				respBytes = response.getBytes(StandardCharsets.UTF_8);
			}
			he.sendResponseHeaders(200, respBytes.length);
			OutputStream os = he.getResponseBody();
			os.write(respBytes);
			os.flush();
			os.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Empties a {@link JSONObject}.
	 * 
	 * @param j An empty {@link JSONObject} that retains modified structure and references.
	 */
	public static void clearJsonObject(JSONObject j) {
		while(j.length()>0) {
			j.remove((String) j.keys().next());
		}
	}
	
	/**
	 * Creates a new {@link JSONObject}, forcing it to use a LinkedHashMap instead of an unlinked one.
	 * 
	 * @return An empty {@link JSONObject} with modified structure.
	 */
	public static JSONObject getLinkedJsonObject() {
		JSONObject j = new JSONObject();
		Field map;
		try {
			map = j.getClass().getDeclaredField("map");
			map.setAccessible(true);
			map.set(j, new LinkedHashMap<>());
			map.setAccessible(false);
		} catch (NoSuchFieldException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (SecurityException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("JSONObject re-link failed!");
			e.printStackTrace();
		}
		return j;
	}

}
