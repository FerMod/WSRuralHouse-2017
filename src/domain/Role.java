package domain;

/**
 * Role of the user
 */
public enum Role {
	CLIENT(0),
	OWNER(1),		
	ADMIN(2),
	SUPER_ADMIN(3);

	private final int role;

	private Role(int role) {
		this.role = role;
	}

	public int getValue() {
		return this.role;
	}

}
