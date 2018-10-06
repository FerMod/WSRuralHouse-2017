package dataAccess;

import java.util.Date;
import java.util.Vector;

import javax.security.auth.login.AccountNotFoundException;

import configuration.ConfigXML;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

public interface DataAccessInterface {

	ConfigXML getConfig();

	void initializeDB();
	
	<T> T update(T entity);
	
	<T> T remove(T entity);

	<T, K> T remove(Class<T> entityClass, K primaryKey);
	
	<T, K> T find(Class<T> entityClass, K primaryKey);

	Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price);
	
	Vector<Offer> getOffers(RuralHouse ruralHouse, Date firstDay, Date lastDay);
	
	Vector<Offer> getOffersBetweenPrice(int min, int max);
	
	Vector<Offer> getOffers();
	
	Vector<Offer> getOffers(ReviewState reviewState);
	
	Vector<Offer> getActiveOffers();
	
	Vector<Offer> getActiveOffers(ReviewState reviewState);
	
	int getOfferCount();
	
	double getOffersHighestPrice();
	
	double getOffersLowestPrice();

	boolean existsOverlappingOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay) throws OverlappingOfferException;

	boolean datesRangeOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2);
	
	RuralHouse createRuralHouse(Owner owner, String name, String description, City city, String address) throws DuplicatedEntityException;

	Vector<RuralHouse> getRuralHouses();

	Vector<RuralHouse> getRuralHouses(Owner owner);

	Vector<RuralHouse> getRuralHouses(ReviewState reviewState);
	
	Vector<RuralHouse> getRuralHouses(Owner owner, ReviewState reviewState);
	
	boolean existsRuralHouse(String description, int city);

	AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException;

	AbstractUser getUser(String username, String password);

	boolean validDni(String dni);

	Role getRole(String username);

	boolean existsUser(String username);

	boolean existsEmail(String email);

	AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException;
	
	City createCity(String name);
	
	boolean existsCity(City city);
	
	boolean existsCity(int id);
	
	boolean existsCity(String name);

	Vector<City> getCities();
	
	void deleteTableContent(String table);
	
	Review createReview(RuralHouse rh);
	
	void updateReview(RuralHouse rh, Review r);

	Booking createBooking(Client client, Offer offer, Date startDate, Date endDate);

	Vector<Booking> getBookings(Client client);

	Vector<Booking> getBookings();
	
}
