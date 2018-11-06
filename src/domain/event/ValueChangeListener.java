package domain.event;

import java.io.Serializable;

@FunctionalInterface
public interface ValueChangeListener extends Serializable {

	void onValueChanged(ValueChangeEvent<?> event);
	
}
