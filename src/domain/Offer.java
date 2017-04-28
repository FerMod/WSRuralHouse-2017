package domain;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Offer implements Serializable {


	@Id
	@GeneratedValue
	private Integer id;
	private Date startDate; // Dates are stored as java.util.Date objects instead of java.sql.Date objects
	private Date endDate;  // because, they are not well stored in db4o as java.util.Date objects
	private double price;   // This is coherent because objects of java.sql.Date are objects of java.util.Date 

	@XmlIDREF
	private RuralHouse ruralHouse;
	private boolean booked = false;

	public Offer(){
	}

	public Offer(Date firstDay, Date lastDay, double price, RuralHouse ruralHouse){
		this.startDate=firstDay;
		this.endDate=lastDay;
		this.price=price;
		this.ruralHouse=ruralHouse;
	}

	/**
	 * Get the house number of the offer
	 * 
	 * @return the house number
	 */
	public RuralHouse getRuralHouse() {
		return this.ruralHouse;
	}

	/**
	 * Set the house number to a offer
	 * 
	 * @param house number
	 */
	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}

	/**
	 * Get the offer id
	 * 
	 * @return offer number
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get the starting date of the offer
	 * 
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Set the starting date of the offer
	 * 
	 * @param startDate the start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set the starting date of the offer
	 * 
	 * @param year The year when starts
	 * @param month The month when starts
	 * @param day The day when starts
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 */
	public void setStartDate(int year, int month, int day) throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		this.startDate = date.parse(year + "/" + month + "/" + day);
	}

	/**
	 * Get the ending date of the offer
	 * 
	 * @return the ending date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Set the ending date of the offer
	 * 
	 * @param endDate the ending date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Set the ending date of the offer
	 * 
	 * @param year The year when finishes
	 * @param month The month when finishes
	 * @param day The day when finishes
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 */
	public void setEndDate(int year, int month, int day) throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		this.endDate = date.parse(year + "/" + month + "/" + day);
	}

	/**
	 * Get the price
	 * 
	 * @return price
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * Set the price
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isBooked() {
		return booked;
	}

	public void setBooked(boolean booked) {
		this.booked = booked;
	}

	public String toString(){
		return id+";"+firstDay.toString()+";"+lastDay.toString()+";"+price+";"+booked;
	}
}
