package gui.components;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import gui.components.table.CustomTableModel;

public class CustomTable extends JTable implements PropertyChangeListener {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -5867408116296077885L;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private int rollOverRowIndex = -1;

	public CustomTable(CustomTableModel model) {
		super(model);
		RollOverListener rollOverListener = new RollOverListener();
		addMouseMotionListener(rollOverListener);
		addMouseListener(rollOverListener);
		pcs.addPropertyChangeListener(this);
	}	

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component component = super.prepareRenderer(renderer, row, column);
		if(isRowSelected(row) || (row == rollOverRowIndex)) {
			component.setForeground(getSelectionForeground());
			component.setBackground(getSelectionBackground());
		} else {
			component.setForeground(getForeground());
			component.setBackground(getBackground());
		}
		return component;
	}
	
	public void removeRow(int index) {
		((CustomTableModel)getModel()).getTableData().remove(index);
	}
	
	public void addRow(Object[] row) {
		((CustomTableModel)getModel()).getTableData().add(row);
	}

	private class RollOverListener extends MouseInputAdapter {

		@Override
		public void mouseExited(MouseEvent e) {
			rollOverRowIndex = -1;
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int row = rowAtPoint(e.getPoint());
			if(row != rollOverRowIndex) {
				rollOverRowIndex = row;
				repaint();
			}
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	System.out.println("heeee");
	}

}
