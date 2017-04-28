package dataAccess;

import java.util.Date;
import java.util.Vector;

import javax.security.auth.login.AccountNotFoundException;

import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.Booking;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

public interface DataAccessInterface {

	void initializeDB();
	
	<T> T update(T entity);

	Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price);
	
	Vector<Offer> getOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay);
	
	Vector<Offer> getOffersBetweenPrice(int min, int max);
	
	Vector<Offer> getOffers();
	
	Vector<Offer> getOffers(ReviewState reviewState);
	
	int getOfferCount();
	
	double getOffersHighestPrice();
	
	double getOffersLowestPrice();

	boolean existsOverlappingOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay) throws OverlappingOfferException;

	RuralHouse createRuralHouse(String description, City city) throws DuplicatedEntityException;
	
	RuralHouse createRuralHouse(Owner owner, String description, City city, String address) throws DuplicatedEntityException;

	Vector<RuralHouse> getRuralHouses();

	Vector<RuralHouse> getRuralHouses(ReviewState reviewState);
	
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

}
