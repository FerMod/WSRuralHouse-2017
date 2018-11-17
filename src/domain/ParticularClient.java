package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.jdo.annotations.NotPersistent;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import domain.event.ValueAddedListener;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ParticularClient extends AbstractUser {

	private List<Booking> bookings;
	@NotPersistent
	transient private Map<RuralHouse, ValueAddedListener> eventListenersMap;

	public ParticularClient(String email, String username, String password) {
		super(email, username, password, UserType.PARTICULAR_CLIENT);
		bookings = new ArrayList<Booking>();
		eventListenersMap = new HashMap<>();
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
		enableOfferAlert(ruralHouse, this::alert);
	}
	
	public void enableOfferAlert(RuralHouse ruralHouse, Consumer<Optional<Offer>> consumer) {	
		@SuppressWarnings("unchecked")
		ValueAddedListener listener = ruralHouse.registerListener((optValue) -> consumer.accept((Optional<Offer>) optValue));
		eventListenersMap.put(ruralHouse, listener);
	}

	private void alert(Optional<Offer> offer) {
		System.out.println("(default) New offer added! " + offer);
	}

	public void disableOfferAlert(RuralHouse ruralHouse) {		
		ruralHouse.unregisterListener(eventListenersMap.get(ruralHouse));
	}
	
	public void disableAllAlerts() {
		eventListenersMap.keySet().forEach(rh -> rh.unregisterListener(eventListenersMap.get(rh)));
	}

	private static final long serialVersionUID = -1989696498234692075L;

}