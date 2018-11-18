package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.Date;

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

import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import domain.Admin;
import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.Review.ReviewState;
import domain.RuralHouse;
import domain.UserType;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;
import test.util.TestUtilities;

class ApplicationFacadeImplTest {

	final static String BAD_DATES_FILE = "/test/data/BadDates.csv";
	final static String CORRECT_DATES_FILE = "/test/data/CorrectDates.csv";

	static RuralHouse rh;
	static Admin admin;
	static Client client;
	static Owner owner;
	static City city;
	static ApplicationFacadeInterface afi;

	Date startDate;
	Date endDate;
	Offer offer;
	Booking booking;
	double price;

	@BeforeAll
	static void beforeAll() {

		LogFile.FILE_NAME = "JUnitTest.log";
		afi = TestUtilities.getApplicationFacadeInstance();
		createTestData();

	}

	static void createTestData() {
		try {

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
		@CsvFileSource(resources = CORRECT_DATES_FILE, numLinesToSkip = 1)
		void testCreateOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);
				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
				assertNotNull(offer);
			} catch (Exception e) {
				fail("Exception thrown when trying to create offer.", e);
			}
		}

		@Test
		@DisplayName("CreateOffer - OverlappingOfferException")
		void testCreateOffer2() {
			try {
				startDate = TestUtilities.parseToDate(LocalDate.of(2018, 9, 24));
				endDate = TestUtilities.parseToDate(LocalDate.of(2018, 10, 12));
				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
				assumeNotNull(offer);

				startDate = TestUtilities.parseToDate(LocalDate.of(2018, 9, 24));
				endDate = TestUtilities.parseToDate(LocalDate.of(2018, 10, 12));
				assertThrows(OverlappingOfferException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
			} catch (Exception e) {
				fail("Expected exceptions.OverlappingOfferException but got " + e.getClass().getCanonicalName(), e);
			}
		}

		@ParameterizedTest
		@DisplayName("CreateOffer - BadDatesException")
		@CsvFileSource(resources = BAD_DATES_FILE, numLinesToSkip = 1)
		void testCreateOffer3(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);
				assertThrows(BadDatesException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
			} catch (Exception e) {
				fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
			}
		}

		@ParameterizedTest
		@DisplayName("GetOffer - Get Correct Value")
		@CsvFileSource(resources = CORRECT_DATES_FILE, numLinesToSkip = 1)
		void testGetOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);

				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);

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
		@CsvFileSource(resources = BAD_DATES_FILE, numLinesToSkip = 1)
		void testGetOffer1(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);

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
		@CsvFileSource(resources = CORRECT_DATES_FILE, numLinesToSkip = 1)
		void testDeleteOffer(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);

				int currentOffers = afi.getOfferCount();
				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
				currentOffers++;
				assertEquals(currentOffers, afi.getOfferCount());

				afi.remove(Offer.class, offer.getId());
				currentOffers--;
				assertEquals(currentOffers, afi.getOfferCount());

				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
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
		@CsvFileSource(resources = CORRECT_DATES_FILE, numLinesToSkip = 1)
		void testCreateBooking(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);

				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);
				booking = afi.createBooking(client, offer, startDate, endDate);

				assertNotNull(booking);
			} catch (Exception e) {
				fail("Exception thrown when trying to create booking.", e);
			}
		}

		@ParameterizedTest
		@DisplayName("CreateBooking - BadDatesException")
		@CsvFileSource(resources = BAD_DATES_FILE, numLinesToSkip = 1)
		void testCreateBooking2(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);

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
		@CsvFileSource(resources = CORRECT_DATES_FILE, numLinesToSkip = 1)
		void testDeleteBooking(@JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date1, @JavaTimeConversionPattern("dd/MM/yyyy") LocalDate date2) {
			try {
				startDate = TestUtilities.parseToDate(date1);
				endDate = TestUtilities.parseToDate(date2);
				offer = TestUtilities.createTestOffer(rh, startDate, endDate, price);

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

}
