package domain.event;

import java.io.Serializable;
import java.util.Optional;

public class ValueChangeEvent<T> implements Serializable {

	private transient Optional<T> oldValue;
	private transient Optional<T> newValue;
	
	public ValueChangeEvent(Optional<T> oldValue, Optional<T> newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Optional<T> getOldValue() {
		return oldValue;
	}

	public Optional<T> getNewValue() {
		return newValue;
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = -8830482684233396228L;

}
