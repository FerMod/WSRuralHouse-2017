package domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import gui.ConsoleWindow;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
//@DiscriminatorValue("CIENT")
public class Client extends AbstractUser {

	public static void main(String[] args) {
		new ConsoleWindow();
		Client c = new Client("email", "user", "pass");
		System.out.println(c.toString());
	}

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
