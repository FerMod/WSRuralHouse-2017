package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javax.persistence.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class RuralHouse implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private String description;
	private int city; 
	private int ownerId;
	private int reviewId;
	private String[] images;
	private String[] tags;                

	public Vector<Offer> offers;

	public RuralHouse() {
	}

	public RuralHouse(String description, int city) {
		this.description = description;
		this.city = city;
		offers = new Vector<Offer>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer houseNumber) {
		this.id = houseNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description=description;
	}


	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}


	/**
	 * This method creates an offer with a house number, first day, last day and price
	 * 
	 * @param House
	 *            number, start day, last day and price
	 * @return None
	 */
	public Offer createOffer(Date firstDay, Date lastDay, double price)  {
		System.out.println("LLAMADA RuralHouse createOffer, offerNumber="+" firstDay="+firstDay+" lastDay="+lastDay+" price="+price);
		Offer off = new Offer(firstDay, lastDay, price, this);
		offers.add(off);
		return off;
	}


	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return this.id + ": " + this.city;
	}

	@Override
	public boolean equals(Object obj) {
		RuralHouse other = (RuralHouse) obj;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		//		if (houseNumber != other.houseNumber) // NO COMPARAR ASÍ ya que houseNumber NO ES "int" sino objeto de "java.lang.Integer"
		if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * This method obtains available offers for a concrete house in a certain period 
	 * 
	 * @param houseNumber, the house number where the offers must be obtained 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return a vector of offers(Offer class)  available  in this period
	 */
	public Vector<Offer> getOffers( Date firstDay,  Date lastDay) {

		Vector<Offer> availableOffers=new Vector<Offer>();
		Iterator<Offer> e=offers.iterator();
		Offer offer;
		while (e.hasNext()){
			offer=e.next();
			if ( (offer.getFirstDay().compareTo(firstDay)>=0) && (offer.getLastDay().compareTo(lastDay)<=0)  )
				availableOffers.add(offer);
		}
		return availableOffers;

	}


	/**
	 * This method obtains the first offer that overlaps with the provided dates
	 * 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return the first offer that overlaps with those dates, or null if there is no overlapping offer
	 */

	public Offer overlapsWith( Date firstDay,  Date lastDay) {

		Iterator<Offer> e=offers.iterator();
		Offer offer=null;
		while (e.hasNext()){
			offer=e.next();
			if ( (offer.getFirstDay().compareTo(lastDay)<0) && (offer.getLastDay().compareTo(firstDay)>0))
				return offer;
		}
		return null;

	}

}
