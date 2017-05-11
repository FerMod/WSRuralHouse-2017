package businessLogic;

import java.util.Date;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import dataAccess.DataAccessInterface;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

@WebService
public interface ApplicationFacadeInterface  {

	/**
	 * 
	 * @param dataAccess
	 */
	void setDataAccess(DataAccessInterface dataAccess);

	/**
	 * Method used to update a entity with their changes to the database
	 * 
	 * @param entity the entity that will be updated
	 * @return the managed instance that is updated
	 */
	<T> T update(T entity);

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
	 * Obtain all the offers stored in the database
	 *
	 * @return a {@code Vector} with objects of type {@code Offer} containing all the offers in the database, {@code null} if none is found
	 */
	@WebMethod
	Vector<Offer> getOffers();

	/**
	 * Obtain all the offers stored in the database that matches with the given {@code ReviewState} of their rural house
	 *
	 * @return a {@code Vector} with objects of type {@code Offer} containing all the offers in the database matching with the given {@code ReviewState} of their rural house, {@code null} if none is found
	 */
	Vector<Offer> getOffers(ReviewState reviewState);

	/**
	 * Returns the number of offers stored in the database
	 *
	 * @return the number of offers in the database
	 */
	@WebMethod
	int getOfferCount();

	/**
	 * Returns the highest price of the stored offers
	 *  
	 * @return highest price of the stored Offers
	 */
	@WebMethod
	double getOffersHighestPrice();

	/**
	 * Creates a new rural house and stores it in the database.
	 * 
	 * @param owner the owner of the rural house
	 * @param name the name of the rural house
	 * @param description the description of the rural house
	 * @param city the city which the rural house is located
	 * @param address the address where the house is located
	 * @return the created rural house, null if none was created
	 * @throws DuplicatedEntityException If is attempted to create an existing entity
	 */
	@WebMethod
	RuralHouse createRuralHouse(Owner owner, String name, String description, City city, String address) throws DuplicatedEntityException;

	/**
	 * This method retrieves the existing rural houses 
	 * 
	 * @return a {@code Vector} of rural houses
	 */
	@WebMethod
	Vector<RuralHouse> getRuralHouses();

	/**
	 * Obtain all the rural houses matching with the entered {@code ReviewState}
	 *
	 * @param reviewState one of the possible states of a {@code Review}
	 * @return a {@code Vector} with objects of type {@code RuralHouse} containing all the rural houses matching with the {@code ReviewState}, {@code null} if none is found
	 * 
	 * @see ReviewState
	 */
	@WebMethod
	Vector<RuralHouse> getRuralHouses(ReviewState reviewState);

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
	 * Create a review for a rural house.
	 * 
	 * @param rh the rural house
	 * @return review created of the rural house
	 */
	@WebMethod
	Review createReview(RuralHouse rh);
	
	/**
	 * Update a review of a rural house.
	 * 
	 * @param rh the rural house
	 * @param r the review
	 */
	@WebMethod
	void updateReview(RuralHouse rh, Review r);
	
}
