package exceptions;

/**
 * Thrown when a account authentication fails.
 */
public class AuthException extends Exception {

	private static final long serialVersionUID = -7963009307719576712L;

	/**
	 * Constructs a {@code AuthException} with a default message.
	 */
	public AuthException() {
		super("Authentification failed.");
	}

	/**
	 * Constructs a {@code AuthException} with a default message.
	 * @param s the message
	 */
	public AuthException(String s) {
		super(s);
	}

}
