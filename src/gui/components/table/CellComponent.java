package gui.components.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import gui.user.client.BookingsTablePanel;

public class CellComponent<T> implements PropertyChangeListener, Serializable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -6682766160415864152L;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private CellComponentInterface cellComponentInterface;
	private T t;

	public CellComponent(T t) {
		this.t = t;
		pcs.addPropertyChangeListener(this);
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

	 /**
     * {@inheritDoc}
     *
     * @param evt  {@inheritDoc}
     */
	@Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Name      = " + evt.getPropertyName());
        System.out.println("Old Value = " + evt.getOldValue());
        System.out.println("New Value = " + evt.getNewValue());
    }

}
