package dataAccess;

import java.awt.List;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.security.auth.login.AccountNotFoundException;

import businessLogic.util.Timer;
import configuration.ConfigXML;
import domain.AbstractUser;
import domain.Admin;
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

public class DataAccess implements DataAccessInterface {

	private final ConfigXML CONFIG;
	private static String DB_PATH;
	private static boolean OVERWRITE_DB_FILE;
	private static boolean INIT_DB_VALUES;

	private EntityManagerFactory emf;
	private EntityManager  db;

	private Timer timer;

	public DataAccess()  {

		timer = new Timer();

		CONFIG = ConfigXML.getInstance();
		DB_PATH = CONFIG.getDbFilename();
		OVERWRITE_DB_FILE = CONFIG.overwriteFile();
		INIT_DB_VALUES = CONFIG.initValues();

		if(CONFIG != null) {

			System.out.println(">> DataAccess: Configuring database connection...");
			System.out.println("\tDB_PATH: " + DB_PATH);

			if(CONFIG.isLocalDatabase()) {

				System.out.println("\tOVERWRITE_DB_FILE: " + OVERWRITE_DB_FILE);
				System.out.println("\tINIT_DB_VALUES: " + INIT_DB_VALUES);

				// Overwrite the database file
				if(OVERWRITE_DB_FILE) {
					deleteDBFile(DB_PATH);
				}

				// Initialize database values.
				if(INIT_DB_VALUES) {
					initializeDB();
				}

			}

		}

	}

	private static void deleteDBFile(String fileName) {
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();
			System.out.println(">> DataAccess: Removed the file \"" + fileName + "\"");
			deleteDBFile(fileName+"$");
		}
	}

	private void open() {

		Map<String, String> properties = null;

		if (!CONFIG.isLocalDatabase()) {
			properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", CONFIG.getUser());
			properties.put("javax.persistence.jdbc.password", CONFIG.getPassword());
		}

		emf = Persistence.createEntityManagerFactory(DB_PATH, properties);
		db = ExponentialBackOff.execute( () -> emf.createEntityManager(), "Could not open database.");

		System.out.println("Database opened");
	}


	private void close() {
		if(db != null && db.isOpen()) {
			db.close();
			System.out.println("Database closed");
		}
	}

	/**
	 * Method used to update a entity with their changes to the database
	 * 
	 * @param entity the entity that will be updated
	 * @return the managed instance that is updated
	 */
	@Override
	public <T> T update(T entity) {
		open();
		db.getTransaction().begin();
		T managedInstance = db.merge(entity);
		db.getTransaction().commit();
		close();
		return managedInstance;
	}

	@Override
	public void initializeDB(){
		try{				

			//			deleteTableContent("RuralHouse");
			//			deleteTableContent("City");
			//			deleteTableContent("Offer");
			//			deleteTableContent("Client");
			//			deleteTableContent("Owner");
			//deleteTableContent("Admin");
			

			Owner owner1 = (Owner)createUser("paco@gmail.com", "paco", "paco123", Role.OWNER);
			Owner owner2 = (Owner)createUser("imowner@gmail.com", "imowner", "imowner", Role.OWNER);
			createUser("client@gmail.com", "client", "client123", Role.CLIENT);
			createUser("juan@gmail.com", "juan", "juan321", Role.CLIENT);
			createUser("myaccount@hotmal.com", "acount", "my.account_is_nic3", Role.OWNER);
			
			createBooking(20, 3);
			getOfferById(20);
			//createUser("admin@admin.com", "admin", "admin", Role.ADMIN);

			Admin admin = (Admin)createUser("admin@admin.com", "admin", "admin", Role.ADMIN);

			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");

			RuralHouse rh1 = createRuralHouse(owner1, "Ezkioko etxea", createCity("Ezkio"), "Calle Falsa / 123");
			rh1.getReview().setState(admin, ReviewState.APPROVED);
			update(rh1);
			createOffer(rh1, date.parse("2017/2/3"), date.parse("2017/3/23"), 13);
			createOffer(rh1, date.parse("2017/5/23"), date.parse("2017/7/16"), 24);
			createOffer(rh1, date.parse("2017/10/3"), date.parse("2017/12/22"), 23);

			RuralHouse rh2 = createRuralHouse(owner1, "Etxetxikia", createCity("Iruna"), "Plz. square 1 3ºA");
			rh2.getReview().setState(admin, ReviewState.APPROVED);
			update(rh2);
			createOffer(rh2, date.parse("2013/10/3"), date.parse("2018/2/8"), 19);		

			RuralHouse rh3 = createRuralHouse(owner2, "Udaletxea", createCity("Bilbo"), "ñeñeñe 3 3ºñe");		
			rh3.getReview().setState(admin, ReviewState.APPROVED);
			update(rh3);
			createOffer(rh3, date.parse("2017/1/5"), date.parse("2019/1/19"), 17);		
			createOffer(rh3, date.parse("2016/12/14"), date.parse("2017/1/3"), 9);		
			createOffer(rh3, date.parse("2013/10/10"), date.parse("2015/2/1"), 5);		

			RuralHouse rh4 = createRuralHouse(owner2, "Gaztetxea", createCity("Renteria"), "Plhasa Bonitah 2 3sero se");	
			rh4.getReview().setState(admin, ReviewState.APPROVED);
			update(rh4);
			createOffer(rh4, date.parse("2017/5/3"), date.parse("2017/6/3"), 20);		
			createOffer(rh4, date.parse("2017/6/7"), date.parse("2017/6/20"), 13);		

			System.out.println("Database initialized");

			for (RuralHouse ruralHouse : getRuralHouses()) {
				System.out.println(ruralHouse.getReview().toString());
			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) {
		Offer offer = null;
		try {	
			open();
			System.out.print(">> DataAccess: createOffer(" + ruralHouse + ", " + firstDay + ", " + lastDay + ", " + price + ") -> ");
			RuralHouse rh = db.find(RuralHouse.class, ruralHouse.getId());
			db.getTransaction().begin();
			offer = rh.createOffer(firstDay, lastDay, price);
			db.persist(offer);
			db.getTransaction().commit();
			System.out.println("Created with id " + offer.getId());
			return offer;
		} catch (Exception e){
			System.err.println("Offer not created: " + e .toString());
		} finally {
			close();
		}
		return offer;
	}

	@Override
	public Vector<Offer> getOffer(RuralHouse rh, Date firstDay,  Date lastDay) {
		Vector<Offer> result = null;
		try { 
			open();
			System.out.println(">> DataAccess: getOffers");
			RuralHouse rhn = db.find(RuralHouse.class, rh.getId());
			result = rhn.getOffers(firstDay,lastDay);
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the offers within the price range defined by the parameters {@code min} and {@code max}
	 *  
	 * @param min the lowest price
	 * @param max the highest price
	 * @return vector of offers within the range
	 */
	public Vector<Offer> getOffersBetweenPrice(int min, int max) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffersByPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o "
					+ "WHERE o.price > :min "
					+ "AND o.price < :max", Offer.class)
					.setParameter("min", min)
					.setParameter("max", max);
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the offers stored in the database
	 *
	 * @return a {@code Vector} with objects of type {@code Offer} containing all the offers in the database, {@code null} if none is found
	 */
	public Vector<Offer> getOffers() {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffers()");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o ", Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the offers stored in the database that matches with the given {@code ReviewState} of their rural house
	 *
	 * @return a {@code Vector} with objects of type {@code Offer} containing all the offers in the database matching with the given {@code ReviewState} of their rural house, {@code null} if none is found
	 */
	public Vector<Offer> getOffers(ReviewState reviewState) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffers(" + reviewState + ")");
			TypedQuery<Offer> query = db.createQuery("SELECT o " +
					"FROM Offer o " +
					"WHERE o.ruralHouse.review.reviewState == :reviewState", Offer.class)
					.setParameter("reviewState", reviewState);
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Returns the number of offers stored in the database
	 *
	 * @return the number of offers in the database
	 */
	public int getOfferCount() {
		int result = 0;
		try{
			open();
			System.out.print(">> DataAccess: getOfferCount() -> ");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o ", Offer.class);
			result = query.getResultList().size();
			System.out.println(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Returns the highest price of the stored offers
	 *  
	 * @return highest price of the stored Offers
	 */
	public double getOffersHighestPrice() {
		double result = 0;
		try{
			open();
			System.out.print(">> DataAccess: getOffersHighestPrice() -> ");
			TypedQuery<Double> query = db.createQuery("SELECT MAX(o.price) "
					+ "FROM Offer o ", Double.class)
					.setMaxResults(1); //There is only one highest price.
			result = query.getSingleResult();
			System.out.println(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Returns the lowest price of the stored offers
	 *  
	 * @return lowest price of the stored offers
	 */
	public double getOffersLowestPrice() {
		double result = 0f;
		try{
			open();
			System.out.print(">> DataAccess: getOfferLowestPrice() -> ");
			TypedQuery<Double> query = db.createQuery("SELECT MIN(o.price) "
					+ "FROM Offer o ", Double.class)
					.setMaxResults(1); //There is only one lowest price.
			result = query.getSingleResult(); 
			System.out.println(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	@Override
	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws  OverlappingOfferException {
		try{
			open();

			RuralHouse ruralHouse = db.find(RuralHouse.class, rh.getId());
			if (ruralHouse.overlapsWith(firstDay, lastDay) != null) {
				return true;
			}
		} catch (Exception e){
			System.out.println("Error: " + e.toString());
			return true;
		} finally {
			close();
		}
		return false;
	}

	@Override
	public RuralHouse createRuralHouse(String description, City city) throws DuplicatedEntityException {
		return createRuralHouse(description, city);
	}

	@Override
	public RuralHouse createRuralHouse(Owner owner, String description, City city, String address) throws DuplicatedEntityException {
		RuralHouse ruralHouse= null;
		try {
			open();
			System.out.print(">> DataAccess: createRuralHouse(" + owner + ", " + description + ", " + city + ", " + address + ") -> ");
			db.getTransaction().begin();
			ruralHouse = new RuralHouse(owner, description, city, address);	
			Review review = new Review(ruralHouse);
			ruralHouse.setReview(review);
			db.persist(ruralHouse);
			db.getTransaction().commit();
			System.out.println("Created with id " + ruralHouse.getId());
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return ruralHouse;
	}

	//	public Review createReview(RuralHouse ruralHouse) {
	//		Review review= null;
	//		try {
	//			open();
	//			System.out.print(">> DataAccess: createReview(" + ruralHouse + ") -> ");
	//			db.getTransaction().begin();
	//			review = new Review(ruralHouse);
	//			db.persist(ruralHouse);
	//			db.getTransaction().commit();
	//			System.out.println("Created with id " + review.getId());
	//		} catch	(Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			close();
	//		}
	//		return review;
	//	}

	@Override
	public Vector<RuralHouse> getRuralHouses() {
		Vector<RuralHouse> result = null;
		try {
			open();
			System.out.println(">> DataAccess: getRuralHouses()");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh "
					+ "FROM RuralHouse rh ", RuralHouse.class);
			result = new Vector<RuralHouse>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;

	}

	/**
	 * Obtain all the rural houses matching with the entered {@code ReviewState}
	 *
	 * @param reviewState one of the possible states of a {@code Review}
	 * @return a {@code Vector} with objects of type {@code RuralHouse} containing all the rural houses matching with the {@code ReviewState}, {@code null} if none is found
	 * 
	 * @see ReviewState
	 */
	@Override
	public Vector<RuralHouse> getRuralHouses(ReviewState reviewState) {
		Vector<RuralHouse> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getRuralHouses(" + reviewState + ")");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh " +
					"FROM RuralHouse rh " +
					"WHERE rh.review.reviewState == :reviewState ", RuralHouse.class)
					.setParameter("reviewState", reviewState);
			result = new Vector<RuralHouse>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	@Override
	public boolean existsRuralHouse(String description, int city) {
		try {
			open();
			timer.startTimer();
			System.out.print("Check if exists \"" + description + "\" -> ");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT DISTINCT rh "
					+ "FROM RuralHouse rh "
					+ "WHERE rh.description = :description "
					+ "AND rh.city = :city", RuralHouse.class)
					.setParameter("description", description)
					.setParameter("city", city);
			query.getSingleResult();
			System.out.println(true);
			System.out.println(timer.getFormattedFinishTime());
			return true;
		} catch (NoResultException e) {
			// Dummy catch
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println(false);
		System.out.println(timer.getFormattedFinishTime());
		return false;
	}

	@Override
	public AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException {
		AbstractUser user = null;
		try {
			open();
			System.out.print(">> DataAccess: createUser(" + email + ", " + username +", " + password + ", " + role + ") -> ");
			db.getTransaction().begin();
			user = getNewUser(email, username, password, role);
			db.persist(user);
			db.getTransaction().commit();
			System.out.println("Created with id " + user.getId());
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return user;
	}

	private AbstractUser getNewUser(String email, String username, String password, Role role) {
		switch (role) {
		case CLIENT:
			return new Client(email, username, password);
		case OWNER:
			return new Owner(email, username, password);
		case ADMIN:
			return new Admin(email, username, password);
		case SUPER_ADMIN:
			return null;
		default:
			return null;
		}
	}

	/**
	 * Obtain a User by username and password from the database
	 * 
	 * @param username String with the username of the user
	 * @param password String with the password of the user
	 * @return The user with the username and password definied
	 */
	public AbstractUser getUser(String username, String password) {
		open();
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u"
				+ " FROM User u"
				+ " WHERE u.username = :username"
				+ " AND u.password = :password", AbstractUser.class)
				.setParameter("username", username)
				.setParameter("password", password);
		Vector<AbstractUser> result = new Vector<AbstractUser>(query.getResultList());
		close();
		return result.get(0);
	}

	@Override
	public boolean validDni(String dni) {
		return dni.toUpperCase().matches("\\d{8}" + controlLetter(dni));
	}

	private char controlLetter(String dni) {
		char[] controlLetter = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
		return controlLetter[Integer.parseInt(dni.substring(8)) % 23];
	}

	@Override
	public Role getRole(String username) {
		Role role = null;
		try {
			open();
			System.out.print(">> DataAccess: getRole(" + username + ") -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username", AbstractUser.class)
					.setParameter("username", username);
			Vector<AbstractUser> result = new Vector<AbstractUser>(new Vector<AbstractUser>(query.getResultList()));
			role = result.get(0).getRole();
			System.out.println(role);	
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return role;
	}

	@Override
	public boolean existsUser(String username) {
		try {
			open();
			timer.startTimer();
			System.out.print(">> DataAccess: Check if exists \"" + username + "\" -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username", AbstractUser.class)
					.setParameter("username", username)
					.setMaxResults(1);
			query.getSingleResult();
			System.out.println(true);
			System.out.println(timer.getFormattedFinishTime());
			return true;
		} catch (NoResultException e) {
			//Dummy catch
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println(false);
		System.out.println(timer.getFormattedFinishTime());
		return false;
	}

	@Override
	public boolean existsEmail(String email) {		
		try {
			open();
			timer.startTimer();
			System.out.print("Check if exists \"" + email + "\" -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.email = :email", AbstractUser.class)
					.setParameter("email", email)
					.setMaxResults(1);
			query.getSingleResult();
			System.out.println(true);
			System.out.println(timer.getFormattedFinishTime());
			return true;
		} catch (NoResultException e) {
			//Dummy catch.
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println(false);
		System.out.println(timer.getFormattedFinishTime());
		return false;
	}

	@Override
	public AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException {	
		AbstractUser user = null;
		try {
			open();
			timer.startTimer();
			System.out.println(">> DataAccess: Login. " + username + " | " + password + " (don't look, this is secret)");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username", AbstractUser.class)
					.setParameter("username", username)
					.setMaxResults(1);
			System.out.println(timer.getFormattedFinishTime());
			user = query.getSingleResult();
			authenticate(user, password);
		} catch (AuthException e) { 
			throw e; //Throw exception and allow a method further up the call stack handle it.
		} catch(NoResultException e) {
			throw new AccountNotFoundException("Account not found.");
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return user;
	}

	private void authenticate(AbstractUser user, String password) throws AuthException {
		if(!password.equals(user.getPassword())) {
			throw new AuthException("Authentification failed.");
		}
	}

	@Override
	public City createCity(String name) {
		City city= null;
		try {
			open();
			System.out.print(">> DataAccess: createCity(\"" + name + "\") -> ");
			db.getTransaction().begin();
			city = new City(name);
			db.persist(city);
			db.getTransaction().commit();
			System.out.println("Created with id " + city.getId());
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return city;
	}
	
	

	@Override
	public boolean existsCity(City city) {
		return existsCity(city.getId());
	}

	@Override
	public boolean existsCity(int id) {
		boolean found = false;
		try {
			open();
			System.out.print(">> DataAccess: Check if exists the city with id:\"" + id + "\" -> ");
			TypedQuery<City> query = db.createQuery("SELECT DISTINCT c "
					+ "FROM City c "
					+ "WHERE c.id = :id", City.class)
					.setParameter("id", id);
			Vector<City> result = new Vector<City>(query.getResultList());
			found = !result.isEmpty();
			System.out.println(found);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return found;
	}

	@Override
	public boolean existsCity(String name) {
		boolean found = false;
		try {
			open();
			System.out.print(">> DataAccess: Check if exists the city with the name:\"" + name + "\" -> ");
			TypedQuery<City> query = db.createQuery("SELECT DISTINCT c "
					+ "FROM City c "
					+ "WHERE c.name = :name", City.class)
					.setParameter("name", name);
			Vector<City> result = new Vector<City>(query.getResultList());
			found = !result.isEmpty();
			System.out.println(found);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return found;
	}

	@Override
	public Vector<City> getCities() {
		Vector<City> result = null;
		try {
			open();
			System.out.println(">> DataAccess: getCities");
			TypedQuery<City> query = db.createQuery("SELECT c FROM City c", City.class);
			result = new Vector<City>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Delete all the table entities
	 * 
	 * @param table the name of the table
	 */
	public void deleteTableContent(String table) {
		try {
			open();
			db.getTransaction().begin();
			System.out.println(">> DataAccess: Delete the table with the name: \"" + table + "\"");
			db.createQuery("DELETE FROM :table")
			.setParameter("table", table)
			.executeUpdate();
			db.getTransaction().commit();
		} catch (PersistenceException e) {
			System.err.println("Could not complete the operation: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			close();
		}
	}

	/**
	 * Modify the user's password.
	 *  
	 * @param the user
	 * @param the password to modify
	 */
	public void changeUserPassword(AbstractUser user, String password) {
		user.setPassword(password);
		update(user);
	}

	/**
	 * Prints to the standard output the content of any member of the
	 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
	 * Java Collections Framework</a>. 
	 * 
	 * @param <E> the type of elements in this collection
	 * @param collection the collection with elements
	 * 
	 * @see     Set
	 * @see     List
	 * @see     Map
	 * @see     SortedSet
	 * @see     SortedMap
	 * @see     HashSet
	 * @see     TreeSet
	 * @see     ArrayList
	 * @see     LinkedList
	 * @see     Vector
	 * @see     Collections
	 * @see     Arrays
	 * @see     AbstractCollection
	 */
	private <E> void printCollection(Collection<E> collection) {
		System.out.println(Arrays.deepToString(collection.toArray()));
	}
	
	@Override
	public Booking createBooking(int idClient, int idOffer) {
		Booking booking= null;
		try {
			open();
			System.out.print(">> DataAccess: createBooking(\"" + idClient + ", " + idOffer + "\") -> ");
			db.getTransaction().begin();
			booking = new Booking(idClient, idOffer);
			db.persist(booking);
			db.getTransaction().commit();
			System.out.println("Created with idClient " + booking.getIdClient() + "and with idOffer " + booking.getIdOffer());
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return booking;
	}
	
	/**
	 * Returns a list with the offer identified by his id
	 * 
	 * @param id of offer
	 * @return a list with the offer specified by his id
	 */
	@Override
	public Vector<Offer> getOfferById(int idOffer) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOfferById");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o WHERE o.id== :idOffer", Offer.class)
					.setParameter("idOffer", idOffer);
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
	/**
	 * Control the boolean booked of the offer
	 * 
	 * @param offer for set his booked value
	 * @param boolean for control the state of booking
	 */
	@Override
	public void offerBookedControl(Offer of, boolean booked) {
		open();
		db.getTransaction().begin();
		of.setBooked(booked);
		db.getTransaction().commit();
		close();
	}
	
	/**
	 * Return a list of bookings of the client specified
	 * 
	 * @param id of a client
	 * @return a list with his bookings
	 */
	@Override
	public Vector<Offer> getBookingsOfClient(int idClient) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getBookingsOfClient");
			TypedQuery<Booking> queryB = db.createQuery("SELECT b"
					+ " FROM Booking b WHERE b.idClient== :idClient", Booking.class)
					.setParameter("idClient", idClient);
			Vector<Booking> bookings = new Vector<Booking>(queryB.getResultList());
			
			result = new Vector<Offer>();
			
			for(Booking bo : bookings) {
				result.add(getOfferById(bo.getIdOffer()).get(0)); //Get the offers and stores in result vector.
			}
			
			printCollection(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
	
	@Override
	public Review createReview(RuralHouse rh) {
		Review review = null;
		try {
			open();
			System.out.print(">> DataAccess: createReview(\"" + rh.getName() + "\") -> ");
			db.getTransaction().begin();
			review = new Review(rh);
			db.persist(review);
			db.getTransaction().commit();
			System.out.println("Created in Rural House " + rh.toString());
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return review;
	}
	
	/**
	 * Update a review of a Rural House
	 * 
	 * @param Rural House of a Owner
	 * @param Review of a Rural House
	 */
	@Override
	public void updateReview(RuralHouse rh, Review r) {
		rh.setReview(r);
		update(r);
	}

}
