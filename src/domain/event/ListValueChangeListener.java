package domain.event;

public interface ListValueChangeListener {

	default void onValueAdded(Object obj) {}
	default void onValueRemoved(Object obj) {}
	default void onMultipleValuesAdded(Object[] objArray) {}
	default void onMultipleValuesRemoved(Object[] objArray) {}

}
