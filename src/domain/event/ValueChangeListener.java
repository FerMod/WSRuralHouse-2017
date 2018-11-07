package domain.event;

import java.util.Optional;

@FunctionalInterface
public interface ValueChangeListener {

	void onValueChanged(Optional<?> oldValue, Optional<?> newValue);
	
}
