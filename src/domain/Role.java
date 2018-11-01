package domain;

/**
 * Role of the user
 */
public enum Role {
	CLIENT(0),
	PARTICULAR_CLIENT(1),
	TRAVEL_AGENCY(2),
	OWNER(3),
	ADMIN(4),
	SUPER_ADMIN(5);

	private final int role;

	private Role(int role) {
		this.role = role;
	}

	public int getValue() {
		return this.role;
	}

}
