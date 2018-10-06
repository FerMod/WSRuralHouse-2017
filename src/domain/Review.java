package domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import domain.util.IntegerAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Review implements Serializable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 8273895976125369427L;

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private Admin reviewer;
	private Date creationDate;
	private Date reviewDate;
	private String description;
	@OneToOne
	@MapsId
	private RuralHouse ruralHouse;

	@Enumerated
	private ReviewState reviewState;

	/**
	 * A review state. A review can be in one of the following states:
	 * <ul>
	 *		<li>{@link #APPROVED}<br>
	 * 		A review is made and the element is marked as valid.
	 * 		</li>
	 * 		<li>{@link #AWAITING_REVIEW}<br>
	 *      A review have not been made yet, and the element is waiting for one.
	 *     	</li>
	 * 		<li>{@link #REJECTED}<br>
	 *      A review is made and the element is marked as not valid.
	 *     	</li>
	 * </ul>
	 *<p>
	 * A review can be in only one state at time. 
	 */
	public enum ReviewState {
		/**
		 * Element marked as valid
		 */
		APPROVED,
		/**
		 * The element does not have any revision
		 */
		AWAITING_REVIEW,
		/**
		 * Element marked as not valid
		 */
		REJECTED		
	}

	public Review(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
		reviewState = ReviewState.AWAITING_REVIEW;
		creationDate = Calendar.getInstance().getTime();
	}

	@Deprecated
	public Review(Admin admin, ReviewState reviewState, String description) {
		this.reviewer = admin;
		this.reviewState = reviewState;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Admin getReviewer() {
		return reviewer;
	}

	public void setReviewer(Admin reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * Get the current review state
	 * @return the review state
	 */
	public ReviewState getState() {
		return reviewState;
	}

	/**
	 * Set the reviewer and the state of the review.
	 * No description is given to the review, so the description value will be null.
	 * This also gets the current system date and stores it to later know when was made the review.
	 * 
	 * @param reviewer the administrator who have made the review
	 * @param state the state that is currently the review
	 */
	public void setState(Admin reviewer, ReviewState state) {
		setState(reviewer, null, state);
	}

	/**
	 * Set the reviewer, description and state of the review.
	 * This also gets the current system date and stores it to later know when was made the review.
	 * 
	 * @param reviewer the administrator who have made the review
	 * @param description a description for the review
	 * @param reviewState the state that is currently the review
	 */
	public void setState(Admin reviewer, String description, ReviewState reviewState) {
		this.reviewer = reviewer;
		this.description = description;
		this.reviewState = reviewState;
		reviewDate = Calendar.getInstance().getTime();
	}

	public void setState(ReviewState reviewState) {
		this.reviewState = reviewState;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	@Override
	public String toString() {
		return "Review [idRev=" + id + ", idAdmin=" + reviewer
				+ ", reviewState=" + reviewState + ", description="
				+ description + "]";
	}

}
