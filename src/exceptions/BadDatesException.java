package exceptions;

/**
 * Thrown when the first date is greater than second date.
 */
public class BadDatesException extends Exception {

	private static final long serialVersionUID = -6594845514342716332L;

	/**
	 * Constructs a {@code BadDates} with no detail message.
	 */
	public BadDatesException() {
		super();
	}

	/**
	 * Constructs a {@code BadDates} with the specified detail message.
	 *
	 * @param   s   the detail message.
	 * 
	 */
	public BadDatesException(String s) {
		super(s);
	}

}