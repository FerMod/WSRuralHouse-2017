package domain;

/**
 * UserType of the user
 */
public enum UserType {
	CLIENT(0),
	PARTICULAR_CLIENT(1),
	TRAVEL_AGENCY(2),
	OWNER(3),
	ADMIN(4),
	SUPER_ADMIN(5);

	private final int type;

	private UserType(int type) {
		this.type = type;
	}

	public int getValue() {
		return this.type;
	}

}
