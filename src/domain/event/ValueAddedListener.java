package domain.event;

import java.util.Optional;

@FunctionalInterface
public interface ValueAddedListener<T> {

	void onValueAdded(Optional<T> optValue);
	
}
