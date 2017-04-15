package domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Owner extends AbstractUser {

	private static final long serialVersionUID = -3896660403851368689L;

	public Owner(String email, String username, String password) {
		super(email, username, password, Role.OWNER);
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
