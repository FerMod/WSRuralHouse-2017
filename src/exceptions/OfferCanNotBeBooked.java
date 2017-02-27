package exceptions;
public class OfferCanNotBeBooked extends Exception {
 private static final long serialVersionUID = 1L;
 
 public OfferCanNotBeBooked()
  {
    super();
  }
  /**This exception is triggered if an offer can not be booked 
  *@param String
  *@return None
  */
  public OfferCanNotBeBooked(String s)
  {
    super(s);
  }
}