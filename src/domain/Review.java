package domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
	
	private ReviewState reviewState;

}
