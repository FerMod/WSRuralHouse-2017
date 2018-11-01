package domain;

import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Owner extends AbstractUser {

	private static final long serialVersionUID = -3896660403851368689L;
	
	private List<RuralHouse> ruralHouses;

	public Owner(String email, String username, String password) {
		super(email, username, password, UserType.OWNER);
		ruralHouses = new Vector<RuralHouse>();
	}

	@Override
	public UserType getRole() {
		return UserType.OWNER;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public List<RuralHouse> getRuralHouses() {
		return ruralHouses;
	}

	public void setRuralHouses(List<RuralHouse> ruralHouses) {
		this.ruralHouses = ruralHouses;
	}
	
}
