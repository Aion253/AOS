package net.aionstudios.api.aos;

/**
 * Enumerates possible response statuses.
 * 
 * @author Winter Roberts
 *
 */
public enum ResponseStatus {
	
	SUCCESS(1);

	private final int value;

	/**
	 * Creates a {@link ResponseStatus} by value.
	 * 
	 * @param newValue An integer representing the value of an enum in this class.
	 */
	ResponseStatus(final int newValue) {
		value = newValue;
	}

	/**
	 * @return The numeric value of a definition.
	 */
	public int getStatus() {
		return value;
	}
	
	/**
	 * @return A string representing the name of this {@link ResponseStatus}.
	 */
	public String getStatusType() {
		if(value==1) {
			return "Success";
		}
		return "Unrecognized";
	}
	
	/**
	 * @return A string describing what this {@link ResponseStatus} means.
	 */
	public String getStatusDesc() {
		if(value==1) {
			return "The request was completed without serious error and returned valid data.";
		}
		return "No Description Provided";
	}

}
