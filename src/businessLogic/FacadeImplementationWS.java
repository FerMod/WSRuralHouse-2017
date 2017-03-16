package businessLogic;

import java.util.Date;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import configuration.ConfigXML;
import dataAccess.DataAccess;

//import domain.Booking;
import domain.Offer;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.AbstractUser.Role;

import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;

//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterfaceWS")
public final class FacadeImplementationWS  implements ApplicationFacadeInterfaceWS {

	public FacadeImplementationWS()  {
		ConfigXML c = ConfigXML.getInstance();
		if (c.getDataBaseOpenMode().equals("initialize")) {
			DataAccess dbManager = new DataAccess();
			dbManager.initializeDB();
			dbManager.close();
		}
	}

	public RuralHouse createRuralHouse(String description, String city) throws DuplicatedEntityException{
		System.out.println(">> FacadeImplementationWS: createRuralHouse=> description= " + description + " city= " + city);

		DataAccess dbManager = new DataAccess();	
		RuralHouse ruralHouse = null;

		try {
			ruralHouse = dbManager.createRuralHouse(description, city);
		} finally {
			dbManager.close();
		}

		System.out.println("<< FacadeImplementationWS: createRuralHouse => " + ruralHouse);
		return ruralHouse;
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) throws OverlappingOfferException, BadDatesException {
		System.out.println(">> FacadeImplementationWS: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);

		DataAccess dbManager = new DataAccess();
		Offer offer = null;

		try {
			if (firstDay.compareTo(lastDay) >= 0) {
				throw new BadDatesException();
			}

			if (!dbManager.existsOverlappingOffer(ruralHouse,firstDay,lastDay)) {
				offer = dbManager.createOffer(ruralHouse,firstDay,lastDay,price);		
			}
		} finally {
			dbManager.close();
		}

		System.out.println("<< FacadeImplementationWS: createOffer=> O= " + offer);
		return offer;
	}

	public void login(String username, String password) throws AuthException, AccountNotFoundException {
		DataAccess dbManager = new DataAccess();
		try {
			dbManager.login(username, password);
		} finally {
			dbManager.close();
		}
	}

	public AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException {
		System.out.println(">> FacadeImplementationWS: createUser=> email=" + email + "username= " + username + " password= " + password + " role=" + role);

		DataAccess dbManager = new DataAccess();
		AbstractUser user = null;

		try {
			user = dbManager.createUser(email, username, password, role);
		} finally {
			dbManager.close();
		}

		System.out.println("<< FacadeImplementationWS: createUser=> " + user);		
		return user;
	}

	public Role getRole(String username) {
		DataAccess dbManager = new DataAccess();
		Role role = dbManager.getRole(username);
		dbManager.close();
		return role;
	}

	public Vector<RuralHouse> getAllRuralHouses()  {
		System.out.println(">> FacadeImplementationWS: getAllRuralHouses");

		DataAccess dbManager = new DataAccess();

		Vector<RuralHouse>  ruralHouses = dbManager.getAllRuralHouses();
		dbManager.close();
		System.out.println("<< FacadeImplementationWS:: getAllRuralHouses");

		return ruralHouses;

	}

	@WebMethod
	public Vector<Offer> getOffers( RuralHouse rh, Date firstDay,  Date lastDay) {

		DataAccess dbManager=new DataAccess();
		Vector<Offer>  offers=new Vector<Offer>();
		offers = dbManager.getOffers(rh, firstDay, lastDay);
		dbManager.close();

		return offers;
	}	

	public void close() {
		DataAccess dbManager=new DataAccess();
		dbManager.close();
	}

	public void initializeBD(){
		DataAccess dbManager=new DataAccess();
		dbManager.initializeDB();
		dbManager.close();
	}
	
}

