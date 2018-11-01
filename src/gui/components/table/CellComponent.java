package gui.components.table;

public class CellComponent<T> {

	private CellComponentInterface cellComponentInterface;
	private T t;

	public CellComponent(T t) {
		this.t = t;
	}

	public CellComponentInterface getCellComponentTable(){
		return cellComponentInterface;
	}

	public void setCellComponentTable(CellComponentInterface cellComponentInterface) {
		this.cellComponentInterface = cellComponentInterface;
	}

	public T getElement() {
		return t;
	}

	public void setElement(T t) {
		this.t = t;
	}

}
