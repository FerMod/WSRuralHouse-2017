package test.util;

import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeNotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.TestInfo;

import businessLogic.ApplicationFacadeFactory;
import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import configuration.ConfigXML;
import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;

public final class TestUtilities {

	private static ApplicationFacadeInterface afi;

	private TestUtilities() {

	}
	
	/**
	 * Gets the application facade instance.
	 * If the application facade is not created, it will create one.
	 * 
	 * @return the application facade instance
	 */
	public static ApplicationFacadeInterface getApplicationFacadeInstance() {
		if(afi == null) {
			try {
				afi = ApplicationFacadeFactory.createApplicationFacade(ConfigXML.getInstance());
			} catch (Exception e) {
				System.err.println("An error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"");
				LogFile.log(e, true);
				e.printStackTrace();
			}

		}
		return afi;
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
	public static Offer createTestOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, double price) {
		Offer offer = null;
		try {
			offer = getApplicationFacadeInstance().createOffer(ruralHouse, firstDay, lastDay, price);
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
	public static Date parseToDate(LocalDate localDate) {
		Date date = null;
		try {
			date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		} catch (Exception e) {
			assumeNoException("Exception thrown when trying to parse date.", e);
		}
		assumeNotNull(date);
		return date;
	}
	
	/**
	 * Returns a formated error message with the exception information.
	 * 
	 * @param e the exception
	 * @param testInfo the <code>TestInfo</code> used, to inject information about the current test
	 * @return the formatted fail message
	 */
	public static String getFailMessage(Exception e, TestInfo testInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Unexpected exception thrown");
		if(testInfo.getTestMethod().isPresent()) {
			sb.append(" in " + testInfo.getTestMethod().get().getName());
		}
		sb.append("\n\tCase: " + testInfo.getDisplayName());
		return sb.toString();
	}

}
