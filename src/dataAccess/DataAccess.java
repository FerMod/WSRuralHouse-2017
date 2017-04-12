package dataAccess;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.security.auth.login.AccountNotFoundException;

import configuration.ConfigXML;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
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

	public DataAccess()  {

		CONFIG = ConfigXML.getInstance();
		DB_PATH = CONFIG.getDbFilename();
		OVERWRITE_DB_FILE = CONFIG.overwriteFile();
		INIT_DB_VALUES = CONFIG.initValues();

		if(CONFIG != null) {
			
			System.out.println(">> DataAccess: Configuring database connection...");
			System.out.println("\tDB_PATH: " + DB_PATH);

			if(CONFIG.isLocalDatabes()) {
				
				System.out.println("\tOVERWRITE_DB_FILE: " + OVERWRITE_DB_FILE);
				System.out.println("\tINIT_DB_VALUES: " + INIT_DB_VALUES);

				// Overwrite the database file
				if(OVERWRITE_DB_FILE) {
					deleteDBFile(DB_PATH);
				}

				// Initialize database values.
				if(INIT_DB_VALUES) {
					File file = new File(DB_PATH);
					INIT_DB_VALUES = !file.exists();
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

		if (!CONFIG.isLocalDatabes()) {
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

	@Override
	public void initializeDB(){
		try{				

//			deleteTableContent("RuralHouse");
//			deleteTableContent("City");
//			deleteTableContent("Offer");
//			deleteTableContent("Client");
//			deleteTableContent("Owner");
			//deleteTableContent("Admin");

			RuralHouse rh;
			rh = createRuralHouse("Ezkioko etxea", createCity("Ezkio").getId());
			createOffer(rh, new Date(2017, 2, 03), new Date(2017, 5, 43), 293);
			createOffer(rh, new Date(2016, 3, 23), new Date(2018, 11, 23),593);
			createOffer(rh, new Date(2017, 1, 4), new Date(2017, 1, 5), 773);			

			createRuralHouse("Etxetxikia", createCity("Iruna").getId());
			createOffer(rh, new Date(2013, 1, 4), new Date(2017, 2, 5), 773);		

			createRuralHouse("Udaletxea", createCity("Bilbo").getId());		
			createOffer(rh, new Date(2017, 1, 5), new Date(2017, 1, 5), 93);		
			createOffer(rh, new Date(2016, 12, 14), new Date(2017, 1, 5), 876);		
			createOffer(rh, new Date(2017, 2, 12), new Date(2017, 4, 5), 233);		

			createRuralHouse("Gaztetxea", createCity("Renteria").getId());	
			createOffer(rh, new Date(2017, 6, 3), new Date(2017, 5, 5), 128);		
			createOffer(rh, new Date(2017, 5, 4), new Date(2017, 6, 20), 455);		

			createUser("paco@gmail.com", "paco", "paco123", Role.OWNER);
			createUser("imowner@gmail.com", "imowner", "imowner", Role.OWNER);
			createUser("client@gmail.com", "client", "client123", Role.CLIENT);
			createUser("juan@gmail.com", "juan", "juan321", Role.CLIENT);
			createUser("myaccount@hotmal.com", "acount", "my.account_is_nic3", Role.OWNER);
			//createUser("admin@admin.com", "admin", "admin", Role.ADMIN);

			System.out.println("Database initialized");

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
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
	public RuralHouse createRuralHouse(String description, int city) throws DuplicatedEntityException {
		RuralHouse ruralHouse= null;
		try {
			open();
			System.out.print(">> DataAccess: createRuralHouse(" + description + ", " + city + ") -> ");
			db.getTransaction().begin();
			ruralHouse = new RuralHouse(description, city);
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
			return null;
		case SUPER_ADMIN:
			return null;
		default:
			return null;
		}
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
					+ "WHERE u.username = :username ", AbstractUser.class)
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
		boolean found = false;
		try {
			open();
			System.out.print(">> DataAccess: Check if exists \"" + username + "\" -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username", AbstractUser.class)
					.setParameter("username", username);
			Vector<AbstractUser> result = new Vector<AbstractUser>(query.getResultList());
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
	public boolean existsEmail(String email) {
		boolean found = false;
		try {
			open();
			System.out.print("Check if exists \"" + email + "\" -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.email = :email", AbstractUser.class)
					.setParameter("email", email);
			Vector<AbstractUser> result = new Vector<AbstractUser>(query.getResultList());
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
	public boolean existsRuralHouse(String description, int city) {
		boolean found = false;
		try {
			open();
			System.out.print("Check if exists \"" + description + "\" -> ");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT DISTINCT rh "
					+ "FROM RuralHouse rh "
					+ "WHERE rh.description = :description "
					+ "AND rh.city = :city", RuralHouse.class)
					.setParameter("description", description)
					.setParameter("city", city);
			Vector<RuralHouse> result = new Vector<RuralHouse>(query.getResultList());
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
	public AbstractUser login(String username, String password) throws AuthException, AccountNotFoundException {	
		AbstractUser user = null;
		try {
			open();
			System.out.println(">> DataAccess: Login. " + username + " | " + password + " (don't look, this is secret)");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username ", AbstractUser.class)
					.setParameter("username", username);
			Vector<AbstractUser> result = new Vector<AbstractUser>(query.getResultList());
			if(!result.isEmpty()) {
				user = result.get(0);
				authenticate(user, password);
			} else {
				user = null;
				throw new AccountNotFoundException("Account not found.");
			}
		} catch (AuthException | AccountNotFoundException e) {
			user = null;
			throw e; //Throw exception and allow a method further up the call stack handle it.
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
	public Vector<RuralHouse> getRuralHouses() {
		Vector<RuralHouse> result = null;
		try {
			open();
			System.out.println(">> DataAccess: getRuralHouses()");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh FROM RuralHouse rh", RuralHouse.class);
			result = new Vector<RuralHouse>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;

	}

	@Override
	public Vector<Offer> getOffer(RuralHouse rh, Date firstDay,  Date lastDay) {
		Vector<Offer> result = null;
		try { 
			open();
			System.out.println(">> DataAccess: getOffers");
			RuralHouse rhn = db.find(RuralHouse.class, rh.getId());
			result = rhn.getOffers(firstDay,lastDay);
			printVector(result);
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
	public List<City> getCities() {
		Vector<City> result = null;
		try {
			open();
			System.out.println(">> DataAccess: getCities");
			TypedQuery<City> query = db.createQuery("SELECT c FROM City c", City.class);
			result = new Vector<City>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
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
			db.createQuery("DELETE FROM " + table + " t ").executeUpdate();
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
	 * Obtain all the offers by a price range defined by the user (tested!).
	 *  
	 * @param min the lowest price
	 * @param max the highest price
	 * @return vector of offers in the range
	 */
	public Vector<Offer> getOffersByPrice(int min, int max) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffersByPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT o"
					+ " FROM Offer o "
					+ "WHERE o.price>" + min + " AND o.price<" + max, Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the offers for a specific price defined by the user (tested!).
	 *  
	 * @param the price
	 * @return vector of offers with the price selected
	 */
	public Vector<Offer> getOffersByConcretePrice(int price) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffersByConcretePrice");
			TypedQuery<Offer> query = db.createQuery("SELECT o"
					+ " FROM Offer o "
					+ "WHERE o.price=" + price, Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain the offer with the lowest price (tested!).
	 *  
	 * @return vector with the offer with the lowest price 
	 */
	public Vector<Offer> getOffersByMinorPrice() {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffersByMinorPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT MIN(o.price)"
					+ " FROM Offer o", Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain the offer with the highest price (tested!).
	 *  
	 * @return vector with the offer with the highest price
	 */
	public Vector<Offer> getOffersByMaxPrice() {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOffersByMaxPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT MAX(o.price)"
					+ " FROM Offer o", Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain the highest price of the Offers (tested!).
	 *  
	 * @return highest price of the Offers
	 */
	public double getMaxPrice() {
		double result = 0;
		try{
			open();
			System.out.println(">> DataAccess: getMaxPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT MAX(o.price)"
					+ " FROM Offer o", Offer.class);
			Vector<Offer> vm = new Vector<Offer>(query.getResultList());
			result = vm.get(0).getPrice(); //There is only one highest price.
			printVector(vm);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
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
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
				+ "FROM User u "
				+ "WHERE u.username = :username AND u.password = :password", AbstractUser.class)
				.setParameter("username", username).setParameter("password", password);
		Vector<AbstractUser> result = new Vector<AbstractUser>(query.getResultList());
		close();
		return result.get(0);
	}

	/**
	 * Obtain the lowest price of the Offers (tested!).
	 *  
	 * @return lowest price of the Offers
	 */
	public double getMinorPrice() {
		double result = 0;
		try{
			open();
			System.out.println(">> DataAccess: getMinorPrice");
			TypedQuery<Offer> query = db.createQuery("SELECT MIN(o.price)"
					+ " FROM Offer o", Offer.class);
			Vector<Offer> vm = new Vector<Offer>(query.getResultList());
			result = vm.get(0).getPrice(); //There is only one lowest price.
			printVector(vm);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Modify the user's password.
	 *  
	 * @param the user
	 * @param the password to modify
	 */
	public void changeUsersPass(AbstractUser user, String password) {
		open();
		db.getTransaction().begin();
		user.setPassword(password);
		db.getTransaction().commit();
		close();
	}


	/**
	 * Modify the user's password.
	 *  
	 * @param the user
	 * @param the password to modify
	 */
	public void modifyUsersPass(AbstractUser user, String password) {
		open();
		db.getTransaction().begin();
		user.setPassword(password);
		db.getTransaction().commit();
		close();
	}

	/**
	 * Prints to the standard output the vector content
	 * 
	 * @param vector the vector of type {@code <T>}
	 */
	private <T> void printVector(Vector<T> vector) {
		System.out.println(Arrays.deepToString(vector.toArray()));
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
	public List<Offer> getOfferById(int idOffer) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getOfferById");
			TypedQuery<Offer> query = db.createQuery("SELECT * "
					+ "FROM Offer o "
					+ "WHERE o.id=" + idOffer, Offer.class);
			result = new Vector<Offer>(query.getResultList());
			printVector(result);
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
	public List<Offer> getBookingsOfClient(int idClient) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getBookingsOfClient");
			TypedQuery<Booking> queryB = db.createQuery("SELECT b.idOffer"
					+ " FROM Booking b WHERE b.idClient=" + idClient , Booking.class);
			List<Booking> bookings = new Vector<Booking>(queryB.getResultList());
			
			result = new Vector<Offer>();
			
			for(Booking bo : bookings) {
				List<Offer> offer = getOfferById(bo.getIdOffer());
				result.add(offer.get(0));
			}
			
			printVector(result);
		} catch	(Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

}
