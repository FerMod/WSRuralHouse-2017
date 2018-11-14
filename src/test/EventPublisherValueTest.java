package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import businessLogic.ApplicationFacadeFactory;
import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import configuration.ConfigXML;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import domain.UserType;
import domain.Review.ReviewState;
import domain.observer.ObservedValue;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;
import test.data.PersonTest;

class EventPublisherValueTest {
	
	static RuralHouse rh;
	static Admin admin;
	static Client client;
	static Owner owner;
	static City city;
	static ApplicationFacadeInterface afi;
	static SimpleDateFormat dateFormat;

	Date startDate;
	Date endDate;
	Offer offer;
	Booking booking;
	double price;
	
	boolean eventInvoked;
	
	@BeforeAll
	static void beforeAll() {

		LogFile.FILE_NAME = "JUnitTest.log";

		initDataBase();
		createTestData();

	}
	
	static void initDataBase() {
		try {
			afi = ApplicationFacadeFactory.createApplicationFacade(ConfigXML.getInstance());
		} catch (Exception e) {
			System.err.println("An error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"");
			LogFile.log(e, true);
			e.printStackTrace();
		}
	}

	static void createTestData() {
		try {			

			dateFormat = new SimpleDateFormat("yyyy/MM/dd");

			admin = (Admin) afi.createUser("adminTest@admin.com", "adminTest", "adminTest", UserType.ADMIN).get();
			owner = (Owner) afi.createUser("ownerTest@gmail.com", "ownerTest", "ownerTest", UserType.OWNER).get();
			client = (Client) afi.createUser("clientTest@gamail.com", "clientTest", "clientTest", UserType.CLIENT).get();
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
		
		eventInvoked = false;
		
		startDate = null;
		endDate = null;
		offer = null;
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
		if (client != null) {
			afi.remove(client);
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
		if (offer != null) {
			afi.remove(offer);
		}
		if (booking != null) {
			afi.remove(booking);
		}
	}

	@DisplayName("Test Event - Method Invocation")
	@ParameterizedTest
	@CsvSource({"30, 10", "-2, 10", "11, 10"})
	void testEventMethodInvocation(int expected, int value, TestInfo testInfo) {

		try {
			rh.addOffer(createTestOffer(ruralHouse, firstDay, lastDay, value));
			rh.registerListener((optValue) -> {
				eventInvoked = true;
				assertAll("EventValues",
					() -> assertEquals(optValue.get(), value, () -> "New value missmatch in event invocation."),
					() -> assertEquals(optValue.get(), expected, () -> "Old value missmatch in event invocation.")
				);
			});

			observedValue.set(expected);

			assertTrue(eventInvoked, () -> "Event method not invoked.");
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getClass().getSimpleName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}
	}

	/**
	 * Create and return an <code>offer</code><p>
	 * Assumes that the created offer will not be <code>null</code> and that the offer 
	 * will not throw any exceptions.<br>
	 * If the assumption fails, the test that calls the function will also fail.
	 * 
	 * @param ruralHouse the rural house associated to the offer
	 * @param firstDay the start date of the offer
	 * @param lastDay the end date of the offer
	 * @param price the offer price per day
	 * @return the created <code>Offer</code>
	 */
	Offer createTestOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) {
		Offer offer = null;
		try {
			offer = afi.createOffer(ruralHouse, firstDay, lastDay, price);
		} catch (OverlappingOfferException e) {
			assumeNoException("Test aborted because of overlapping offers.", e);
		} catch (BadDatesException e) {
			assumeNoException("Test aborted because of bad dates.", e);
		} catch (Exception e) {
			assumeNoException("Test aborted because of unexpected exception.", e);
		}
		assumeNotNull(offer);
		return offer;
	}
	
	/**
	 * Parse <code>java.util.Date</code> to <code>java.time.LocalDate</code>.<p>
	 * Assumes that the parsed value will not be <code>null</code> and that the parse 
	 * will not throw any exceptions.<br>
	 * If the assumption fails, the test that calls the function will also fail.
	 * 
	 * @param localDate value to parse
	 * @return The date in <code>java.util.Date</code> type
	 */
	Date parseToDate(LocalDate localDate) {
		Date date = null;
		try {
			date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to parse date.", e);
		}
		assumeNotNull(date);
		return date;
	}
	
}
