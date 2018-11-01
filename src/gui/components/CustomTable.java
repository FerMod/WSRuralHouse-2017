package gui.components;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class CustomTable extends JTable {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -5867408116296077885L;

	private int rollOverRowIndex = -1;

	public CustomTable(TableModel model) {
		super(model);
		RollOverListener rollOverListener = new RollOverListener();
		addMouseMotionListener(rollOverListener);
		addMouseListener(rollOverListener);
	}

	@Override
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
	
}
