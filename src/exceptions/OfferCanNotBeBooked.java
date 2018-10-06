package exceptions;

/**
 * This exception is throwed if an offer can not be booked 
 *
 */
public class OfferCanNotBeBooked extends Exception {
	
	private static final long serialVersionUID = 1L;

	public OfferCanNotBeBooked() {
		super();
	}
	
	public OfferCanNotBeBooked(String s) {
		super(s);
	}
	
}