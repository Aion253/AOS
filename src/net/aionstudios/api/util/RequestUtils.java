package net.aionstudios.api.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class RequestUtils {
	
	public static Map<String, String> resolveGetQuery(String qs) {
	    Map<String, String> result = new HashMap<>();
	    if (qs == null)
	        return result;

	    int last = 0, next, l = qs.length();
	    while (last < l) {
	        next = qs.indexOf('&', last);
	        if (next == -1)
	            next = l;

	        if (next > last) {
	            int eqPos = qs.indexOf('=', last);
	            try {
	                if (eqPos < 0 || eqPos > next)
	                    result.put(URLDecoder.decode(qs.substring(last, next), "utf-8"), "");
	                else
	                    result.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"), URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
	            } catch (UnsupportedEncodingException e) {
	                throw new RuntimeException(e); // will never happen, utf-8 support is mandatory for java
	            }
	        }
	        last = next + 1;
	    }
	    return result;
	}
	
	public static Map<String, String> resolvePostQuery(HttpExchange httpExchange) throws IOException {
		  Map<String, String> parameters = new HashMap<>();
		  InputStream inputStream = httpExchange.getRequestBody();
		  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		  byte[] buffer = new byte[2048];
		  int read = 0;
		  while ((read = inputStream.read(buffer)) != -1) {
			  byteArrayOutputStream.write(buffer, 0, read);
		  }
		  String[] keyValuePairs = byteArrayOutputStream.toString().split("&");
		  for (String keyValuePair : keyValuePairs) {
		    String[] keyValue = keyValuePair.split("=");
		    if (keyValue.length != 2) {
		      continue;
		    }
		    parameters.put(keyValue[0], keyValue[1]);
		  }
		  return parameters;
	}

}
