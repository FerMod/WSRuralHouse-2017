package test.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Equals Test")
@Tag("equals")
public interface EqualsContract<T> extends Testable<T> {

    T createNotEqualValue();
    
    @DisplayName("Equals Itself - True")
    @Test
    default void valueEqualsItself() {
        T value = createValue();
        assertEquals(value, value);
    }
    
    @DisplayName("Equals Null - False")
    @Test
    default void valueDoesNotEqualNull() {
        T value = createValue();
        assertFalse(value.equals(null));
    }
    
    @DisplayName("Equals Different - False")
    @Test
    default void valueDoesNotEqualDifferentValue() {
        T value = createValue();
        T differentValue = createNotEqualValue();
        assertNotEquals(value, differentValue);
        assertNotEquals(differentValue, value);
    }

}
