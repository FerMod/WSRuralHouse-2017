package gui.components.table;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class CustomTableModel extends AbstractTableModel {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -3112261746199594263L;

	private Dimension imageDimension;

	private String[] columnNames;

	private Object[][] data; 

	public <T> CustomTableModel(List<CellComponent<T>> cellComponent) {
		this(Arrays.asList("Details"), null, cellComponent);	
	}

	public <T> CustomTableModel(List<ImageIcon> imageList, List<CellComponent<T>> cellComponent) {
		this(Arrays.asList("Image", "Details"), imageList, cellComponent);
	}

	public <T> CustomTableModel(List<String> columnNames, List<ImageIcon> imageList, List<CellComponent<T>> cellComponent) {
		super();
		this.imageDimension = new Dimension(60, 60);
		this.columnNames = (String[]) columnNames.toArray();
		if(cellComponent != null && imageList != null) {
			initData(imageList, cellComponent);					
		} else if(cellComponent != null) {
			initData(cellComponent);
		} 
	}

	private <T> void initData(List<CellComponent<T>> cellComponentList) {
		data = new Object[cellComponentList.size()][1];
		int i = 0;
		for (CellComponent<T> component : cellComponentList) {
			data[i][0] = component;
			System.out.println("data[" + i + "][0] " + component.toString());
			i++;					
		}
		System.out.println();
	}

	private <T> void initData(List<ImageIcon> imageList, List<CellComponent<T>> cellComponentList) {
		data = new Object[cellComponentList.size()][2];
		int i = 0;
		for (CellComponent<T> component : cellComponentList) {
			data[i][0] = getScaledImage(imageList.get(i));
			System.out.println("data[" + i + "][0] " + imageList.get(i));
			data[i][1] = component;
			System.out.println("data[" + i + "][1] " + component.toString());
			i++;					
		}
		System.out.println();
	}

	public ImageIcon getScaledImage(String path) {
		ImageIcon imageIcon = null;
		try {
			imageIcon = getScaledImage(ImageIO.read(getClass().getResource(path)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageIcon;
	}

	public ImageIcon getScaledImage(BufferedImage bufferedImage) {	 
		return new ImageIcon(bufferedImage.getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_SMOOTH));
	}

	public ImageIcon getScaledImage(ImageIcon imageIcon) {
		return new ImageIcon(imageIcon.getImage().getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_SMOOTH));
	}

	@Override
	public int getColumnCount() {
		if(columnNames != null) {
			return columnNames.length;			
		}
		return 0;
	}

	@Override
	public int getRowCount() {
		if(data != null) {
			return data.length;			
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public void setValueAt(int row, int col, ImageIcon value) {
		data[row][col] = getScaledImage(value);
	}

	public void setValueAt(int row, int col, Object value) {
		data[row][col] = value;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Dimension getImageDimension() {
		return imageDimension;
	}

	public void setImageDimension(int width, int height) {
		setImageDimension(new Dimension(width, height));
	}

	public void setImageDimension(Dimension imageDimension) {
		this.imageDimension = imageDimension;
	}

	public int getImageWidth() {
		return imageDimension.width;
	}

	public void setImageWidth(int width) {
		this.imageDimension.width = width;
	}

	public int getImageHeight() {
		return this.imageDimension.height;
	}

	public void setImageHeight(int height) {
		this.imageDimension.height = height;
	}

	/*
	 * JTable uses this method to determine the default renderer/
	 * editor for each cell.  If we didn't implement this method,
	 * then the last column would contain text ("true"/"false"),
	 * rather than a check box.
	 */
	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}


	public boolean isCellEditable(int row, int col) {
		//Note that the data/cell address is constant, no matter where the cell appears on screen.
		return true;
	}


	/*
	 * Don't need to implement this method unless your table's
	 * data can change.
	 */
	//		@Override
	//		public void setValueAt(Object value, int row, int col) {
	//			if (DEBUG) {
	//				System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "+ value.getClass() + ")");
	//			}
	//
	//			data[row][col] = value;
	//			fireTableCellUpdated(row, col);
	//
	//			if (DEBUG) {
	//				System.out.println("New value of data:");
	//				printDebugData();
	//			}
	//		}

	/**
	 * Set the width of the columns as percentages.
	 * 
	 * @param table the {@link JTable} whose columns will be set
	 * @param percentages the widths of the columns as percentages</p>
	 * <b>Note</b>: this method does <b>NOT</b> verify that all percentages add up to 100% and for
	 * the columns to appear properly, it is recommended that the widths for <b>ALL</b> columns be specified.
	 */
	public void setTableColumnWidthPercentages(JTable table, double[] percentages) {
		final double factor = 10000;
		TableColumnModel model = table.getColumnModel();
		for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
			TableColumn column = model.getColumn(columnIndex);
			column.setPreferredWidth((int) (percentages[columnIndex] * factor));
		}
	}

	@SuppressWarnings("unused")
	private void printDebugData() {
		int numRows = getRowCount();
		int numCols = getColumnCount();

		for (int i=0; i < numRows; i++) {
			System.out.print("    row " + i + ":");
			for (int j=0; j < numCols; j++) {
				System.out.print("  " + data[i][j]);
			}
			System.out.println();
		}
		System.out.println("--------------------------");
	}

}
