package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import domain.util.IntegerAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Inheritance
@Entity(name = "User")
//@Table(name = "User")
public abstract class AbstractUser implements UserInterface, Serializable {

	private static final long serialVersionUID = -8104656861921494420L;

	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Id
	private String email;
	@Id
	private String username;
	private String password;
	@Enumerated
	private UserType userType;

	/**
	 * Protected to only make it visible for the child class
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @param userType
	 */
	protected AbstractUser(String email, String username, String password, UserType userType) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.userType = userType;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password + ", userType=" + userType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractUser other = (AbstractUser) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
