package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import configuration.util.CurrencyUtils;

class CurrencyUtilsTest {

	static CurrencyUtils currencyUtils;

	@BeforeAll
	static void beforeAll() {
		currencyUtils = CurrencyUtils.getInstance();
	}

	@ParameterizedTest
    @CsvSource({"$, en", "€, es", "€, eus"})
	void testCurrencySymbol(String expectedValue, Locale locale) {
		try {
			
			System.out.println(locale.getDisplayCountry());
			String actualValue = currencyUtils.getCurrencySymbol(locale);
			assertEquals(expectedValue, actualValue, () -> "The currency symbol {0} does not match with {1}");
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception thrown when trying to get currency symbol.", e);
		}
	}



}
