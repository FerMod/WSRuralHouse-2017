package test.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Comparable Test")
@Tag("comparable")
public interface ComparableContract<T extends Comparable<T>> extends Testable<T> {

	T createSmallerValue();

	@DisplayName("Compare Itself - Return Zero")
	@Test
	default void returnsZeroWhenComparedToItself() {
		T value = createValue();
		assertEquals(0, value.compareTo(value));
	}

	@DisplayName("Compare To Smaller - Return Positive Number")
	@Test
	default void returnsPositiveNumberWhenComparedToSmallerValue() {
		T value = createValue();
		T smallerValue = createSmallerValue();
		assertTrue(value.compareTo(smallerValue) > 0);
	}

	@DisplayName("Compare To Larger - Return Negative Number")
	@Test
	default void returnsNegativeNumberWhenComparedToLargerValue() {
		T value = createValue();
		T smallerValue = createSmallerValue();
		assertTrue(smallerValue.compareTo(value) < 0);
	}

}
