package domain.util;


import java.util.Vector;

public class RuralHouseExtIt<RuralHouse> implements ExtendedIterator {
	
	public Vector<RuralHouse> rhs;
	public int index;
	
	public RuralHouseExtIt() {
		rhs = new Vector<RuralHouse>();
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return index <= rhs.size()-1;
	}

	@Override
	public RuralHouse next() {
		RuralHouse rh = rhs.get(index);
		index = index + 1;
		return rh;
	}

	@Override
	public RuralHouse previous() {
		RuralHouse rh = rhs.get(index);
		index = index - 1;
		return rh;
	}

	@Override
	public boolean hasPrevious() {
		return index-1 >= 0;
	}

	@Override
	public void goFirst() {
		index = 0;
	}

	@Override
	public void goLast() {
		index = rhs.size()-1;
	}

}
