package net.aionstudios.api.context;

import java.util.ArrayList;
import java.util.List;

public class ContextManager {
	
	private static List<Context> contexts = new ArrayList<Context>();
	
	public static boolean addContext(Context con) {
		for(Context c: contexts) {
			if(con.getContext().equals(c.getContext())) {
				System.err.println("Attempt to register duplicate context '"+con.getContext()+"' failed! A context by the same name has already reserved the call!");
				return false;
			}
		}
		contexts.add(con);
		return true;
	}
	
	public static boolean removeContext(Context con) {
		return contexts.remove(con);
	}
	
	public static Context findContext(String context) {
		for(Context c: contexts) {
			if(c.getContext().equals(context)) {
				return c;
			}
		}
		return null;
	}

}
