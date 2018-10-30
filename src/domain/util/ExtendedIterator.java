package domain.util;

import java.util.Iterator;

public interface ExtendedIterator extends Iterator {
	
	//devuelve el elemento actual y pasa al anterior
	public Object previous();
	
	//true si existe el elemento anterior
	public boolean hasPrevious();
	
	//Se posiciona en el primer elemento
	public void goFirst();
	
	//Se posiciona en el último elemento
	public void goLast();
	
}
