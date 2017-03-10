
package domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
import javax.persistence.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class User implements Serializable {
	
	private static final long serialVersionUID = -8104656861921494420L;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	@Id
	private String email;
	@Id
	private String username;
	private String password;
	@Enumerated
	private Role role;
	
	/**
	 * Role of the user account
	 */
	public enum Role {
		GUEST(0),
		CLIENT(1),
		OWNER(2),
		ADMIN(3),
		SUPER_ADMIN(4);

		private final int role;

		private Role(int role) {
			this.role = role;
		}

		public int getValue() {
			return this.role;
		}
		
	}
	
	public User(String email, String username, String password, Role role) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}
	
	public String getUser() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDescription(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return this.id + ": " + this.username + " : " + this.password;
	}

	@Override
	public boolean equals(Object obj) {
		User other = (User) obj;
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (!id.equals(other.id))
			return false;
   	    return true;
	}
	
}
