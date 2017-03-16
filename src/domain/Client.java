package domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Client extends AbstractUser {

	private static final long serialVersionUID = -1989696498234692075L;

	public Client(String email, String username, String password) {
		super(email, username, password, Role.CLIENT);
	}

	public int getIdClient() {
		return super.getId();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public Role getRole() {
		return Role.CLIENT;
	}

}
