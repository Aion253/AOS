package net.aionstudios.api.account;

public enum AppType {

	PUBLIC(2),
	PRIVATE(1),
	PARTNER(0);

	private final int value;

	/**
	 * Creates a {@link ResponseStatus} by value.
	 * 
	 * @param newValue An integer representing the value of an enum in this class.
	 */
	private AppType(final int newValue) {
		value = newValue;
	}

	/**
	 * @return The numeric value of a definition.
	 */
	public int getValue() {
		return value;
	}
	
}
