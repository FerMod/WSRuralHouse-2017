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
	private Client c;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	private Offer o;

	private Date creationDate;
	
	public Booking(Client c, Offer o) {
		this.c = c;
		this.o = o;
		creationDate = Calendar.getInstance().getTime();
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}

	public Offer getO() {
		return o;
	}

	public void setO(Offer o) {
		this.o = o;
	}

	@Override
	public String toString() {
		return "Booking [c=" + c.toString() + ", o=" + o.toString() + "]";
	}

}
