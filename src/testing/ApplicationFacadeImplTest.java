package testing;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
import exceptions.DuplicatedEntityException;
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
	static void initAll() {

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
				//2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");
				Service service = Service.create(url, qname);
				afi = service.getPort(ApplicationFacadeInterface.class);
			}

		} catch (Exception e) {
			System.err.println("An error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"\nTo show the console output press \"F12\"");
			e.printStackTrace();
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

		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
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
	void tearDown() {
		if (offer != null) {
			afi.remove(offer);
		}
		if (booking != null) {
			afi.remove(booking);
		}
	}

	@BeforeEach
	void restoreValues() {
		startDate = null;
		endDate = null;
		offer = null;
		booking = null;
		price = 550.0;
	}

	@ParameterizedTest
	@DisplayName("Create/Delete Offer - Correct Creation/Deletion")
	@CsvFileSource(resources = "/testing/CorrectDates.csv")
	void testCreateDeleteOffer(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);

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
			assumeNoException("Exception thrown when testing the creation\\deletion of an offer.", e);
		}
	}
	
	@ParameterizedTest
	@DisplayName("CreateBooking - Correct Creation")
	@CsvFileSource(resources = "/testing/CorrectDates.csv")
	void testCreateBooking(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);

			offer = createTestOffer(rh, startDate, endDate, price);
			booking = afi.createBooking(client, offer, startDate, endDate);

			assertNotNull(booking);
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to create booking.", e);
		}
	}
	
	@ParameterizedTest
	@DisplayName("CreateBooking - BadDatesException")
	@CsvFileSource(resources = "/testing/BadDates.csv")
	void testCreateBooking2(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);

			// This should throw an exception later on, but we are not testing that right now
			offer = new Offer(startDate, endDate, price, rh);

			assertThrows(BadDatesException.class, () -> booking = afi.createBooking(client, offer, startDate, endDate));
		} catch (Exception e) {
			fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
		}		
	}

	@ParameterizedTest
	@DisplayName("CreateOffer - Correct Creation")
	@CsvFileSource(resources = "/testing/CorrectDates.csv")
	void testCreateOffer(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);
			offer = createTestOffer(rh, startDate, endDate, price);
			assertNotNull(offer);
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to create offer.", e);
		}
	}

	@Test
	@DisplayName("CreateOffer - OverlappingOfferException")
	void testCreateOffer2() {
		try {
			startDate = parseDate("2018/09/24");
			endDate = parseDate("2018/10/12");
			offer = createTestOffer(rh, startDate, endDate, price);
			assertNotNull(offer);

			startDate = parseDate("2018/09/24");
			endDate = parseDate("2018/10/12");
			assertThrows(OverlappingOfferException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
		} catch (Exception e) {
			fail("Expected exceptions.OverlappingOfferException but got " + e.getClass().getCanonicalName(), e);
		}
	}

	@ParameterizedTest
	@DisplayName("CreateOffer - BadDatesException")
	@CsvFileSource(resources = "/testing/BadDates.csv")
	void testCreateOffer3(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);
			assertThrows(BadDatesException.class, () -> offer = afi.createOffer(rh, startDate, endDate, price));
		} catch (Exception e) {
			fail("Expected exceptions.BadDatesException but got " + e.getClass().getCanonicalName(), e);
		}
	}

	@ParameterizedTest
	@DisplayName("GetOffer - Get Correct Value")
	@CsvFileSource(resources = "/testing/CorrectDates.csv")
	void testGetOffer(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);

			offer = createTestOffer(rh, startDate, endDate, price);

			Offer expected = afi.find(Offer.class, offer.getId());
			assumeTrue(expected.equals(offer), "Assumption is not true");

			Offer obtained = afi.getOffers(rh, startDate, endDate).get(0);			
			assertEquals(expected, obtained);
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to get offer.", e);
		}
	}

	@ParameterizedTest
	@DisplayName("GetOffer - BadDatesException")
	@CsvFileSource(resources = "/testing/BadDates.csv")
	void testGetOffer1(String date1, String date2) {
		try {
			startDate = parseDate(date1);
			endDate = parseDate(date2);

			assertAll("Offer BadDatesExceptions",
				() -> assertThrows(BadDatesException.class, () -> {
					offer = afi.createOffer(rh, startDate, endDate, price);
				}),
				() -> assertThrows(BadDatesException.class, () -> {
					assertNotNull(afi.getOffers(rh, startDate, endDate).get(0));
				})
			);
		} catch (Exception e) {
			fail("Instead of BadDatesException another different has thrown.", e);
		}
	}

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

	Date parseDate(String dateString) {
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			assumeNoException("Exception thrown when trying to parse date.", e);
		}
		assumeNotNull(date);
		return date;
	}

}
