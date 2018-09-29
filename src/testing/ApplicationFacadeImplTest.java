package testing;

import static org.junit.Assert.*;
import org.junit.Test;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import domain.Booking;
import domain.City;
import domain.Client;
import domain.Offer;
import domain.Owner;
import domain.RuralHouse;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;

public class ApplicationFacadeImplTest {
	static RuralHouse rh;
	static Client client;
	static Owner ow;
	static City city;
	static ApplicationFacadeInterface afi;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() {
		afi = new ApplicationFacadeImpl();
		ow = afi.getRuralHouses().get(0).getOwner();
		city = afi.getRuralHouses().get(0).getCity();
		rh = afi.getRuralHouses().get(0);
		client = new Client("cliente002@gmail.com", "cliente002", "clien");
	}

	@Test
	public void testCreateBooking() {
		try {
			Date firstDay = dateFormat.parse("2018-09-24");
			Date lastDay = dateFormat.parse("2018-10-12");
			Offer o = new Offer(firstDay, lastDay, 550.0, rh);
			Booking esperado = new Booking(client, o, 550.0, firstDay, lastDay);
			Booking resultado = afi.createBooking(client, o, firstDay, lastDay);
			assertEquals(esperado, resultado);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCreateBooking2() {
		try {
			Date firstDay = dateFormat.parse("2018-09-24");
			Date lastDay = dateFormat.parse("2000-10-12");
			Offer o = new Offer(firstDay, lastDay, 550.0, rh);
			Booking esperado = new Booking(client, o, 550.0, firstDay, lastDay);
			Booking resultado = afi.createBooking(client, o, firstDay, lastDay);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreateOffer() throws OverlappingOfferException, BadDatesException {
		try {
			Date firstDay = dateFormat.parse("2018-09-24");
			Date lastDay = dateFormat.parse("2018-10-12");
			Offer esperado = new Offer(firstDay, lastDay, 550.0, rh);
			Offer resultado = afi.createOffer(rh, firstDay, lastDay, 550.0);
			assertEquals(esperado, resultado);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCreateOffer2() throws OverlappingOfferException, BadDatesException {
		try {
			Date firstDay = dateFormat.parse("2018-09-28");
			Date lastDay = dateFormat.parse("1997-10-14");
			Offer esperado = new Offer(firstDay, lastDay, 500.0, rh);
			Offer resultado = afi.createOffer(rh, firstDay, lastDay, 500.0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testCreateOffer3() throws OverlappingOfferException, BadDatesException {
		try {
			Date firstDay = dateFormat.parse("2018-09-29");
			Date lastDay = dateFormat.parse("2018-10-13");
			Offer esperado = new Offer(firstDay, lastDay, 320.0, rh);
			Offer resultado = afi.createOffer(rh, firstDay, lastDay, 320.0);
			assertTrue(true);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetOffer() {
		try {
			Date firstDay = dateFormat.parse("2018-09-24");
			Date lastDay = dateFormat.parse("2018-10-12");
			Offer esperado = new Offer(firstDay, lastDay, 550.0, rh);
			Offer resultado = afi.getOffer(rh, firstDay, lastDay).get(0);
			assertEquals(esperado, resultado);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testGetOffer1() {
		try {
			Date firstDay = dateFormat.parse("2018-09-10");
			Date lastDay = dateFormat.parse("1986-11-09");
			Offer esperado = new Offer(firstDay, lastDay, 550.0, rh);
			Offer resultado = afi.getOffer(rh, firstDay, lastDay).get(0);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@After
	public void tearDown() {
		try {
			Date firstDay = dateFormat.parse("2018-09-24");
			Date lastDay = dateFormat.parse("2018-10-12");
			Offer o = new Offer(firstDay, lastDay, 550.0, rh);
			afi.remove(new Booking(client, o, 550.0, firstDay, lastDay));
			
			afi.remove(new Offer(firstDay, lastDay, 550.0, rh));
		} catch (ParseException e) {

		}
	}
}
