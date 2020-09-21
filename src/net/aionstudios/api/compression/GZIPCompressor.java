package net.aionstudios.api.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

/**
 * A utility class for preparing a response to a GZIP stream.
 * 
 * @author Winter Roberts
 *
 */
public class GZIPCompressor {
	
	/**
	 * Converts the input String to a GZIP byte array.
	 * 
	 * @param str The response to be sent.
	 * @return A byte array, the GZIP-encoded representation of the input string.
	 * @throws IOException
	 */
	public static byte[] compress(String str) throws IOException {
	    if ((str == null) || (str.length() == 0)) {
	      return null;
	    }
	    ByteArrayOutputStream obj = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(obj);
	    gzip.write(str.getBytes(StandardCharsets.UTF_8));
	    gzip.flush();
	    gzip.close();
	    return obj.toByteArray();
	}

}
