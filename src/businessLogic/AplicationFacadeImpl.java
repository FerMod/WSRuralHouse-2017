package businessLogic;

import java.util.Date;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.security.auth.login.AccountNotFoundException;

import dataAccess.DataAccessInterface;
import domain.Offer;
import domain.RuralHouse;
import domain.AbstractUser;
import domain.AbstractUser.Role;

import exceptions.AuthException;
import exceptions.BadDatesException;
import exceptions.DuplicatedEntityException;
import exceptions.OverlappingOfferException;
import exceptions.DuplicatedEntityException.Error;

//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterface")
public final class AplicationFacadeImpl  implements AplicationFacadeInterface {

	private DataAccessInterface dataAccess;

	public void setDataAccess(DataAccessInterface dataAccess) {
		this.dataAccess = dataAccess;
	}

	public RuralHouse createRuralHouse(String description, String city) throws DuplicatedEntityException{
		System.out.println(">> FacadeImpl: createRuralHouse=> description= " + description + " city= " + city);

		RuralHouse ruralHouse = null;

		if(!dataAccess.existsRuralHouse(description, city)) {
			ruralHouse = dataAccess.createRuralHouse(description, city);
		} else {
			throw new DuplicatedEntityException();
		}

		System.out.println("<< FacadeImpl: createRuralHouse => " + ruralHouse);
		return ruralHouse;
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) throws OverlappingOfferException, BadDatesException {
		System.out.println(">> FacadeImpl: createOffer=> ruralHouse= "+ruralHouse+" firstDay= "+firstDay+" lastDay="+lastDay+" price="+price);

		Offer offer = null;

		if (firstDay.compareTo(lastDay) >= 0) {
			throw new BadDatesException();
		}

		if (!dataAccess.existsOverlappingOffer(ruralHouse,firstDay,lastDay)) {
			offer = dataAccess.createOffer(ruralHouse,firstDay,lastDay,price);		
		}

		System.out.println("<< FacadeImpl: createOffer=> O= " + offer);
		return offer;
	}

	public void login(String username, String password) throws AuthException, AccountNotFoundException {
		dataAccess.login(username, password);
	}

	public AbstractUser createUser(String email, String username, String password, Role role) throws DuplicatedEntityException {
		System.out.println(">> FacadeImpl: createUser=> email=" + email + "username= " + username + " password= " + password + " role=" + role);
		if(!dataAccess.existsUser(username)) {
			if(!dataAccess.existsEmail(email)) {
				return dataAccess.createUser(email, username, password, role);
			} else {
				throw new DuplicatedEntityException(Error.DUPLICATED_EMAIL);
			}
		} else {
			throw new DuplicatedEntityException(Error.DUPLICATED_USERNAME);
		}
	}

	public Role getRole(String username) {
		Role role = dataAccess.getRole(username);
		return role;
	}

	public Vector<RuralHouse> getAllRuralHouses()  {
		System.out.println(">> FacadeImpl: getAllRuralHouses");
		return new Vector<RuralHouse>(dataAccess.getAllRuralHouses());
	}

	@WebMethod
	public Vector<Offer> getOffers(RuralHouse rh, Date firstDay,  Date lastDay) {
		return new Vector<Offer>(dataAccess.getOffers(rh, firstDay, lastDay));
	}	

}

