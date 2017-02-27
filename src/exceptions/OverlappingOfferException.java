package exceptions;

/**
 * Thrown when two offers overlaps because of the date.
 */
public class OverlappingOfferException extends Exception {

	private static final long serialVersionUID = -3406783201140469412L;

	/**
	 * Constructs a {@code OverlappingOfferException} with no detail message.
	 */
	public OverlappingOfferException() {
		super();
	}

	/**
	 * Constructs a {@code OverlappingOfferException} with the specified detail message.
	 *
	 * @param   s   the detail message.
	 * 
	 */
	public OverlappingOfferException(String s) {
		super(s);
	}

}