package test;

import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import businessLogic.ApplicationFacadeInterface;
import domain.Admin;
import domain.City;
import domain.Owner;
import domain.Review.ReviewState;
import domain.util.ExtendedIterator;
import domain.RuralHouse;
import domain.UserType;
import test.util.TestUtilities;

class ExtendedIteratorTest {

	static final int MIN_TEST_SAMPLES = 2;
	static final int MAX_TEST_SAMPLES = 10;
	static List<RuralHouse> ruralHouseList;
	static  List<City> cityList;
	static Admin admin;
	static Owner owner;
	static ApplicationFacadeInterface afi;

	ExtendedIterator<RuralHouse> it;

	@BeforeAll
	static void beforeAll() {
		afi = TestUtilities.getApplicationFacadeInstance();
		createTestData();
	}

	static void createTestData() {
		try {

			admin = (Admin) afi.createUser("adminTest@admin.com", "adminTest", "adminTest", UserType.ADMIN).get();
			owner = (Owner) afi.createUser("ownerTest@gmail.com", "ownerTest", "ownerTest", UserType.OWNER).get();

			int testSampleSize = ThreadLocalRandom.current().nextInt(MIN_TEST_SAMPLES, MAX_TEST_SAMPLES + 1);
			ruralHouseList = new ArrayList<>(testSampleSize);
			cityList = new ArrayList<>(testSampleSize);
			for (int i = 0; i < testSampleSize; i++) {

				City city = afi.createCity("TestCity " + i);
				RuralHouse rh = afi.createRuralHouse(owner, "Casa " + i, "Descripcion " + i, city, "TestAddres " + i);
				rh.getReview().setState(admin, ReviewState.APPROVED);
				afi.update(rh);

				cityList.add(city);
				ruralHouseList.add(rh);

			}
			assumeTrue(ruralHouseList.size() == testSampleSize, () -> "Assumed list size of " + testSampleSize + ", but got " + ruralHouseList.size());

		} catch (Exception e) {
			assumeNoException("Exception raised when creating the test data.", e);
		}		
	}

	@BeforeEach
	void beforeEach() {
		it = afi.ruralHouseIterator();
	}

	@AfterAll
	static void afterAll() {
		for (RuralHouse ruralHouse : ruralHouseList) {
			afi.remove(ruralHouse);
		}
		for (City city : cityList) {
			afi.remove(city);
		}
		if (admin != null) {
			afi.remove(admin);
		}
		if (owner != null) {
			afi.remove(owner);
		}
	}

	@DisplayName("HasPrevious - True")
	void testHasPreviousTrue(TestInfo testInfo) {
		try {

			it.goLast();
			assertTrue(it.hasPrevious(), () -> "Returned wrong value.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("HasPrevious - False")
	void testHasPreviousFalse(TestInfo testInfo) {
		try {

			it.goFirst();
			assertFalse(it.hasPrevious(), () -> "Returned wrong value.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("HasNext - True")
	void testHasNextTrue(TestInfo testInfo) {
		try {

			it.goFirst();
			assertTrue(it.hasNext(), () -> "Returned wrong value.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("HasNext - False")
	void testHasNextFalse(TestInfo testInfo) {
		try {

			it.goFirst();
			assertFalse(it.hasNext(), () -> "Returned wrong value.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("Previous - Return Previous Element")
	@Test
	void testPrevious(TestInfo testInfo) {
		try {

			RuralHouse expected = ruralHouseList.get(0);
			assertEquals(expected, it.previous(), () -> "Previous element not equal.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("Previous - NoSuchElementException")
	@Test
	void testPreviousNoSuchElementException() {
		try {

			try {
				it.previous();
			} catch (Exception e) {
				assumeNoException("Assumed no exception but unexpected exception has thrown.", e);
			}

			assertThrows(NoSuchElementException.class, () -> it.previous());

		} catch (Exception e) {
			fail("Expected java.util.NoSuchElementException but got " + e.getClass().getCanonicalName(), e);
		}
	}

	@DisplayName("Next - Return Next Element")
	@Test
	void testNext(TestInfo testInfo) {
		try {

			RuralHouse expected = ruralHouseList.get(0);
			assertEquals(expected, it.next(), () -> "Previous element not equal.");

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("Next - NoSuchElementException")
	@Test
	void testNextNoSuchElementException() {
		try {

			it.goLast();
			try {
				it.next();
			} catch (Exception e) {
				assumeNoException("Assumed no exception but unexpected exception has thrown.", e);
			}

			assertThrows(NoSuchElementException.class, () -> it.next());

		} catch (Exception e) {
			fail("Expected java.util.NoSuchElementException but got " + e.getClass().getCanonicalName(), e);
		}
	}

	@DisplayName("GoFirst - Move to First Element")
	@Test
	void testGoFirst(TestInfo testInfo) {
		try {

			it.goFirst();
			try {
				it.previous();
			} catch (Exception e) {
				assumeNoException("Assumed no exception but unexpected exception has thrown.", e);
			}

			assertAll("GoFirst",
				() -> assertTrue(it.hasNext(), () -> "Not moved correctly to first element. Has Next"),
				() -> assertFalse(it.hasPrevious(), () -> "Not moved correctly to first element. Has Previous")
			);

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("GoLast - Move to Last Element")
	@Test
	void testGoLast(TestInfo testInfo) {
		try {

			it.goLast();
			try {
				it.next();
			} catch (Exception e) {
				assumeNoException("Assumed no exception but unexpected exception has thrown.", e);
			}

			assertAll("GoLast",
				() -> assertFalse(it.hasNext(), () -> "Not moved correctly to last element. Has Next"),
				() -> assertTrue(it.hasPrevious(), () -> "Not moved correctly to last element. Has Previous")
			);

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("Element Iteration - Correct Element Iteration")
	@Test
	void testCorrectOrder(TestInfo testInfo) {
		try {

			List<RuralHouse> resultRuralHouse = new ArrayList<>();

			it.goFirst();
			while (it.hasNext()){
				resultRuralHouse.add(it.next());
			}

			assertEquals(ruralHouseList, resultRuralHouse);

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

	@DisplayName("Element Iteration - Correct Inverse Element Iteration")
	@Test
	void testCorrectInverseOrder(TestInfo testInfo) {
		try {

			List<RuralHouse> resultRuralHouse = new ArrayList<>();
			List<RuralHouse> expectedList = new ArrayList<>(ruralHouseList);
			Collections.reverse(expectedList);

			it.goLast();
			while (it.hasPrevious()){
				resultRuralHouse.add(it.previous());
			}		

			assertEquals(expectedList, resultRuralHouse);

		} catch (Exception e) {
			fail(TestUtilities.getFailMessage(e, testInfo), e);
		}	
	}

}
