package net.aionstudios.api.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressor {
	
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
