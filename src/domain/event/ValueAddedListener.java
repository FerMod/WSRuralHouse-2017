package domain.event;

import java.util.Optional;

@FunctionalInterface
public interface ValueAddedListener {

	void onValueAdded(Optional<?> newValue);
	
}
