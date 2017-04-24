package domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Review {

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private Admin reviewer;
	private Date creationDate;
	private Date reviewDate;
	private String description;
	@Enumerated
	private ReviewState reviewState;

	/**
	 * A review state. A review can be in one of the following states:
	 * <ul>
	 * <li>{@link #APPROVED}</br>
	 *     A review is made and the element is marked as valid.
	 *     </li>
	 * <li>{@link #AWAITING_REVIEW}</br>
	 *      A review have not been made yet, and the element is waiting for one.
	 *     </li>
	 * <li>{@link #REJECTED}</br>
	 *      A review is made and the element is marked as not valid.
	 *     </li>
	 * </ul>
	 * A review can be in only one state at time.
	 * 
	 * @see #getReviewState
	 * 
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

	public Review() {
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

	public ReviewState getState() {
		return reviewState;
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

	/**
	 * Set the reviewer and the state of the review.
	 * No description is given to the review, so the description value will be null.
	 * This also gets the current system date and stores it to later know when was made the review.
	 * 
	 * @param reviewer the administrator who have made the review
	 * @param state the state that is currently the review
	 */
	public void setReview(Admin reviewer, ReviewState state) {
		setReview(reviewer, null, state);
	}

	/**
	 * Set the reviewer, description and state of the review.
	 * This also gets the current system date and stores it to later know when was made the review.
	 * 
	 * @param reviewer the administrator who have made the review
	 * @param description a description for the review
	 * @param state the state that is currently the review
	 */
	public void setReview(Admin reviewer, String description, ReviewState reviewState) {
		this.reviewer = reviewer;
		this.description = description;
		this.reviewState = reviewState;
		reviewDate = Calendar.getInstance().getTime();
	}

	@Override
	public String toString() {
		return "Review [idRev=" + id + ", idAdmin=" + reviewer
				+ ", reviewState=" + reviewState + ", description="
				+ description + "]";
	}

}
