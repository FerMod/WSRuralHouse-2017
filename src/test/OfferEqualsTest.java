package test;

import static org.junit.Assume.assumeNoException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import businessLogic.ApplicationFacadeInterface;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.ParticularClient;
import domain.RuralHouse;
import domain.TravelAgency;
import domain.UserType;
import domain.Review.ReviewState;
import test.contract.EqualsContract;
import test.util.TestUtilities;

public class OfferEqualsTest implements EqualsContract<Offer> {

	static RuralHouse rh;
	static Admin admin;
	static ParticularClient particularClient;
	static TravelAgency travelAgency;
	static Owner owner;
	static City city;
	static ApplicationFacadeInterface afi;
	
	Date startDate;
	Date endDate;
	Offer offer1, offer2;
	Booking booking;
	double price;

	@BeforeAll
	static void beforeAll() {
		afi = TestUtilities.getApplicationFacadeInstance();
		createTestData();
	}

	static void createTestData() {
		try {
			
			admin = (Admin) afi.createUser("adminTest@admin.com", "adminTest", "adminTest", UserType.ADMIN).get();
			owner = (Owner) afi.createUser("ownerTest@gmail.com", "ownerTest", "ownerTest", UserType.OWNER).get();
			particularClient = (ParticularClient) afi.createUser("particularClient@gamail.com", "ParticularClientTest", "ParticularClientTest", UserType.PARTICULAR_CLIENT).get();
			travelAgency = (TravelAgency) afi.createUser("travelagencytest@gamail.com", "TravelAgencyTest", "TravelAgencyTest", UserType.TRAVEL_AGENCY).get();
			city = afi.createCity("TestCity");

			rh = afi.createRuralHouse(owner, "Casa Test", "Descripción Test", city, "Calle Test / 12Test");
			rh.getReview().setState(admin, ReviewState.APPROVED);
			afi.update(rh);

		} catch (Exception e) {
			assumeNoException("Exception raised when creating the test data.", e);
		}		
	}

	@BeforeEach
	void beforeEach() {

		startDate = null;
		endDate = null;
		offer1 = null;
		offer2 = null;
		booking = null;
		price = 550.0;

	}

	@AfterAll
	static void afterAll() {
		if (rh != null) {
			afi.remove(rh);
		}
		if (admin != null) {
			afi.remove(admin);
		}
		if (particularClient != null) {
			afi.remove(particularClient);
		}
		if (travelAgency != null) {
			afi.remove(travelAgency);
		}
		if (owner != null) {
			afi.remove(owner);
		}
		if (city != null) {
			afi.remove(city);
		}
	}

	@AfterEach
	void afterEach() {
		if(offer1 != null) {
			afi.remove(offer1);
		}
		if(offer2 != null) {
			afi.remove(offer2);
		}
	}

	@Override
	public Offer createValue() {
		Date firstDay = TestUtilities.parseToDate(LocalDate.of(2018, Month.SEPTEMBER, 12));
		Date lastDay = TestUtilities.parseToDate(LocalDate.of(2018, Month.SEPTEMBER, 13));
		offer1 = TestUtilities.createTestOffer(rh, firstDay, lastDay, price);
		return offer1;
	}

	@Override
	public Offer createNotEqualValue() {
		Date firstDay = TestUtilities.parseToDate(LocalDate.of(2018, Month.SEPTEMBER, 13));
		Date lastDay = TestUtilities.parseToDate(LocalDate.of(2030, Month.DECEMBER, 07));
		offer2 = TestUtilities.createTestOffer(rh, firstDay, lastDay, price);
		return offer2;
	}

}
