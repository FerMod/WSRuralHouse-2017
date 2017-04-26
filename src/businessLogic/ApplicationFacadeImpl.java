package businessLogic;

import java.util.Date;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import dataAccess.DataAccessInterface;
import domain.Offer;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.Review.ReviewState;
import domain.City;
import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;
import exceptions.DuplicatedEntityException.Error;

//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterface")
public final class ApplicationFacadeImpl  implements ApplicationFacadeInterface {

	private DataAccessInterface dataAccess;

	public void setDataAccess(DataAccessInterface dataAccess) {
		this.dataAccess = dataAccess;
	}

	@Override
	public <T> T update(T entity) {
		return dataAccess.update(entity);
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) throws OverlappingOfferException, BadDatesException {
		System.out.println(">> ApplicationFacadeImpl: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);

		Offer offer = null;

		if (firstDay.compareTo(lastDay) >= 0) {
			throw new BadDatesException();
		}

		if (!dataAccess.existsOverlappingOffer(ruralHouse,firstDay,lastDay)) {
			offer = dataAccess.createOffer(ruralHouse,firstDay,lastDay,price);		
		}

		System.out.println("<< ApplicationFacadeImpl: createOffer=> O= " + offer);
		return offer;
	}

	@WebMethod
	@Override
	public Vector<Offer> getOffer(RuralHouse ruralHouse, Date firstDay,  Date lastDay) {
		return new Vector<Offer>(dataAccess.getOffer(ruralHouse, firstDay, lastDay));
	}

	@Override
	public Vector<Offer> getOffers() {
		return dataAccess.getOffers();
	}
	
	@Override
	public int getOfferCount() {
		return dataAccess.getOfferCount();
	}

	@Override
	public double getOffersHighestPrice() {
		return dataAccess.getOffersHighestPrice();
	}

	@Override
	public RuralHouse createRuralHouse(String description, City city) throws DuplicatedEntityException{
		System.out.println(">> ApplicationFacadeImpl: createRuralHouse=> description= " + description + " city= " + city);

		RuralHouse ruralHouse = null;

		if(!dataAccess.existsRuralHouse(description, city.getId())) {
			ruralHouse = dataAccess.createRuralHouse(description, city);
		} else {
			throw new DuplicatedEntityException();
		}

		System.out.println("<< ApplicationFacadeImpl: createRuralHouse => " + ruralHouse);
		return ruralHouse;
	}

	public Vector<RuralHouse> getRuralHouses()  {
		System.out.println(">> ApplicationFacadeImpl: getAllRuralHouses");
		return new Vector<RuralHouse>(dataAccess.getRuralHouses());
	}

	@Override
	public Vector<RuralHouse> getRuralHouses(ReviewState reviewState) {
		return dataAccess.getRuralHouses(reviewState);
	}

	@Override
	public City createCity(String name) {
		System.out.println(">> ApplicationFacadeImpl: createCity=> name= " + name);

		City city = null;

		if (!dataAccess.existsCity(name)) {
			city = dataAccess.createCity(name);		
		}

		System.out.println("<< ApplicationFacadeImpl: createCity=> City [id=" + city.getId() + ", " + city.getName() + "]");
		return city;
	}

	@Override
	public Vector<String> getCitiesNames() {
		System.out.println(">> ApplicationFacadeImpl: getCitiesNames()");
		//Get the city name list from the city object list, and parse it to vector
		Vector<String> namesVector = new Vector<String>(dataAccess.getCities().stream()
				.map(City::getName)
				.collect(Collectors.toList()));
		return namesVector;
	}

	@Override
	public Vector<City> getCities() {
		System.out.println(">> ApplicationFacadeImpl: getCities()");
		return new Vector<City>(dataAccess.getCities());
	}

	public AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException {
		System.out.println(">> ApplicationFacadeImpl: createUser=> email=" + email + "username= " + username + " password= " + password + " role=" + role);
		if(!dataAccess.existsEmail(email)) {
			if(!dataAccess.existsUser(username)) {
				return dataAccess.createUser(email, username, password, role);
			} else {
				throw new DuplicatedEntityException(Error.DUPLICATED_USERNAME);
			}
		} else {
			throw new DuplicatedEntityException(Error.DUPLICATED_EMAIL);
		}
	}

	public Role getRole(String username) {
		Role role = dataAccess.getRole(username);
		return role;
	}

	public AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException {
		return dataAccess.login(username, password);
	}

	//	private getConfig() {
	//		return dataAccess.ge
	//	}

}

