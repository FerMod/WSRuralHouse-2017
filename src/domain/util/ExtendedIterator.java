package domain.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface ExtendedIterator<E> extends Iterator<E> {
	
	/**
	 * Returns {@code true} if the iteration has more elements.
	 * (In other words, returns {@code true} if {@link #previous} would
	 * return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements
	 */
	boolean hasPrevious();

	/**
	 * Returns the previous element in the iteration.
	 * 
	 * @return the previous element in the iteration
	 * @throws NoSuchElementException if the iteration has no more elements
	 */
	E previous();
	
	 /**
	 * Moves to the first element of the iterator
	 */
	void goFirst();
	
	/**
	 * Moves to the last element of the iterator
	 */
	void goLast();
	
}