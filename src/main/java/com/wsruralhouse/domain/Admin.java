package com.wsruralhouse.domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Admin extends AbstractUser {
	
	private static final long serialVersionUID = 9196192939959320700L;

	public Admin(String email, String username, String password) {
		super(email, username, password, Role.ADMIN);
	}
	
	@Override
	public Role getRole() {
		return Role.ADMIN;
	}

	@Override
	public String toString() {
		return super.toString();
	}
	
}