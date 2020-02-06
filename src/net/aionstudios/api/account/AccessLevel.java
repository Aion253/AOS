package net.aionstudios.api.account;

public enum AccessLevel {

	BASE(5),
	READUSER(4),
	MODIFYUSER(3),
	READSAFE(2),
	MODIFYSAFE(1),
	SYSTEM(0);

	private final int value;

	/**
	 * Creates a {@link ResponseStatus} by value.
	 * 
	 * @param newValue An integer representing the value of an enum in this class.
	 */
	private AccessLevel(final int newValue) {
		value = newValue;
	}

	/**
	 * @return The numeric value of a definition.
	 */
	public int getValue() {
		return value;
	}
	
}
