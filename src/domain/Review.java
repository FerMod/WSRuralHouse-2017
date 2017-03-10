package domain;

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
	private Integer idRev;
	
	@Id
	private Integer idAdmin;
	
	public enum ReviewState {
		ACEPTED(1),
		IN_PROCESS(0),
		REJECTED(-1);

		private int state;

		private ReviewState(int state) {
			this.state = state;
		}

		public int getValue() {
			return this.state;
		}
		
	}
	
	@Enumerated
	private ReviewState reviewState;
	private String description;
	
	
	public Review(Integer idRev, Integer idAdmin, ReviewState reviewState, String description) {
		this.idRev = idRev;
		this.idAdmin = idAdmin;
		this.reviewState = reviewState;
		this.description = description;
	}


	public Integer getIdRev() {
		return idRev;
	}


	public void setIdRev(Integer idRev) {
		this.idRev = idRev;
	}


	public Integer getIdAdmin() {
		return idAdmin;
	}


	public void setIdAdmin(Integer idAdmin) {
		this.idAdmin = idAdmin;
	}


	public ReviewState getReviewState() {
		return reviewState;
	}


	public void setReviewState(ReviewState reviewState) {
		this.reviewState = reviewState;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "Review [idRev=" + idRev + ", idAdmin=" + idAdmin
				+ ", reviewState=" + reviewState + ", description="
				+ description + "]";
	}

}
