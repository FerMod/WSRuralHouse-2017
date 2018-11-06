package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import domain.event.ValueChangeListener;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class TravelAgency extends AbstractUser {

	private static final long serialVersionUID = -1989696498234692075L;
	
	private List<Booking> bookings;
	private ValueChangeListener eventListener;
	
	public TravelAgency(String email, String username, String password) {
		super(email, username, password, UserType.PARTICULAR_CLIENT);
		bookings = new ArrayList<Booking>();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public UserType getRole() {
		return UserType.CLIENT;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public void enableOfferAlert(RuralHouse ruralHouse) {		
		ruralHouse.getOfferObserver().registerListener(eventListener = event -> {
			System.out.println("Nueva oferta! " + event.getNewValue());
		});
	}

	public void disableOfferAlert(RuralHouse ruralHouse) {
		ruralHouse.getOfferObserver().unregisterListener(eventListener);
	}

}
