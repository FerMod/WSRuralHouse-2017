package exceptions;

/**
 * This exception is throwed if there is a problem when constructor of DB4oManager is executed
 */
public class DB4oManagerCreationException extends Exception {
	private static final long serialVersionUID = 1L;

	public DB4oManagerCreationException() {
		super();
	}

	public DB4oManagerCreationException(String s) {
		super(s);
	}
	
}