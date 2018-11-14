package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.jws.Oneway;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import businessLogic.ApplicationFacadeFactory;
import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import configuration.ConfigXML;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.ParticularClient;
import domain.RuralHouse;
import domain.TravelAgency;
import domain.UserType;
import domain.event.ValueAddedListener;
import domain.Review.ReviewState;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;

class EventPublisherValueTest {
	
	final static String TEST_DATA_FILE = "/test/data/CorrectDates.csv";
	
	static RuralHouse rh;
	static Admin admin;
	static ParticularClient particularClient;
	static TravelAgency travelAgency;
	static Owner owner;
	static City city;
	static ApplicationFacadeInterface afi;
	static SimpleDateFormat dateFormat;

	Date startDate;
	Date endDate;
	List<Offer> offers;
	Booking booking;
	double price;
	
	int invocationCount;
	boolean eventInvoked;
	
	@BeforeAll
	static void beforeAll() {
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
		
		eventInvoked = false;
		invocationCount = 0;
		
		startDate = null;
		endDate = null;
		offers = new ArrayList<>();
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
		for (Offer offer : offers) {
			afi.remove(offer);
		}
		if (booking != null) {
			afi.remove(booking);
		}
	}

	@DisplayName("Test Event - Method Invocation")
	@ParameterizedTest
	@CsvFileSource(resources = TEST_DATA_FILE, numLinesToSkip = 1)
	void testEventMethodInvocation(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2, TestInfo testInfo) {
		try {
			
			startDate = parseToDate(date1);
			endDate = parseToDate(date2);
			offers.add(createTestOffer(rh, startDate, endDate, price));
			
			rh.addOffer(offers.get(0));
			rh.registerListener((optValue) -> {
				eventInvoked = true;
				assertEquals(optValue.get(), offers.get(0), () -> "Value missmatch in event invocation.");
			});

			assertTrue(eventInvoked, () -> "Event method not invoked.");
			
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getClass().getSimpleName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		}
	}
	
	@DisplayName("Test Event - Multiple Invocations")
	@ParameterizedTest
	@CsvSource({
		"12/09/2018, 13/09/2018, 24/09/2018, 12/10/2018"
	})
	void testEventMultipleMethodInvocation(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1Offer1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2Offer1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1Offer2, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2Offer2, TestInfo testInfo) {
		try {
			
			int expectedInvocations = 2;

			startDate = parseToDate(date1Offer1);
			endDate = parseToDate(date2Offer1);			
			offers.add(createTestOffer(rh, startDate, endDate, price));
			
			startDate = parseToDate(date1Offer2);
			endDate = parseToDate(date2Offer2);
			offers.add(createTestOffer(rh, startDate, endDate, price));
			
			invocationCount = 0;
			rh.registerListener((optValue) -> {
				eventInvoked = true;
				assertEquals(optValue.get(), offers.get(invocationCount), () -> "Value missmatch in event invocation.");
				invocationCount++;
			});
			
			particularClient.enableOfferAlert(rh);
			travelAgency.enableOfferAlert(rh);
			
			for (Offer offer : offers) {
				rh.addOffer(offer);
			}

			assumeTrue(eventInvoked, () -> "Event method not invoked.");
			assertEquals(expectedInvocations, invocationCount, () -> "Unexpected ammount of method invocations made.");
			
		} catch (Exception e) {
			fail("Unexpected exception thrown in " + testInfo.getClass().getSimpleName() + "\n\tCase: " + testInfo.getDisplayName(), e);
		} finally {
			particularClient.disableOfferAlert(rh);
			travelAgency.disableOfferAlert(rh);
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
