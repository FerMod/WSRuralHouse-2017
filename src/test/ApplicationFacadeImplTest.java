package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.CsvFileSource;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.AbstractUser.Role;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;

class ApplicationFacadeImplTest {

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

	@BeforeAll
	static void beforeAll() {

		LogFile.FILE_NAME = "JUnitTest.log";

		initDataBase();
		createTestData();

	}

	static void initDataBase() {
		try {

			ConfigXML config = ConfigXML.getInstance();
			Locale.setDefault(new Locale(config.getLocale()));

			if (config.isBusinessLogicLocal()) {
				afi = new ApplicationFacadeImpl();
				DataAccess dataAccess = new DataAccess();
				afi.setDataAccess(dataAccess);
			} else {

				String serviceName = "http://" + config.getBusinessLogicNode() + ":" + config.getBusinessLogicPort() + "/ws/" + config.getBusinessLogicName() + "?wsdl";
				URL url = new URL(serviceName);

				//1st argument refers to wsdl document above
				//2nd argument is the service name, refers to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");
				Service service = Service.create(url, qname);
				afi = service.getPort(ApplicationFacadeInterface.class);
			}

		} catch (Exception e) {
			LogFile.log(e, true);
			System.err.println("Test aborted, because an error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"");
			e.printStackTrace();
			assumeNoException(e);
		}
	}

	static void createTestData() {
		try {			

			dateFormat = new SimpleDateFormat("yyyy/MM/dd");

			admin = (Admin)afi.createUser("adminTest@admin.com", "adminTest", "adminTest", Role.ADMIN);
			owner = (Owner)afi.createUser("ownerTest@gmail.com", "ownerTest", "ownerTest", Role.OWNER);
			client = (Client)afi.createUser("clientTest@gamail.com", "clientTest", "clientTest", Role.CLIENT);
			city = afi.createCity("TestCity");

			rh = afi.createRuralHouse(owner, "Casa Test", "Descripción Test", city, "Calle Test / 12Test");
			rh.getReview().setState(admin, ReviewState.APPROVED);
			afi.update(rh);

		} catch (Exception e) {
			assumeNoException("Exception raised when creating the test data.", e);
		}		
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

	@BeforeEach
	void beforeEach() {
		startDate = null;
		endDate = null;
		offer = null;
		booking = null;
		price = 550.0;
	}

	@Nested
	@DisplayName("Offer Test")
	class OfferTest {

		@ParameterizedTest
		@DisplayName("CreateOffer - Correct Creation")
		@CsvFileSource(resources = "/test/data/CorrectDates.csv", numLinesToSkip = 1)
		void testCreateOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);
				offer = createTestOffer(rh, startDate, endDate, price);
				assertNotNull(offer);
			} catch (Exception e) {
				fail("Exception thrown when trying to create offer.", e);
			}
		}

		@Test
		@DisplayName("CreateOffer - OverlappingOfferException")
		void testCreateOffer2() {
			try {
				startDate = parseToDate(LocalDate.of(2018, 9, 24));
				endDate = parseToDate(LocalDate.of(2018, 10, 12));
				offer = createTestOffer(rh, startDate, endDate, price);
				assumeNotNull(offer);

				startDate = parseToDate(LocalDate.of(2018, 9, 24));
				endDate = parseToDate(LocalDate.of(2018, 10, 12));
				assertThrows(OverlappingOfferException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
			} catch (Exception e) {
				fail("Expected exceptions.OverlappingOfferException but got " + e.getClass().getCanonicalName(), e);
			}
		}

		@ParameterizedTest
		@DisplayName("CreateOffer - BadDatesException")
		@CsvFileSource(resources = "/test/data/BadDates.csv", numLinesToSkip = 1)
		void testCreateOffer3(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);
				assertThrows(BadDatesException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
			} catch (Exception e) {
				fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
			}
		}

		@ParameterizedTest
		@DisplayName("GetOffer - Get Correct Value")
		@CsvFileSource(resources = "/test/data/CorrectDates.csv", numLinesToSkip = 1)
		void testGetOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);

				offer = createTestOffer(rh, startDate, endDate, price);

				Offer expected = afi.find(Offer.class, offer.getId());
				assumeTrue(expected.equals(offer), "Assumption is not true");

				Offer obtained = afi.getOffers(rh, startDate, endDate).get(0);			
				assertEquals(expected, obtained);
			} catch (Exception e) {
				fail("Exception thrown when trying to get offer.", e);
			}
		}

		@ParameterizedTest
		@DisplayName("GetOffer - BadDatesException")
		@CsvFileSource(resources = "/test/data/BadDates.csv", numLinesToSkip = 1)
		void testGetOffer1(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);

				assertAll("Offer BadDatesExceptions",
					() -> assertThrows(BadDatesException.class, () -> {
						offer = afi.createOffer(rh, startDate, endDate, price);
					}),
					() -> assertThrows(BadDatesException.class, () -> {
						assertNotNull(afi.getOffers(rh, startDate, endDate).get(0));
					})
				);
			} catch (Exception e) {
				fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
			}
		}

		@ParameterizedTest
		@DisplayName("Delete Offer - Correct Deletion")
		@CsvFileSource(resources = "/test/data/CorrectDates.csv", numLinesToSkip = 1)
		void testDeleteOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);

				int currentOffers = afi.getOfferCount();
				offer = createTestOffer(rh, startDate, endDate, price);
				currentOffers++;
				assertEquals(currentOffers, afi.getOfferCount());

				afi.remove(Offer.class, offer.getId());
				currentOffers--;
				assertEquals(currentOffers, afi.getOfferCount());

				offer = createTestOffer(rh, startDate, endDate, price);
				currentOffers++;
				assertEquals(currentOffers, afi.getOfferCount());

				assertNotNull(offer);
			} catch (Exception e) {
				fail("Exception thrown when test the deletion of an offer.", e);
			}
		}

	}

	@Nested
	@DisplayName("Booking Test")
	class BookingTest {

		@ParameterizedTest
		@DisplayName("CreateBooking - Correct Creation")
		@CsvFileSource(resources = "/test/data/CorrectDates.csv", numLinesToSkip = 1)
		void testCreateBooking(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);

				offer = createTestOffer(rh, startDate, endDate, price);
				booking = afi.createBooking(client, offer, startDate, endDate);

				assertNotNull(booking);
			} catch (Exception e) {
				fail("Exception thrown when trying to create booking.", e);
			}
		}

		@ParameterizedTest
		@DisplayName("CreateBooking - BadDatesException")
		@CsvFileSource(resources = "/test/data/BadDates.csv", numLinesToSkip = 1)
		void testCreateBooking2(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);

				try {
					// This should throw an exception later on, but we are not test that right now
					offer = new Offer(startDate, endDate, price, rh);
				} catch (Exception e) {
					assumeNoException(e);
				}
				assumeNotNull(offer);

				assertThrows(BadDatesException.class, () -> booking = afi.createBooking(client, offer, startDate, endDate));
			} catch (Exception e) {
				fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
			}		
		}

		@ParameterizedTest
		@DisplayName("Delete Booking - Correct Deletion")
		@CsvFileSource(resources = "/test/data/CorrectDates.csv", numLinesToSkip = 1)
		void testDeleteBooking(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = parseToDate(date1);
				endDate = parseToDate(date2);
				offer = createTestOffer(rh, startDate, endDate, price);

				int currentBookings = afi.getBookings(client).size();
				booking = afi.createBooking(client, offer, startDate, endDate);
				currentBookings++;
				assertEquals(currentBookings, afi.getBookings(client).size());

				afi.remove(Booking.class, booking.getId());
				currentBookings--;
				assertEquals(currentBookings, afi.getBookings(client).size());

				booking = afi.createBooking(client, offer, startDate, endDate);
				currentBookings++;
				assertEquals(currentBookings, afi.getBookings(client).size());

				assertNotNull(booking);
			} catch (Exception e) {
				fail("Exception thrown when test the deletion of a booking.", e);
			}
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
