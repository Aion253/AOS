package net.aionstudios.api.errors;

import java.util.Arrays;
import java.util.Map;

import net.aionstudios.api.aos.RateEventReference;
import net.aionstudios.api.error.ErrorManager;
import net.aionstudios.api.response.Response;

/**
 * Allows AOS to call internal errors without directly referencing their classes.
 * 
 * @author Winter Roberts
 *
 */
public class InternalErrors {
	
	public static NoSuchContextError noSuchContextError = null;
	public static NoSuchActionError noSuchActionError = null;
	public static NoResponseError noResponseError = null;
	public static NoContextDefaultError noContextDefaultError = null;
	public static MissingGetParametersError missingGetParametersError = null;
	public static MissingPostParametersError missingPostParametersError = null;
	public static MissingFileParametersError missingFileParametersError = null;
	public static RateLimitError rateLimitError = null;
	
	public static InvalidCredentialsError invalidCredentialsError = null;
	public static InvalidSessionError invalidSessionError = null;
	public static UnauthorizedAccessError unauthorizedAccessError = null;
	
	/**
	 * Creates instances of each internal {@link AOSError} and registers them to the {@link ErrorManager}.
	 * 
	 * @return True if the errors were initialized, false if they already had been.
	 */
	public static boolean initializeInternalErrors() {
		if(noSuchContextError==null) {
			noSuchContextError = new NoSuchContextError();ErrorManager.registerError(noSuchContextError);
			noSuchActionError = new NoSuchActionError();ErrorManager.registerError(noSuchActionError);
			noResponseError = new NoResponseError();ErrorManager.registerError(noResponseError);
			noContextDefaultError = new NoContextDefaultError();ErrorManager.registerError(noContextDefaultError);
			missingGetParametersError = new MissingGetParametersError();ErrorManager.registerError(missingGetParametersError);
			missingPostParametersError = new MissingPostParametersError();ErrorManager.registerError(missingPostParametersError);
			missingFileParametersError = new MissingFileParametersError();ErrorManager.registerError(missingFileParametersError);
			rateLimitError = new RateLimitError();ErrorManager.registerError(rateLimitError);
			
			invalidCredentialsError = new InvalidCredentialsError();ErrorManager.registerError(invalidCredentialsError);
			invalidSessionError = new InvalidSessionError();ErrorManager.registerError(invalidSessionError);
			unauthorizedAccessError = new UnauthorizedAccessError();ErrorManager.registerError(unauthorizedAccessError);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Switches the {@link Response} to that of a NoContextSuchError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of an unregistered {@link Context} requested by the client.
	 */
	public static void noSuchContextError(Response e, String requestContext) {
		e.putErrorResponse(noSuchContextError, "No such context '"+requestContext+"'");
	}
	
	/**
	 * Switches the {@link Response} to that of a NoSuchActionError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of an unregistered {@link Action} for the given {@link Context} requested by the client.
	 * @param getQuery The get parameters in the request url.
	 */
	public static void noSuchActionError(Response e, String requestContext, Map<String, String> getQuery) {
		e.putErrorResponse(noSuchActionError, "No such action '"+getQuery.get("action")+"' for context '"+requestContext+"'");
	}
	
	/**
	 * Switches the {@link Response} to that of a EmptyAPIResponseError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of an unregistered {@link Action} for the given {@link Context} requested by the client.
	 * @param getQuery The get parameters in the request url.
	 */
	public static void emptyAPIResponseError(Response e, String requestContext, Map<String, String> getQuery) {
		e.putErrorResponse(noResponseError, "Empty response on action '"+getQuery.get("action")+"' for context '"+requestContext+"'");
	}
	
	/**
	 * Switches the {@link Response} to that of a NoContextDefaultError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of the {@link Context} requested by the client.
	 */
	public static void noContextDefaultError(Response e, String requestContext) {
		e.putErrorResponse(noContextDefaultError, "No context default for context '"+requestContext+"'");
	}
	
	/**
	 * Switches the {@link Response} to that of a MissingGetParametersError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of the {@link Action} for the given {@link Context} requested by the client.
	 * @param getQuery The get parameters in the request url.
	 * @param parameters The get parameters required by the requested {@link Action}
	 */
	public static void missingGetParameters(Response e, String requestContext, Map<String, String> getQuery, String[] parameters) {
		e.putErrorResponse(missingGetParametersError, "Action '"+getQuery.get("action")+"' expected parameters "+Arrays.toString(parameters));
	}
	
	/**
	 * Switches the {@link Response} to that of a MissingPostParametersError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param requestContext The string name of the {@link Action} for the given {@link Context} requested by the client.
	 * @param getQuery The get parameters in the request url.
	 * @param parameters The post parameters required by the requested {@link Action}
	 */
	public static void missingPostParameters(Response e, String requestContext, Map<String, String> getQuery, String[] parameters) {
		e.putErrorResponse(missingPostParametersError, "Action '"+getQuery.get("action")+"' expected parameters "+Arrays.toString(parameters));
	}
	
	public static void missingFileParameters(Response e, String requestContext, Map<String, String> getQuery, String[] parameters) {
		e.putErrorResponse(missingFileParametersError, "Action '"+getQuery.get("action")+"' expected parameters "+Arrays.toString(parameters));
	}
	
	/**
	 * Switches the {@link Response} to that of a RateLimitError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 * @param type The {@link RateEventReference} type that was rate limited.
	 */
	public static void rateLimitError(Response e, RateEventReference type) {
		e.putErrorResponse(rateLimitError, "Your "+type.getTypeName()+" has exceeded it's maximum "+type.getMaxRequestsPerHour()+" requests per hour!");
	}
	
	/**
	 * Switches the {@link Response} to that of a InvalidSessionError.
	 * 
	 * @param e	The {@link Response} to be sent back to the client.
	 */
	public static void invalidSessionError(Response e) {
		e.putErrorResponse(invalidSessionError, "The provided apiToken was not registered to any account!");
	}

}
