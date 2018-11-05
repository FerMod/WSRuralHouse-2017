package domain.observer;

@FunctionalInterface
public interface ValueChangedListener {

	<T> void onValueChanged(T oldValue, T newValue);
	
}
