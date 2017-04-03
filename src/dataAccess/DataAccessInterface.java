package dataAccess;

import java.util.Date;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.City;
import domain.Offer;
import domain.RuralHouse;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

public interface DataAccessInterface {

	void initializeDB();

	Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price);

	RuralHouse createRuralHouse(String description, int city, double price) throws DuplicatedEntityException;

	AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException;

	boolean validDni(String dni);

	Role getRole(String username);

	boolean existsUser(String username);

	boolean existsEmail(String email);

	AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException;

	List<RuralHouse> getRuralHouses();

	List<Offer> getOffers(RuralHouse ruralHouse, Date firstDay, Date lastDay);

	boolean existsOverlappingOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay) throws OverlappingOfferException;

	boolean existsRuralHouse(String description, int city);
	
	City createCity(String name);
	
	boolean existsCity(City city);

	boolean existsCity(int id);
	
	boolean existsCity(String name);

	List<City> getCities();
	
	void deleteTableContent(String table);
	
	AbstractUser getUser(String username, String password);

}
