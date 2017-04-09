package businessLogic;

import java.util.Date;
import java.util.Vector;

import domain.Offer;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.City;
import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;
import dataAccess.DataAccessInterface;

@WebService
public interface ApplicationFacadeInterface  {
	
	/**
	 * 
	 * @param dataAccess
	 */
	void setDataAccess(DataAccessInterface dataAccess);

	/**
	 * Creates a new rural house and stores it in the database.
	 * 
	 * @param description the description of the rural house
	 * @param city the id of the city which the rural house is located
	 * @return the created rural house, null if none was created
	 * @throws DuplicatedEntityException If is attempted to create an existing entity
	 */
	RuralHouse createRuralHouse(String description, int city) throws DuplicatedEntityException;
	
	/**
	 * Creates a new rural house and stores it in the database.
	 * 
	 * @param description the description of the rural house
	 * @param city the city which the rural house is located
	 * @return the created rural house, null if none was created
	 * @throws DuplicatedEntityException If is attempted to create an existing entity
	 */
	RuralHouse createRuralHouse(String description, City city) throws DuplicatedEntityException;

	/**
	 * Creates an offer and stores it in the database.
	 * 
	 * @param ruralHouse the rural house that the offer is going to apply
	 * @param firstDay the start date of the offer
	 * @param lastDay the ending date of the offer
	 * @param price the price of the offer
	 * @return the created offer, null if none was created
	 * @throws OverlappingOfferException If the entered date interval overlaps with already existing offer date
	 * @throws BadDatesException If the first date is greater than second date
	 */
	@WebMethod
	Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) throws OverlappingOfferException, BadDatesException;

	/**
	 * Creates an user and stores it in the database.
	 * 
	 * @param username the name of the account
	 * @param password the password of the account
	 * @param role the role assigned to the account
	 * @return the created user, null if none was created
	 * @throws DuplicatedEntityException If is attempted to create an existing entity
	 */
	@WebMethod
	AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException;

	/**
	 * Get the account role.
	 * 
	 * @param username the name of the account
	 * @return the role assigned to the account
	 */
	@WebMethod
	Role getRole(String username);

	/**
	 * This method retrieves the existing rural houses 
	 * 
	 * @return a {@code Vector} of rural houses
	 */
	@WebMethod
	Vector<RuralHouse> getRuralHouses();

	/**
	 * This method obtains the offers of a ruralHouse in the provided date interval
	 * 
	 * @param ruralHouse the rural house that the offer is applied to
	 * @param firstDay the start date of the offer
	 * @param lastDay the ending date of the offer
	 * @return a {@code Vector} of offers that are contained in those date range, or {@code null} if there is no offers
	 */
	@WebMethod
	Vector<Offer> getOffer(RuralHouse ruralHouse, Date firstDay,  Date lastDay);

	/**
	 * Login the user with the account that matches the entered user name and password
	 * 
	 * @param username the user name of the account
	 * @param password the password of the account
	 * @throws AuthException If the authentication is failed
	 * @throws AccountNotFoundException If no such account is found
	 * 
	 * @return the object inherited from {@code AbstractUser} that represents the user which have successfully logged in
	 * @see {@link AbstractUser}
	 */
	@WebMethod
	AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException;

	/**
	 * Creates a city and stores it in the database.
	 * @param name the name of the city
	 * @return the created city
	 * @see {@link City}
	 */
	City createCity(String name);
	
	/**
	 * Return a vector of all the cities names stored in the database
	 * 
	 * @return a vector with all the cities names of type {@code String}
	 * @see {@link City}
	 */
	Vector<String> getCitiesNames();
	
	/**
	 * Return a vector of all the cities stored in the database
	 * 
	 * @return a vector with all the cities of type {@code City}
	 * @see {@link City}
	 */
	Vector<City> getCities();	

}
