package exceptions;
public class DB4oManagerCreationException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public DB4oManagerCreationException()
  {
    super();
  }
  /**This exception is triggered if there is a problem when constructor of DB4oManager is executed
  *@param String
  *@return None
  */
  public DB4oManagerCreationException(String s)
  {
    super(s);
  }
}