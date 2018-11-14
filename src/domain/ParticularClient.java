package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import domain.event.ValueAddedListener;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ParticularClient extends AbstractUser {

	private static final long serialVersionUID = -1989696498234692075L;

	private List<Booking> bookings;
	private ValueAddedListener eventListener;

	public ParticularClient(String email, String username, String password) {
		super(email, username, password, UserType.PARTICULAR_CLIENT);
		bookings = new ArrayList<Booking>();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public UserType getRole() {
		return UserType.PARTICULAR_CLIENT;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public void enableOfferAlert(RuralHouse ruralHouse) {
		ruralHouse.registerListener(eventListener = (optValue) -> {
			if(optValue.isPresent()) {
				offerAlert((Offer) optValue.get());
			}
		});
	}

	public void offerAlert(Offer offer) {
		System.out.println("New offer added! " + offer);
	}

	public void disableOfferAlert(RuralHouse ruralHouse) {
		ruralHouse.unregisterListener(eventListener);
	}

}