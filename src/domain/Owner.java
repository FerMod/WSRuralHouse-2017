package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Owner extends AbstractUser {

	private static final long serialVersionUID = -3896660403851368689L;

	public Owner(String email, String username, String password) {
		super(email, username, password, Role.OWNER);
	}

	public int getIdOwner() {
		return super.getId();
	}

	@Override
	public Role getRole() {
		return Role.OWNER;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
