package gui.components.component.table;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public interface CellComponentInterface extends TableCellEditor, TableCellRenderer {

	@Override
	Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column);

	@Override
	Object getCellEditorValue();

	@Override
	boolean isCellEditable(EventObject e);

	@Override
	Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column);

}