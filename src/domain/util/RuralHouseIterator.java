package domain.util;

import java.util.List;
import java.util.NoSuchElementException;

import domain.RuralHouse;

public class RuralHouseIterator implements ExtendedIterator<RuralHouse> {

	private List<RuralHouse> ruralHouseList;
	private int index = 0;

	public RuralHouseIterator(List<RuralHouse> ruralHouseList) {
		this.ruralHouseList = ruralHouseList;
	}

	@Override
	public boolean hasPrevious() {
		return index >= 0;
	}

	@Override
	public boolean hasNext() {
		return index <= ruralHouseList.size()-1;
	}

	@Override
	public RuralHouse previous() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		return ruralHouseList.get(index--);
	}

	@Override
	public RuralHouse next() {
		if(!hasNext()) {
			throw new NoSuchElementException();
		}
		return ruralHouseList.get(index++);
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
