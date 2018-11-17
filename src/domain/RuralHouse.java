package domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import domain.event.EventPublisher;
import domain.event.ValueAddedListener;
import domain.util.IntegerAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class RuralHouse extends EventPublisher<ValueAddedListener<Offer>> implements Serializable {

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
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	private Review review;
	/**
	 * The image in the first position (<i>index 0</i>) of the {@code Vector} will be used as the
	 * rural house image icon.
	 */
	@OneToOne(cascade=CascadeType.ALL)
	private List<byte[]> images;
	private String[] tags;

	//@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@OneToMany(mappedBy = "ruralHouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Map<Integer, Offer> offers;

	public RuralHouse() {
		this.review = new Review(this);
		this.offers =  Collections.synchronizedMap(new HashMap<Integer, Offer>());
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
		this();
		this.owner = owner;
		owner.getRuralHouses().add(this);
		this.name = name;
		this.description = description;
		this.city = city;
		this.address = address;
		this.images = new Vector<byte[]>();
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
	public List<byte[]> getImages() {
		return images;
	}

	/**
	 * Set the <code>Vector</code> of <code>ImageIcon</code>
	 * 
	 * @param images the <code>ImageIcon</code> <code>Vector</code>
	 */
	public void setImages(Vector<byte[]>  images) {
		this.images = images;
	}

	/**
	 * Returns the image at the specified position
	 * 
	 * @param index index of the image to return
	 * @return the <code>ImageIcon</code> at the specified index
	 */
	public ImageIcon getImage(int index) {
		if(images != null) {
			if(images.size() > 0) {
				try {
					return new ImageIcon(ImageIO.read(new ByteArrayInputStream(images.get(0))));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//Return a image 
		return NO_IMAGE_AVAILABLE;
	}

	/**
	 * Appends the specified image to the end of the image <code>Vector</code>.
	 * 
	 * @param imagePath path to the element to be appended to the <code>Vector</code>
	 * @return <code>true</code> if the image is added to the <code>Vector</code> 
	 * @throws IOException if an I/O error occurs reading from the stream
	 */
	public boolean addImage(String imagePath) throws IOException {
		Path path = Paths.get(imagePath);
		return images.add(Files.readAllBytes(path));
	}

	/**
	 * Appends the specified image to the end of the image <code>Vector</code>.
	 * 
	 * @param uri the URI of the element to be appended to the <code>Vector</code>
	 * @return <code>true</code> if the image is added to the <code>Vector</code> 
	 * @throws IOException if an I/O error occurs reading from the stream
	 */
	public boolean addImage(URI uri) throws IOException {
		Path path = Paths.get(uri);
		return images.add(Files.readAllBytes(path));
	}

	/**
	 * Removes the image at the specified position.
	 * 
	 * @param index the index of the image to be removed
	 * @return element that was removed
	 */
	public ImageIcon removeImage(int index) {
		ImageIcon imageIcon = null;
		try {
			imageIcon = new ImageIcon(ImageIO.read(new ByteArrayInputStream(images.remove(index)))); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageIcon;
	}

	/**
	 * Removes the first occurrence of the specified image.
	 * If the does not contain the image, it is unchanged.
	 * 
	 * @param image element to be removed from this Vector, if present
	 * @return true if it contained the specified element
	 */
	public boolean removeImage(byte[] image) {
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
	 * Adds an offer to the rural house
	 * 
	 * @param offer the offer to add
	 */
	public void addOffer(Offer offer) {
		offers.put(offer.getId(), offer);
		// Notify the list of registered listeners
		this.notifyListeners((listener) -> listener.onValueAdded(Optional.ofNullable(offer)));
	}

	/**
	 * Get an offer with the given id
	 * 
	 * @param id the id of the offer
	 * @return the offer 
	 */
	public Offer getOffer(int id) {
		return offers.get(id);
	}


	/**
	 * This method obtains available offers for a concrete house in a certain period 
	 * 
	 * @param firstDay first day in a period range 
	 * @param lastDay last day in a period range
	 * @return a vector of offers(Offer class)  available  in this period
	 */
	public List<Offer> getOffers(Date firstDay,  Date lastDay) {
		List<Offer> availableOffers = offers.entrySet()
				.stream()
				.map(entry -> entry.getValue())
				.filter((offer) -> offer.getStartDate().compareTo(firstDay) >= 0 && offer.getEndDate().compareTo(lastDay) <= 0)
				.collect(Collectors.toList());
		return availableOffers;
	}

	/**
	 * This method obtains all the available offers 
	 * 
	 * @return a vector of {@code Offers}
	 */
	public Collection<Offer> getOffers() {
		System.out.println(offers);
		return offers.values();
	}

	/**
	 * This method obtains the first offer that overlaps with the provided dates
	 * 
	 * @param firstDay first day in a period range 
	 * @param lastDay last day in a period range
	 * @return the first offer that overlaps with those dates, or null if there is no overlapping offer
	 */
	public Optional<Offer> overlapsWith(Date firstDay,  Date lastDay) {
		Optional<Offer> optOffer = offers.entrySet()
				.stream()
				.map(entry -> entry.getValue())
				.filter((offer) -> offer.getEndDate().after(firstDay) && offer.getStartDate().before(lastDay))
				.findFirst();
		return optOffer;
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
