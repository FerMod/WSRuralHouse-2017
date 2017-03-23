package domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javax.persistence.*;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id
	@GeneratedValue
	private Integer id;
	private String name;

	public City(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer houseNumber) {
		this.id = houseNumber;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
