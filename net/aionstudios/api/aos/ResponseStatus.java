package net.aionstudios.api.aos;

public enum ResponseStatus {
	
	SUCCESS(1);

	private final int value;

	ResponseStatus(final int newValue) {
		value = newValue;
	}

	/**
	 * @return The numeric value of a definition.
	 */
	public int getStatus() {
		return value;
	}
	
	public String getStatusType() {
		if(value==1) {
			return "Success";
		}
		return "Unrecognized";
	}
	
	public String getStatusDesc() {
		if(value==1) {
			return "The request was completed without serious error and returned valid data.";
		}
		return "No Description Provided";
	}

}
