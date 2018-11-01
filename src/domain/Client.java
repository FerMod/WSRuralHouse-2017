package domain;

import java.util.Vector;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Client extends AbstractUser {

	private static final long serialVersionUID = -1989696498234692075L;
	
	private Vector<Booking> bookings;

	public Client(String email, String username, String password) {
		super(email, username, password, UserType.CLIENT);
		bookings = new Vector<Booking>();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public UserType getRole() {
		return UserType.CLIENT;
	}

	public Vector<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Vector<Booking> bookings) {
		this.bookings = bookings;
	}

}
