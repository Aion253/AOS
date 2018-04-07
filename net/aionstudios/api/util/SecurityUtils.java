package net.aionstudios.api.util;

import java.security.SecureRandom;

public class SecurityUtils {
	
	private static final String tokenChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	/**
	 * Generates a token of the given length
	 * @param length	The length of token to generate
	 * @return	A token
	 */
	public static String genToken(int length){
	   StringBuilder sb = new StringBuilder(length);
	   for( int i = 0; i < length; i++ ) 
	      sb.append( tokenChars.charAt( rnd.nextInt(tokenChars.length()) ) );
	   return sb.toString();
	}

}
