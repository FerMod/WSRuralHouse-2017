package domain.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ValueChangedListenerTest<T> {

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	protected final Lock readLock = readWriteLock.readLock();
	protected final Lock writeLock = readWriteLock.writeLock();

	private T value;
	private List<ValueChangedListener> listeners = new ArrayList<>();

	public void add(T value) {

		T oldValue = this.value;
		this.value = value;

		// Notify the list of registered listeners
		this.notifyValueChanged(oldValue, value);

	}

	public ValueChangedListener registerListener(ValueChangedListener listener) {

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

	public void unregisterListener(ValueChangedListener listener) {

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

	public void notifyValueChanged(T oldValue, T newValue) {

		// Lock the list of listeners for reading
		this.readLock.lock();

		try {

			// Notify each of the listeners in the list of registered listeners
			this.listeners.forEach(listener -> listener.onValueChanged(oldValue, newValue));

		}

		finally {

			// Unlock the reader lock
			this.readLock.unlock();

		}

	}

}
