package domain.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public abstract class EventPublisher<T> {

	private transient final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
	protected transient final Lock readLock = readWriteLock.readLock();
	protected transient final Lock writeLock = readWriteLock.writeLock();

	private transient List<T> listeners = new ArrayList<>();

	public T registerListener(T listener) {

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

	public void unregisterListener(T listener) {

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
	
	public void unregisterAllListeners() {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {
			// Remove all the listeners from the list of the registered listeners
			this.listeners.clear();
		} finally {
			// Unlock the writer lock
			this.writeLock.unlock();
		}

	}

	public void notifyListeners(Consumer<? super T> action) {

		// Lock the list of listeners for writing
		this.writeLock.lock();

		try {
			// Execute some function on each of the listeners
			this.listeners.forEach(action);
		} finally {
			// Unlock the writer lock
			this.writeLock.unlock();
		}

	}

}
