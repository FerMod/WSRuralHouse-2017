package domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import domain.util.IntegerAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Booking implements Serializable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -8672637781397201363L;

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	private Client client;
	@OneToOne
	private Offer offer;
	private Date bookingDate;

	private Date startDate;
	private Date endDate;
	private double price;
	
	public Booking() {
	}

	public Booking(Client client, Offer offer, double price, Date startDate, Date endDate) {
		this.client = client;
		this.offer = offer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		bookingDate = Calendar.getInstance().getTime();
	}
	
	public Integer getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "Booking [client=" + client + ", offer=" + offer + ", creationDate=" + bookingDate + "]";
	}

}
