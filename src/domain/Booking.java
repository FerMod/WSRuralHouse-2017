package domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Booking {
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	private Client client;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	private Offer offer;

	private Date creationDate;
	
	public Booking(Client c, Offer o) {
		this.client = c;
		this.offer = o;
		creationDate = Calendar.getInstance().getTime();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client c) {
		this.client = c;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer o) {
		this.offer = o;
	}

	@Override
	public String toString() {
		return "Booking [c=" + client.toString() + ", o=" + offer.toString() + "]";
	}

}
