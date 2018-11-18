package businessLogic;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import dataAccess.DataAccessInterface;
import domain.Booking;
import domain.Offer;
import domain.Owner;
import domain.Review;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.Review.ReviewState;
import domain.util.ExtendedIterator;
import domain.util.RuralHouseIterator;
import domain.UserType;
import domain.City;
import domain.Client;
import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;
import exceptions.DuplicatedEntityException.Error;

//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterface")
public final class ApplicationFacadeImpl  implements ApplicationFacadeInterface {

	private DataAccessInterface dataAccess;
	private Locale locale;

	public ApplicationFacadeImpl() {
	}
	
	public ApplicationFacadeImpl(DataAccessInterface dataAccess) {
		setDataAccess(dataAccess);
	}
	
	public void setDataAccess(DataAccessInterface dataAccess) {
		this.dataAccess = dataAccess;
	}

	@Override
	public <T> T update(T entity) {
		return dataAccess.update(entity);
	}

	@Override
	public <T> T remove(T entity) {
		return dataAccess.remove(entity);
	}
	
	@Override
	public <T, K> T remove(Class<T> entityClass, K primaryKey) {
		return dataAccess.remove(entityClass, primaryKey);
	}
	
	@Override
	public <T, K> T find(Class<T> entityClass, K primaryKey) {
		return dataAccess.find(entityClass, primaryKey);
	}
	
	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) throws OverlappingOfferException, BadDatesException {
		System.out.println(">> ApplicationFacadeImpl: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);

		Offer offer = null;

		if (firstDay.compareTo(lastDay) >= 0) {
			throw new BadDatesException();
		}

		if (dataAccess.existsOverlappingOffer(ruralHouse, firstDay, lastDay)) {
			throw new OverlappingOfferException();		
		}
		
		offer = dataAccess.createOffer(ruralHouse, firstDay, lastDay, price);

		System.out.println("<< ApplicationFacadeImpl: createOffer=> O= " + offer);
		return offer;
	}

	@WebMethod
	@Override
	public Vector<Offer> getOffers(RuralHouse ruralHouse, Date firstDay,  Date lastDay) throws BadDatesException {
		if (firstDay.compareTo(lastDay) >= 0) {
			throw new BadDatesException();
		}
		return new Vector<Offer>(dataAccess.getOffers(ruralHouse, firstDay, lastDay));
	}

	@Override
	public List<Offer> getOffers() {
		return dataAccess.getOffers();
	}

	@Override
	public List<Offer> getOffers(ReviewState reviewState) {
		return dataAccess.getOffers(reviewState);
	}

	@Override
	public List<Offer> getActiveOffers() {
		return dataAccess.getActiveOffers();
	}

	@Override
	public List<Offer> getActiveOffers(ReviewState reviewState) {
		return dataAccess.getActiveOffers(reviewState);
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
	public RuralHouse createRuralHouse(Owner owner, String name, String description, City city, String address) throws DuplicatedEntityException {
		System.out.println(">> ApplicationFacadeImpl: createRuralHouse=> description= " + description + " city= " + city);

		RuralHouse ruralHouse = null;

		if(!dataAccess.existsRuralHouse(description, city.getId())) {
			ruralHouse = dataAccess.createRuralHouse(owner, name, description, city, address);
		} else {
			throw new DuplicatedEntityException();
		}

		System.out.println("<< ApplicationFacadeImpl: createRuralHouse => " + ruralHouse);
		return ruralHouse;
	}

	@Override
	public ExtendedIterator<RuralHouse> ruralHouseIterator()  {
		System.out.println(">> ApplicationFacadeImpl: ruralHouseIterator");
		return new RuralHouseIterator(dataAccess.getRuralHouses());
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

	@Override
	public Optional<AbstractUser> createUser(String email, String username, String password, UserType userType) throws DuplicatedEntityException {
		System.out.println(">> ApplicationFacadeImpl: createUser=> email=" + email + "username= " + username + " password= " + password + " userType=" + userType);
		if(!dataAccess.existsEmail(email)) {
			if(!dataAccess.existsUser(username)) {
				return dataAccess.createUser(email, username, password, userType);
			} else {
				throw new DuplicatedEntityException(Error.DUPLICATED_USERNAME);
			}
		} else {
			throw new DuplicatedEntityException(Error.DUPLICATED_EMAIL);
		}
	}

	public UserType getUserTypeOf(String username) {
		UserType userType = dataAccess.getRole(username);
		return userType;
	}

	public AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException {
		return dataAccess.login(username, password);
	}

	public Review createReview(RuralHouse rh) {
		return dataAccess.createReview(rh);
	}

	public void updateReview(RuralHouse rh, Review r) {
		dataAccess.updateReview(rh, r);
	}

	@Override
	public Booking createBooking(Client client, Offer offer, Date startDate, Date endDate) throws BadDatesException {
		if (startDate.compareTo(endDate) >= 0) {
			throw new BadDatesException();
		}
		return dataAccess.createBooking(client, offer, startDate, endDate);
	}

	@Override
	public Vector<Booking> getBookings(Client client) {
		return dataAccess.getBookings(client);
	}

	public Locale getLocale() {
		locale = Locale.forLanguageTag(dataAccess.getConfig().getLocale().name());
		if(locale == null) {
			locale = Locale.getDefault();
		}
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public Vector<RuralHouse> getRuralHouses(Owner owner) {
		return dataAccess.getRuralHouses(owner);
	}

	@Override
	public Vector<RuralHouse> getRuralHouses(Owner owner, ReviewState reviewState) {
		return dataAccess.getRuralHouses(owner, reviewState);
	}

	@Override
	public Vector<Booking> getBookings() {
		return dataAccess.getBookings();
	}
	
	@Override
	public boolean datesRangeOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
		return dataAccess.datesRangeOverlap(startDate1, endDate1, startDate2, endDate2);
	}

}

