package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class RuralHouse implements Serializable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -7593429026088916515L;

	/**
	 * When no image is available this one will be used 
	 */
	private static final ImageIcon NO_IMAGE_AVAILABLE = new ImageIcon(RuralHouse.class.getResource("/img/no_image_available.png"));

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private City city; 
	private String address;
	private Owner owner;
	//@OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval=true, optional=false)
	@OneToOne(cascade=CascadeType.ALL)
	private Review review;
	/**
	 * The image in the first position (<i>index 0</i>) of the {@code Vector} will be used as the
	 * rural house image icon.
	 */
	private Vector<ImageIcon> images;
	private String[] tags;                

	public Vector<Offer> offers;

	public RuralHouse() {
		this.review = new Review(this);
	}

	/**
	 * Constructor of {@code RuralHouse}.
	 * 
	 * @param owner the owner of the rural house
	 * @param description a short text that describes the rural house 
	 * @param city the city where the rural house is located
	 * @param address the address where the rural house is
	 * @deprecated No longer the rural house name is optional, so this constructor will be removed in a near future
	 */
	@Deprecated
	public RuralHouse(Owner owner, String description, City city, String address) {
		this(owner, null, description, city, address);
	}

	/**
	 * Constructor of {@code RuralHouse}.
	 * <p>
	 * The {@code ReviewState} of the rural house will be {@code AWAITING} as default until a review is made by an administrator
	 * 
	 * @param owner the owner of the rural house
	 * @param name the name of that is given to the rural house
	 * @param description a short text that describes the rural house 
	 * @param city the city where the rural house is located
	 * @param address the address where the rural house is
	 * 
	 * @see Review.ReviewState
	 */
	public RuralHouse(Owner owner, String name, String description, City city, String address) {
		this.owner = owner;
		this.name = name;
		this.description = description;
		this.city = city;
		this.address = address;
		this.review = new Review(this);
		offers = new Vector<Offer>();
		images = new Vector<ImageIcon>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer houseNumber) {
		this.id = houseNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Get the <code>Vector</code> containing objects of type <code>ImageIcon</code> 
	 * 
	 * @return the image vector
	 */
	public Vector<ImageIcon> getImages() {
		return images;
	}

	/**
	 * Set the <code>Vector</code> of <code>ImageIcon</code>
	 * 
	 * @param images the <code>ImageIcon</code> <code>Vector</code>
	 */
	public void setImages(Vector<ImageIcon>  images) {
		this.images = images;
	}

	/**
	 * Returns the image at the specified position
	 * 
	 * @param index index of the image to return
	 * @return the <code>ImageIcon</code> at the specified index
	 */
	public ImageIcon getImage(int index) {
		if(images.size() > 0) {
			return images.get(0);
		} else {
			//Return a image 
			return NO_IMAGE_AVAILABLE;
		}
	}

	/**
	 * Appends the specified image to the end of the image <code>Vector</code>.
	 * 
	 * @param image element to be appended to the <code>Vector</code>
	 * @return <code>true</code> if the image is added to the <code>Vector</code> 
	 */
	public boolean addImage(ImageIcon image) {
		return images.add(image);
	}

	/**
	 * Removes the image at the specified position.
	 * 
	 * @param index the index of the image to be removed
	 * @return element that was removed
	 */
	public ImageIcon removeImage(int index) {
		return images.remove(index);
	}

	/**
	 * Removes the first occurrence of the specified image.
	 * If the does not contain the image, it is unchanged.
	 * 
	 * @param image element to be removed from this Vector, if present
	 * @return true if it contained the specified element
	 */
	public boolean removeImage(ImageIcon image) {
		return images.remove(image);
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
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
		Offer offer = new Offer(firstDay, lastDay, price, this);
		offers.add(offer);
		return offer;
	}


	/**
	 * This method obtains available offers for a concrete house in a certain period 
	 * 
	 * @param houseNumber, the house number where the offers must be obtained 
	 * @param firstDay, first day in a period range 
	 * @param lastDay, last day in a period range
	 * @return a vector of offers(Offer class)  available  in this period
	 */
	public Vector<Offer> getOffers(Date firstDay,  Date lastDay) {

		Vector<Offer> availableOffers=new Vector<Offer>();
		Iterator<Offer> e = offers.iterator();
		Offer offer;
		while (e.hasNext()){
			offer=e.next();
			if ( (offer.getStartDate().compareTo(firstDay)>=0) && (offer.getEndDate().compareTo(lastDay)<=0)  )
				availableOffers.add(offer);
		}
		return availableOffers;
	}

	/**
	 * This method obtains all the available offers 
	 * 
	 * @return a vector of {@code Offers}
	 */
	public Vector<Offer> getOffers() {
		System.out.println(offers);
		if(offers != null) {
			if(!offers.isEmpty()) {
				return offers;			
			}
		}
		return new Vector<>();
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
			if ( (offer.getStartDate().compareTo(lastDay)<0) && (offer.getEndDate().compareTo(firstDay)>0))
				return offer;
		}
		return null;
	}

	public String toDetailedString() {
		return "RuralHouse [id=" + id + ", name=" + name + ", description=" + description + ", city=" + city
				+ ", address=" + address + ", owner=" + owner + ", review=" + review + ", images=" + images + ", tags="
				+ Arrays.toString(tags) + ", offers=" + offers + "]";
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

}
