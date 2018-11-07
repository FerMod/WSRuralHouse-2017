package domain.observer;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import domain.event.EventListeners;
import domain.event.ListValueChangeListener;

public class ObservedList<E> extends EventListeners<ListValueChangeListener> implements Collection<E> {

	private Collection<E> collection;

	public ObservedList(Collection<E> collection) {
		this.collection = collection;
	}

	@Override
	public int size() {
		return collection.size();
	}

	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return collection.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return collection.iterator();
	}

	@Override
	public Object[] toArray() {
		return collection.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return collection.toArray(a);
	}

	@Override
	public boolean add(E e) {
		boolean added = collection.add(e);
		this.notifyListeners((listener) -> listener.onValueAdded(e));
		return added;
	}

	@Override
	public boolean remove(Object o) {
		boolean removed = collection.remove(o);
		this.notifyListeners((listener) -> listener.onValueRemoved(o));
		return removed;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean added = collection.addAll(c);
		this.notifyListeners((listener) -> listener.onMultipleValuesAdded(c.toArray()));
		return added;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = collection.remove(c);
		this.notifyListeners((listener) -> listener.onMultipleValuesRemoved(c.toArray()));
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = collection.retainAll(c);
		this.notifyListeners((listener) -> listener.onMultipleValuesRemoved(collection.stream().filter(e -> !c.contains(e)).toArray()));
		return changed;
	}

	@Override
	public void clear() {
		this.notifyListeners((listener) -> listener.onMultipleValuesRemoved(collection.toArray()));
		collection.clear();
	}

}
