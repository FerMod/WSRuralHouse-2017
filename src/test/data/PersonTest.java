package test.data;

import java.io.Serializable;

public class PersonTest implements Serializable {

	private String name;
	private int age; 

	public PersonTest(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;	
	}

	public void setName(String name) {
		this.name = name;	
	}

	public int getAge() {
		return age;	
	}

	public void setAge(int age) {
		this.age = age;	
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!PersonTest.class.isAssignableFrom(obj.getClass())) {
			return false;
		}

		final PersonTest other = (PersonTest) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}

		if (this.age != other.age) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 53 * hash + this.age;
		return hash;
	}

	private static final long serialVersionUID = -8858049414160214232L;

}
