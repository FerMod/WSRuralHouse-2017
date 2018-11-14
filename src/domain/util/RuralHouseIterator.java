package domain.util;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;

import domain.RuralHouse;

public class RuralHouseIterator implements ExtendedIterator<RuralHouse> {

	private List<RuralHouse> ruralHouseList;
	private int index = 0;

	public RuralHouseIterator(Vector<RuralHouse> ruralHouseList) {
		this(new ArrayList<>(ruralHouseList));
	}

	public RuralHouseIterator(List<RuralHouse> ruralHouseList) {
		this.ruralHouseList = ruralHouseList;
	}

	@Override
	public boolean hasNext() {
		return index <= ruralHouseList.size()-1;
	}

	@Override
	public RuralHouse next() {
		RuralHouse ruralHouse = ruralHouseList.get(index);
		index++;
		return ruralHouse;
	}

	@Override
	public RuralHouse previous() {
		if (index < 0) {
            throw new ConcurrentModificationException();
		}
		RuralHouse ruralHouse = ruralHouseList.get(index);
		index--;
		return ruralHouse;
	}

	@Override
	public boolean hasPrevious() {
		return index >= 0;
	}

	@Override
	public void goFirst() {
		index = 0;
	}

	@Override
	public void goLast() {
		index = ruralHouseList.size()-1;
	}

}
