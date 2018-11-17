package dataAccess;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.security.auth.login.AccountNotFoundException;

import businessLogic.util.LogFile;
import businessLogic.util.Timer;
import configuration.Config;
import configuration.ConfigXML;
import domain.AbstractUser;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.ParticularClient;
import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;
import domain.TravelAgency;
import domain.UserFactory;
import domain.UserType;
import domain.util.ExtendedIterator;
import domain.util.RuralHouseIterator;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;

public class DataAccess implements DataAccessInterface {

	private final Config CONFIG;
	private static String DB_PATH;
	private static boolean OVERWRITE_DB_FILE;
	private static boolean INIT_DB_VALUES;

	private EntityManagerFactory emf;
	private EntityManager db;

	private Timer timer;

	@Deprecated
	private String[] images = {"/img/house00.png", "/img/house01.png", "/img/house02.png", "/img/house03.png", "/img/house04.png"};

	public DataAccess()  {
		
		timer = new Timer();

		CONFIG = ConfigXML.getInstance();
		DB_PATH = CONFIG.getDBFilename();
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
		db = ExponentialBackOff.execute(() -> emf.createEntityManager(), "Could not open database.");

		System.out.println("Database opened");
	}


	private void close() {
		if(db != null && db.isOpen()) {
			db.close();
			System.out.println("Database closed");
		}
	}

	@Override
	public Config getConfig() {
		return CONFIG;	
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
		entity = db.contains(entity) ? entity : db.merge(entity);
		db.getTransaction().commit();
		close();
		return entity;
	}

	/**
	 * Method used to remove a entity from the database
	 * 
	 * @param entity the entity that will be removed
	 * @return the managed instance that has been removed
	 */	
	@Override
	public <T> T remove(T entity) {
		open();
		db.getTransaction().begin();
		entity = db.contains(entity) ? entity : db.merge(entity);
		db.remove(entity);
		db.getTransaction().commit();
		close();
		return entity;
	}

	/**
	 * Method used to remove a entity from the database with the specified key
	 * @param <T> the entity type
	 * @param <K> the entity primary key type
	 *  
	 * @param entityClass the entity class type
	 * @param primaryKey the entity primary key
	 * @return the managed instance that has been removed
	 */
	@Override
	public <T, K> T remove(Class<T> entityClass, K primaryKey) {
		open();
		db.getTransaction().begin();
		T entity = db.find(entityClass, primaryKey);
		entity = db.contains(entity) ? entity : db.merge(entity);
		db.remove(entity);
		db.getTransaction().commit();
		close();
		return entity;
	}

	/**
	 * Find by primary key. Search for an entity of the specified class and primary key.
	 * If the entity instance is contained in the persistence context, it is returned from there. 
	 * 
	 * @param entityClass the entity class type
	 * @param primaryKey the entity primary key
	 * @return the found entity instance or <code>null</code> if the entity does not exist
	 */
	@Override
	public <T, K> T find(Class<T> entityClass, K primaryKey) {
		open();
		T entity = db.find(entityClass, primaryKey);
		close();
		return entity;
	}

	@Override
	public void initializeDB(){
		try{				

			// deleteTableContent("RuralHouse");
			// deleteTableContent("City");
			// deleteTableContent("Offer");
			// deleteTableContent("Client");
			// deleteTableContent("Owner");
			// deleteTableContent("Admin");

			Owner owner1 = (Owner)createUser("paco@gmail.com", "paco", "paco123", UserType.OWNER).get();
			Owner owner2 = (Owner)createUser("imowner@gmail.com", "imowner", "imowner", UserType.OWNER).get();
			// Owner owner3 = (Owner)createUser("myaccount@hotmal.com", "acount", "my.account_is_nic3", UserType.OWNER).get();
			Client client = (Client)createUser("client@gamail.com", "client", "client123", UserType.CLIENT).get();

			Admin admin = (Admin)createUser("admin@admin.com", "admin", "admin", UserType.ADMIN).get();

			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");

			RuralHouse rh1 = createRuralHouse(owner1, "Ezkioko etxea", "Una descripcion de la casa", createCity("Ezkio"), "Calle Falsa / 123");
			rh1.addImage(DataAccess.class.getResource(getRandomImage()).toURI());
			rh1.getReview().setState(admin, ReviewState.APPROVED);
			update(rh1);
			createOffer(rh1, date.parse("2017/2/3"), date.parse("2017/3/23"), 13);
			createOffer(rh1, date.parse("2017/5/2"), date.parse("2017/7/16"), 24);
			createOffer(rh1, date.parse("2017/10/3"), date.parse("2017/12/22"), 23);

			RuralHouse rh2 = createRuralHouse(owner1, "Etxetxikia", "Casa en zona tranquila sin trafico", createCity("Iruna"), "Plz. square 1 3ºA");
			rh2.addImage(DataAccess.class.getResource(getRandomImage()).toURI());
			rh2.getReview().setState(admin, ReviewState.APPROVED);
			update(rh2);
			createOffer(rh2, date.parse("2013/10/3"), date.parse("2018/2/8"), 19);		

			RuralHouse rh3 = createRuralHouse(owner2, "Udaletxea", "Localizada en un sitio, con gente", createCity("Bilbo"), "Street 3 3ºF");	
			rh3.addImage(DataAccess.class.getResource(getRandomImage()).toURI());
			rh3.getReview().setState(admin, ReviewState.APPROVED);
			update(rh3);
			createOffer(rh3, date.parse("2017/1/5"), date.parse("2019/1/19"), 17);		
			createOffer(rh3, date.parse("2016/12/14"), date.parse("2017/1/3"), 9);		
			createOffer(rh3, date.parse("2013/10/10"), date.parse("2015/2/1"), 5);		

			RuralHouse rh4 = createRuralHouse(owner2, "Gaztetxea", "Se me acaban las ideas para descripciones de casa, pero quiero que sea una larga para ver como se representaria.\n"
					+ "Y si admins tiene saltos de linea?\n"
					+ "Se vera como reacciona todo, pero deberia caber y si no, poner un limite.\n"
					+ "Como por ejemplo poner tres puntos suspensivos cuando supera ciertos caracterers.", createCity("Renteria"), "Plaza Grande 5 8-C");	
			rh4.addImage(DataAccess.class.getResource(getRandomImage()).toURI());
			rh4.getReview().setState(admin, ReviewState.APPROVED);
			update(rh4);
			// Offer offer = createOffer(rh4, date.parse("2017/5/3"), date.parse("2017/6/3"), 20);		
			createOffer(rh4, date.parse("2017/6/7"), date.parse("2017/6/20"), 13);		
			admin = (Admin) createUser("adminTemp@admin.com", "adminTemp", "adminTemp", UserType.ADMIN).get();
			Owner owner = (Owner) createUser("own@gmail.com", "own", "own", UserType.OWNER).get();			
			RuralHouse rh = createRuralHouse(owner, "Rural House Name", "Descripcion de la casa bonita", createCity("Donostia"), "La calle larga 4 - 3ºb");
			rh.getReview().setState(admin, ReviewState.APPROVED);
			update(rh);
			Offer offer1 = createOffer(rh, date.parse("2017/1/20"), date.parse("2017/3/23"), 13);
			Offer offer2 = createOffer(rh, date.parse("2017/5/7"), date.parse("2017/9/16"), 24);
			createBooking(client, offer1, date.parse("2017/1/4"), date.parse("2019/2/20"));
			createBooking(client, offer2, date.parse("2017/6/13"), date.parse("2019/8/2"));

			System.out.println("Database initialized");

			ExtendedIterator<RuralHouse> rhs = new RuralHouseIterator(getRuralHouses());

			while (rhs.hasNext()) {
				System.out.println(((RuralHouse) rhs.next()).getReview().toString());
			}

		} catch (Exception e){
			LogFile.log(e, true);
			e.printStackTrace();
		}
	}

	@Deprecated
	public String getRandomImage() {
		// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
		return images[ThreadLocalRandom.current().nextInt(0, images.length)];
	}

	@Override
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) {
		Offer offer = null;
		try {	
			open();
			System.out.print(">> DataAccess: createOffer(" + ruralHouse + ", " + firstDay + ", " + lastDay + ", " + price + ") -> ");
			RuralHouse rh = db.find(RuralHouse.class, ruralHouse.getId());
			db.getTransaction().begin();
			//			offer = rh.createOffer(firstDay, lastDay, price);
			offer = new Offer(firstDay, lastDay, price, rh);
			db.persist(offer);
			db.flush();
			rh.addOffer(offer);
			db.getTransaction().commit();
			System.out.println("Created with id " + offer.getId());
			return offer;
		} catch (Exception e){
			System.err.print("Offer not created because of: ");
			e.printStackTrace();
		} finally {
			close();
		}
		return offer;
	}

	@Override
	public List<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay) {
		List<Offer> result = null;
		try { 
			open();
			System.out.println(">> DataAccess: getOffers");
			RuralHouse rhn = db.find(RuralHouse.class, rh.getId());
			result = rhn.getOffers(firstDay,lastDay);
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Get all the active offers. This means, that this will query 
	 * the offers that had not reached the end date.
	 * 
	 * @return the {@code Vector} with elements of the type {@code Offer}, that represent the active offers
	 */
	public Vector<Offer> getActiveOffers() {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getActiveOffers()");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o "
					+ "AND o.endDate >= :currentDate", Offer.class)
					.setParameter("currentDate", Calendar.getInstance().getTime());
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the offers stored in the database that haven't ended yet, and matches with the given {@code ReviewState} of their rural house
	 *
	 * @return a {@code Vector} with objects of type {@code Offer} containing all the active offers in the database matching with the given {@code ReviewState} of their rural house, {@code null} if none is found
	 */
	public Vector<Offer> getActiveOffers(ReviewState reviewState) {
		Vector<Offer> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getActiveOffers(" + reviewState + ")");
			TypedQuery<Offer> query = db.createQuery("SELECT o "
					+ "FROM Offer o "
					+ "WHERE o.ruralHouse.review.reviewState == :reviewState "
					+ "AND o.endDate >= :currentDate", Offer.class)
					.setParameter("reviewState", reviewState)
					.setParameter("currentDate", Calendar.getInstance().getTime());
			result = new Vector<Offer>(query.getResultList());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	@Override
	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) {
		try{
			open();
			RuralHouse ruralHouse = db.find(RuralHouse.class, rh.getId());
			if (ruralHouse.overlapsWith(firstDay, lastDay).isPresent()) {
				return true;
			}
		} catch (Exception e){
			e.printStackTrace();
			return true;
		} finally {
			close();
		}
		return false;
	}

	@Override
	public boolean datesRangeOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
		return endDate1.compareTo(startDate2) > 0 && startDate1.compareTo(endDate2) < 0;
	}

	@Override
	public RuralHouse createRuralHouse(Owner owner, String name, String description, City city, String address) throws DuplicatedEntityException {
		RuralHouse ruralHouse= null;
		try {
			open();
			System.out.print(">> DataAccess: createRuralHouse(" + owner + ", " + name + ", " + description + ", " + city + ", " + address + ") -> ");
			db.getTransaction().begin();
			ruralHouse = new RuralHouse(owner, name, description, city, address);
			ruralHouse.setReview(new Review(ruralHouse));
			db.persist(ruralHouse);
			db.getTransaction().commit();
			System.out.println("Created with id " + ruralHouse.getId());
		} catch	(Exception e) {
			LogFile.log(e, true);
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
	//			LogFile.generateFile(e, true);
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
			System.out.println(">> DataAccess: ruralHouseIterator()");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh "
					+ "FROM RuralHouse rh ", RuralHouse.class);
			result = new Vector<RuralHouse>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the rural houses matching with the entered {@code Owner}
	 *
	 * @param owner the owner of the rural house
	 * @return a {@code Vector} with objects of type {@code RuralHouse} containing all the rural houses 
	 * matching with the {@code Owner}, {@code null} if none is found
	 * 
	 */
	@Override
	public Vector<RuralHouse> getRuralHouses(Owner owner) {
		Vector<RuralHouse> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getRuralHouses(" + owner + ")");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh "
					+ "FROM RuralHouse rh "
					+ "WHERE rh.owner = :owner ", RuralHouse.class)
					.setParameter("owner", owner);
			result = new Vector<RuralHouse>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
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
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Obtain all the rural houses matching with the entered {@code Owner} and {@code ReviewState}
	 *
	 * @param owner the owner of the rural house
	 * @param reviewState one of the possible states of a {@code Review}
	 * @return a {@code Vector} with objects of type {@code RuralHouse} containing all the rural houses 
	 * matching with the {@code Owner} and {@code ReviewState}, {@code null} if none is found
	 * 
	 * @see ReviewState
	 */
	@Override
	public Vector<RuralHouse> getRuralHouses(Owner owner, ReviewState reviewState) {
		Vector<RuralHouse> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getRuralHouses(" + owner + ", " + reviewState + ")");
			TypedQuery<RuralHouse> query = db.createQuery("SELECT rh "
					+ "FROM RuralHouse rh "
					+ "WHERE rh.owner = :owner "
					+ "AND rh.review.reviewState = :reviewState ", RuralHouse.class)
					.setParameter("owner", owner)
					.setParameter("reviewState", reviewState);
			result = new Vector<RuralHouse>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
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
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		System.out.println(false);
		System.out.println(timer.getFormattedFinishTime());
		return false;
	}

	@Override
	public Optional<AbstractUser> createUser(String email, String username, String password, UserType userType) throws DuplicatedEntityException {
		AbstractUser user = null;
		try {
			open();
			System.out.print(">> DataAccess: createUser(" + email + ", " + username +", " + password + ", " + userType + ") -> ");
			db.getTransaction().begin();
			UserFactory<AbstractUser>  userFactory = getUserFactory(userType).get();
			user = userFactory.create(email, username, password);
			db.persist(user);
			db.getTransaction().commit();
			System.out.println("Created with id " + user.getId());
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return Optional.ofNullable(user);
	}

	private Optional<UserFactory<AbstractUser>> getUserFactory(UserType userType) {
		switch (userType) {
		case CLIENT:
			return Optional.ofNullable(Client::new);
		case PARTICULAR_CLIENT:
			return Optional.ofNullable(ParticularClient::new);
		case TRAVEL_AGENCY:
			return Optional.ofNullable(TravelAgency::new);
		case OWNER:
			return Optional.ofNullable(Owner::new);
		case ADMIN:
			return Optional.ofNullable(Admin::new);
		case SUPER_ADMIN:
		default:
			return Optional.empty();
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
	public UserType getRole(String username) {
		UserType userType = null;
		try {
			open();
			System.out.print(">> DataAccess: getRole(" + username + ") -> ");
			TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
					+ "FROM User u "
					+ "WHERE u.username = :username", AbstractUser.class)
					.setParameter("username", username);
			Vector<AbstractUser> result = new Vector<AbstractUser>(new Vector<AbstractUser>(query.getResultList()));
			userType = result.get(0).getRole();
			System.out.println(userType);	
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return userType;
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
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
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {			
			close();
		}
	}

	/**
	 * Modify the user's password.
	 *  
	 * @param user the user
	 * @param password the password to modify
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

	/**
	 * Creates a booking for the introduced client of the passed offer.
	 * The booking is made between the passed dates.
	 * 
	 * @param client the client who is making the booking
	 * @param offer the offer to book
	 * @param startDate the first date of the booking
	 * @param endDate the end date of the booking
	 * 
	 * @return the booking
	 */
	@Override
	public Booking createBooking(Client client, Offer offer, Date startDate, Date endDate) {
		Booking booking= null;
		try {
			open();
			System.out.print(">> DataAccess: createBooking(" +  client + ", " + offer + ", " + startDate + ", " + endDate + ") -> ");
			db.getTransaction().begin();
			double price = getPrice(offer.getPrice(), startDate, endDate);
			booking = new Booking(client, offer, price, startDate, endDate);
			Client clientInstance = db.find(Client.class, client);
			clientInstance.getBookings().add(booking);
			Offer offerInstance = db.find(Offer.class, offer);
			offerInstance.setBooked(true);
			db.persist(booking);
			db.getTransaction().commit();
			System.out.println("Created with client " + booking.getClient().getUsername() + "and with offer " + booking.getOffer().toString());
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		update(client);
		update(offer);
		return booking;
	}

	/**
	 * Get the difference between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the difference
	 * @return the difference value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillis = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
	}

	private double getPrice(double price, Date startDate, Date endDate) {
		long days = getDateDiff(startDate, endDate, TimeUnit.DAYS) + 1;
		return days * price;
	}

	/**
	 * Obtain a {@code Vector} filled with bookings made
	 * by the matching client.
	 * 
	 * @param client the client of the bookings
	 * @return a {@code Vector} filled with elements of type {@code Booking}, that
	 * represents the bookings made by the client, returns {@code null} otherwise.
	 */
	@Override
	public Vector<Booking> getBookings(Client client) {
		Vector<Booking> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getBookings(" + client + ")");
			TypedQuery<Booking> query = db.createQuery("SELECT b "
					+ "FROM Booking b "
					+ "WHERE b.client = :client", Booking.class)
					.setParameter("client", client);
			result = new Vector<Booking>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	/**
	 * Return a {@code Vector} with all the stored bookings
	 * 
	 * @return a {@code Vector} filled with bookings
	 */
	@Override
	public Vector<Booking> getBookings() {
		Vector<Booking> result = null;
		try{
			open();
			System.out.println(">> DataAccess: getBookings()");
			TypedQuery<Booking> query = db.createQuery("SELECT b "
					+ "FROM Booking b ", Booking.class);
			result = new Vector<Booking>(query.getResultList());
			System.out.println("Found " + query.getResultList().size());
			printCollection(result);
		} catch	(Exception e) {
			LogFile.log(e, true);
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
			System.out.println("Created review in Rural House " + rh.toString());
		} catch	(Exception e) {
			LogFile.log(e, true);
			e.printStackTrace();
		} finally {
			close();
		}
		return review;
	}

	/**
	 * Update a review of a Rural House
	 * 
	 * @param rh the rural house of a owner
	 * @param r the review of a rural house
	 */
	@Override
	public void updateReview(RuralHouse rh, Review r) {
		rh.setReview(r);
		update(r);
	}


}
