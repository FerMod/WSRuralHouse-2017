package domain.util;

import java.util.ArrayList;

import domain.RuralHouse;

public class ExtendedItRhs implements ExtendedIterator<RuralHouse> {
	
	public ArrayList<RuralHouse> rhs;
	public int i = 0;
	
	public ExtendedItRhs(ArrayList<RuralHouse> rhs) {
		this.rhs = rhs;
	}

	@Override
	public boolean hasNext() {
		return i <= rhs.size()-1;
	}

	@Override
	public RuralHouse next() {
		RuralHouse rh = rhs.get(i);
		i++;
		return rh;
	}

	@Override
	public RuralHouse previous() {
		RuralHouse rh = rhs.get(i);
		i--;
		return rh;
	}

	@Override
	public boolean hasPrevious() {
		return i >= 0;
	}

	@Override
	public void goFirst() {
		i = 0;
	}

	@Override
	public void goLast() {
		i = rhs.size()-1;
	}

}
