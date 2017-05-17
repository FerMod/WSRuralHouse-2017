package gui.components.component.table;

import gui.ClientTableCell;

public class CellComponent<T> {

	private ClientTableCell tableCellComponent;
	private T t;
	
	public CellComponent(T t) {
		this.t = t;
	}

	public CellComponentInterface getTableCellComponent() {
		return tableCellComponent;
	}

	public void setTableCellComponent(ClientTableCell tableCellComponent) {
		this.tableCellComponent = tableCellComponent;
	}

	public T getElement() {
		return t;
	}

	public void setElement(T t) {
		this.t = t;
	}
	
}
