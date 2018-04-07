package net.aionstudios.api.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
	
	private static List<AOSError> errors = new ArrayList<AOSError>();
	
	public static boolean registerError(AOSError error) {
		for(AOSError e : errors) {
			if(e.getName().equals(error.getName())) {
				System.out.println("An error identified as '"+e.getName()+"' was already registered! This new instance couldn't be");
				return false;
			}
		}
		errors.add(error);
		return true;
	}
	
	public static AOSError getErrorByName(String error) {
		for(AOSError e : errors) {
			if(e.getName().equals(error)) {
				return e;
			}
		}
		return null;
	}
	
	public static int getErrorNumber(String error) {
		for(int i = 0; i < errors.size(); i++) {
			if(errors.get(i).getName().equals(error)) {
				return i+1;
			}
		}
		System.err.println("No such error '"+error+"'");
		return -1;
	}

}
