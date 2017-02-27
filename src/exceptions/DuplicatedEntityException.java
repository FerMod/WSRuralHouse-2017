package exceptions;

/** 
 * Thrown when is attempted to add an existing entity to the data base.
 */
public class DuplicatedEntityException extends Exception {

	private static final long serialVersionUID = 2564825147116821092L;

	/**
	 * Constructs a {@code DuplicatedEntityException} with a default message.
	 */
	public DuplicatedEntityException() {
		super("Attempted to create an entity that already exists.");
	}

	/**
	 * Constructs a {@code DuplicatedEntityException} with the specified detail message.
	 *
	 * @param   s   the detail message.
	 * 
	 */
	public DuplicatedEntityException(String s) {
		super(s);
	}

}
