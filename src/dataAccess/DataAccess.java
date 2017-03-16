package dataAccess;


import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.security.auth.login.AccountNotFoundException;

import configuration.ConfigXML;
//import domain.Booking;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.AbstractUser.Role;
import domain.Client;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;
import exceptions.DuplicatedEntityException.Error;
import exceptions.OverlappingOfferException;

public class DataAccess  {

	public static String fileName;
	protected static EntityManagerFactory emf;
	protected static EntityManager  db;

	ConfigXML c;

	public DataAccess()  {

		c = ConfigXML.getInstance();

		System.out.println("Creating objectdb instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());
		
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory(c.getDbFilename());
			db = emf.createEntityManager();			
		} else {		
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());
			emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+c.getDbFilename(), properties);
			db = emf.createEntityManager();				
		}
	}

	public void initializeDB(){

		db.getTransaction().begin();
		try{			

			TypedQuery<RuralHouse> query = db.createQuery("SELECT c FROM RuralHouse c", RuralHouse.class);
			List<RuralHouse> results = query.getResultList();

			Iterator<RuralHouse> itr = results.iterator();

			while (itr.hasNext()){
				RuralHouse rh=itr.next();
				db.remove(rh);				
			}

			RuralHouse rh1 = new RuralHouse("Ezkioko etxea","Ezkio");
			RuralHouse rh2 = new RuralHouse("Etxetxikia","Iruna");
			RuralHouse rh3 = new RuralHouse("Udaletxea","Bilbo");
			RuralHouse rh4 = new RuralHouse("Gaztetxea","Renteria");

			db.persist(rh1);
			db.persist(rh2);
			db.persist(rh3);
			db.persist(rh4);

			db.getTransaction().commit();
			System.out.println("Db initialized");

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
		System.out.println(">> DataAccess: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);

		try {	
			RuralHouse rh = db.find(RuralHouse.class, ruralHouse.getHouseNumber());

			db.getTransaction().begin();
			Offer o = rh.createOffer(firstDay, lastDay, price);
			db.persist(o);
			db.getTransaction().commit();
			return o;

		}
		catch (Exception e){
			System.out.println("Offer not created: "+e.toString());
			return null;
		}
	}

	public RuralHouse createRuralHouse (String description, String city) throws DuplicatedEntityException {
		System.out.println(">> DataAccess: createRuralHouse=> description= " + description + " city= " + city);
		RuralHouse ruralHouse= null;
		if(!existsRuralHouse(description, city)) {
			db.getTransaction().begin();
			ruralHouse = new RuralHouse(description, city);
			db.persist(ruralHouse);
			db.getTransaction().commit();
		} else {
			throw new DuplicatedEntityException();
		}
		return ruralHouse;
	}

	public AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException {
		System.out.println(">> DataAccess: createUser=> email=" + email + " username=" + username + " password=" + password + " role=" + role);
		AbstractUser user = null;
		if(!existsUser(username)) {
			if(!existsEmail(email)) {
				db.getTransaction().begin();
				user = getNewUser(email, username, password, role);
				db.persist(user);
				db.getTransaction().commit();
			} else {
				throw new DuplicatedEntityException(Error.DUPLICATED_EMAIL);
			}
		} else {
			throw new DuplicatedEntityException(Error.DUPLICATED_USERNAME);
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

	public boolean validDni(String dni) {
		return dni.toUpperCase().matches("\\d{8}" + controlLetter(dni));
	}

	private char controlLetter(String dni) {
		char[] controlLetter = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
		return controlLetter[Integer.parseInt(dni.substring(8)) % 23];
	}

	public Role getRole(String username) {
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
				+ "FROM User u "
				+ "WHERE u.username = :username ", AbstractUser.class)
				.setParameter("username", username);
		List<AbstractUser> result = query.getResultList();
		return result.get(0).getRole();
	}

	public boolean existsUser(String username) {
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
				+ "FROM User u "
				+ "WHERE u.username = :username", AbstractUser.class)
				.setParameter("username", username);
		List<AbstractUser> result = query.getResultList();
		return !result.isEmpty();
	}

	public boolean existsEmail(String email) {
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
				+ "FROM User u "
				+ "WHERE u.email = :email", AbstractUser.class)
				.setParameter("email", email);
		List<AbstractUser> result = query.getResultList();
		return !result.isEmpty();
	}

	public boolean existsRuralHouse(String description, String city) {
		TypedQuery<RuralHouse> query = db.createQuery("SELECT DISTINCT rh "
				+ "FROM RuralHouse rh "
				+ "WHERE rh.description = :description "
				+ "AND rh.city = :city", RuralHouse.class)
				.setParameter("description", description)
				.setParameter("city", city);
		List<RuralHouse> result = query.getResultList();
		return !result.isEmpty();
	}

	public void login(String username, String password) throws AuthException, AccountNotFoundException {	
		TypedQuery<AbstractUser> query = db.createQuery("SELECT DISTINCT u "
				+ "FROM User u "
				+ "WHERE u.username = :username ", AbstractUser.class)
				.setParameter("username", username);
		List<AbstractUser> result = query.getResultList();
		if(!result.isEmpty()) {
			AbstractUser user = result.get(0);
			authenticate(user, password);
		} else {
			throw new AccountNotFoundException("Account not found.");
		}
	}

	private void authenticate(AbstractUser user, String password) throws AuthException {
		if(!password.equals(user.getPassword())) {
			throw new AuthException("Authentification failed.");
		}
	}

	public Vector<RuralHouse> getAllRuralHouses() {
		System.out.println(">> DataAccess: getAllRuralHouses");
		Vector<RuralHouse> res = new Vector<>();

		TypedQuery<RuralHouse> query = db.createQuery("SELECT c FROM RuralHouse c", RuralHouse.class);
		List<RuralHouse> results = query.getResultList();

		Iterator<RuralHouse> itr = results.iterator();

		while (itr.hasNext()){
			res.add(itr.next());
		}

		return res;

	}

	public Vector<Offer> getOffers( RuralHouse rh, Date firstDay,  Date lastDay) {
		System.out.println(">> DataAccess: getOffers");
		Vector<Offer> res=new Vector<>();
		RuralHouse rhn = db.find(RuralHouse.class, rh.getHouseNumber());
		res = rhn.getOffers(firstDay,lastDay);
		return res;
	}

	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws  OverlappingOfferException{
		try{
			RuralHouse rhn = db.find(RuralHouse.class, rh.getHouseNumber());
			if (rhn.overlapsWith(firstDay,lastDay)!=null) return true;
		} catch (Exception e){
			System.out.println("Error: "+e.toString());
			return true;
		}
		return false;
	}

	public void close(){
		db.close();
		System.out.println("DataBase closed");
	}
}
