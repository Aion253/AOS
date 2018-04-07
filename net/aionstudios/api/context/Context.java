package net.aionstudios.api.context;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import net.aionstudios.api.action.Action;
import net.aionstudios.api.response.Response;

public abstract class Context {
	
	private String context;
	private List<Action> contextActions = new ArrayList<Action>();
	
	public Context(String handlerContext) {
		context = "/"+handlerContext;
	}

	public boolean addAction(Action action) {
		for(Action a: contextActions) {
			if(action.getAction().equals(a.getAction())) {
				System.err.println("Attempt to register duplicate action '"+action.getAction()+"' failed! An action by the same name has already reserved the call!");
				return false;
			}
		}
		contextActions.add(action);
		return true;
	}
	
	public boolean removeContext(Action action) {
		return contextActions.remove(action);
	}
	
	public Action findAction(String action) {
		for(Action a: contextActions) {
			if(a.getAction().equals(action)) {
				return a;
			}
		}
		return null;
	}
	
	public abstract void contextDefault(Response response, String requestContext) throws JSONException;
	
	public String getContext() {
		return context;
	}

}
