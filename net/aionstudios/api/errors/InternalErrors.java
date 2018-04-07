package net.aionstudios.api.errors;

import java.util.Arrays;
import java.util.Map;

import net.aionstudios.api.aos.RateEventReference;
import net.aionstudios.api.error.ErrorManager;
import net.aionstudios.api.response.Response;

public class InternalErrors {
	
	public static NoSuchContextError noSuchContextError = null;
	public static NoSuchActionError noSuchActionError = null;
	public static NoResponseError noResponseError = null;
	public static NoContextDefaultError noContextDefaultError = null;
	public static MissingGetParametersError missingGetParametersError = null;
	public static MissingPostParametersError missingPostParametersError = null;
	public static RateLimitError rateLimitError = null;
	
	public static InvalidCredentialsError invalidCredentialsError = null;
	public static InvalidSessionError invalidSessionError = null;
	public static UnauthorizedAccessError unauthorizedAccessError = null;
	
	public static boolean initializeInternalErrors() {
		if(noSuchContextError==null) {
			noSuchContextError = new NoSuchContextError();ErrorManager.registerError(noSuchContextError);
			noSuchActionError = new NoSuchActionError();ErrorManager.registerError(noSuchActionError);
			noResponseError = new NoResponseError();ErrorManager.registerError(noResponseError);
			noContextDefaultError = new NoContextDefaultError();ErrorManager.registerError(noContextDefaultError);
			missingGetParametersError = new MissingGetParametersError();ErrorManager.registerError(missingGetParametersError);
			missingPostParametersError = new MissingPostParametersError();ErrorManager.registerError(missingPostParametersError);
			rateLimitError = new RateLimitError();ErrorManager.registerError(rateLimitError);
			
			invalidCredentialsError = new InvalidCredentialsError();ErrorManager.registerError(invalidCredentialsError);
			invalidSessionError = new InvalidSessionError();ErrorManager.registerError(invalidSessionError);
			unauthorizedAccessError = new UnauthorizedAccessError();ErrorManager.registerError(unauthorizedAccessError);
			
			return true;
		}
		return false;
	}
	
	public static void noSuchContextError(Response e, String requestContext) {
		e.putErrorResponse(noSuchContextError, "No such context '"+requestContext+"'");
	}
	
	public static void noSuchActionError(Response e, String requestContext, Map<String, String> getQuery) {
		e.putErrorResponse(noSuchActionError, "No such action '"+getQuery.get("action")+"' for context '"+requestContext+"'");
	}
	
	public static void emptyAPIResponseError(Response e, String requestContext, Map<String, String> getQuery) {
		e.putErrorResponse(noResponseError, "Empty response on action '"+getQuery.get("action")+"' for context '"+requestContext+"'");
	}
	
	public static void noContextDefaultError(Response e, String requestContext) {
		e.putErrorResponse(noContextDefaultError, "No context default for context '"+requestContext+"'");
	}
	
	public static void missingGetParameters(Response e, String requestContext, Map<String, String> getQuery, String[] parameters) {
		e.putErrorResponse(missingGetParametersError, "Action '"+getQuery.get("action")+"' expected parameters "+Arrays.toString(parameters));
	}
	
	public static void missingPostParameters(Response e, String requestContext, Map<String, String> getQuery, String[] parameters) {
		e.putErrorResponse(missingPostParametersError, "Action '"+getQuery.get("action")+"' expected parameters "+Arrays.toString(parameters));
	}
	
	public static void rateLimitError(Response e, RateEventReference type) {
		e.putErrorResponse(rateLimitError, "Your "+type.getTypeName()+" has exceeded it's maximum "+type.getMaxRequestsPerHour()+" requests per hour!");
	}
	
	public static void invalidSessionError(Response e) {
		e.putErrorResponse(invalidSessionError, "The provided apiToken was not registered to any account!");
	}

}
