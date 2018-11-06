package domain.observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import domain.event.ValueChangeEvent;
import domain.event.ValueChangeListener;

public class ObservedValue<T> implements Serializable {

	private transient final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	protected transient final Lock readLock = readWriteLock.readLock();
	protected transient final Lock writeLock = readWriteLock.writeLock();

	private transient List<ValueChangeListener> listeners;
	private Optional<T> value;

	public ObservedValue() {
		this(null);
	}

	public ObservedValue(T value) {
		listeners = new ArrayList<ValueChangeListener>();
		this.value = Optional.ofNullable(value);
	}

	public void set(T value) {		
		set(Optional.ofNullable(value));
	}

	public void set(Optional<T> value) {

		Optional<T> oldValue = this.value;
		this.value = value;

		// Notify the list of registered listeners
		this.notifyValueChanged(oldValue, value);

	}

	public T get() {
		return (T) value.orElse(null);

	}

	public Optional<T> getOptional() {
		return value;

	}

	public ValueChangeListener registerListener(ValueChangeListener listener) {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {

			// Add the listener to the list of registered listeners
			this.listeners.add(listener);

		} finally {

			// Unlock the writer lock
			this.writeLock.unlock();

		}

		return listener;
	}

	public void unregisterListener(ValueChangeListener listener) {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {

			// Remove the listener from the list of the registered listeners
			this.listeners.remove(listener);

		} finally {

			// Unlock the writer lock
			this.writeLock.unlock();

		}

	}

	public List<ValueChangeListener> getListeners() {
		return listeners;
	}

	public void notifyValueChanged(T oldValue, T newValue) {
		notifyValueChanged(Optional.ofNullable(oldValue), Optional.ofNullable(newValue));
	}

	public void notifyValueChanged(Optional<T> oldValue, Optional<T> newValue) {

		// Lock the list of listeners for reading
		this.readLock.lock();

		try {

			// Notify each of the listeners in the list of registered listeners
			this.listeners.forEach(listener -> {
				listener.onValueChanged(new ValueChangeEvent<>(oldValue, newValue));
			});

		} finally {

			// Unlock the reader lock
			this.readLock.unlock();

		}

	}


	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(value.orElse(null));
	}

	/**
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see Serializable
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.value = (Optional<T>) Optional.ofNullable(in.readObject());
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = 8159940159964193507L;

}
