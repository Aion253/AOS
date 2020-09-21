package net.aionstudios.api.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DeflaterOutputStream;

/**
 * A utility class for preparing a response to a deflator stream.
 * 
 * @author Winter Roberts
 *
 */
public class DeflateCompressor {

	/**
	 * Converts the input String to a deflator byte array.
	 * 
	 * @param str The response to be sent.
	 * @return A byte array, the deflator-encoded representation of the input string.
	 * @throws IOException
	 */
	public static byte[] compress(String str) throws IOException {
	    if ((str == null) || (str.length() == 0)) {
	      return null;
	    }
	    ByteArrayOutputStream obj = new ByteArrayOutputStream();
	    DeflaterOutputStream deflate = new DeflaterOutputStream(obj);
	    deflate.write(str.getBytes(StandardCharsets.UTF_8));
	    deflate.flush();
	    deflate.close();
	    return obj.toByteArray();
	}
	
}
