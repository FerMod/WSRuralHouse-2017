package gui;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

public class ClientMainWindow extends JFrame {

	private boolean DEBUG = true;

	private static final long serialVersionUID = 3063325462028186709L;

	private JTextField searchField;
	private JTable table;
	private TableModel tableModel;
	private TableRowSorter<TableModel> sorter;
	private JPanel topPanel, bottomPanel;
	private JScrollPane centerPanel;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gbcTopPanel, gbcCenterPanel, gbcBottomPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMainWindow frame = new ClientMainWindow();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientMainWindow() {	

		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(getGridBagLayout());

		GridBagConstraints gbc;

		gbc = getGbcTopPanel();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 10);
		getContentPane().add(getSearchField(), gbc);

		gbc = getGbcCenterPanel();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		getContentPane().add(getCenterPanel(), gbc);
		getCenterPanel().setViewportView(getTable());

		//getCenterPanel().add(getTable());

		//Create the scroll pane and add the table to it.
		//		JScrollPane scrollPane = new JScrollPane(getTable());
		//Add the scroll pane to this panel.
		//		getCenterPanel().add(scrollPane);

		getContentPane().add(getBottomPanel(), getGbcBottomPanel());

		JButton btnNewButton = new JButton("New button");
		bottomPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		bottomPanel.add(btnNewButton_1);

		/*
		JMenuBar menuBar = new JMenuBar();
		menuBar.setOpaque(false);
		menuBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setJMenuBar(menuBar);

		JButton btnLogOut = new JButton("Log Out");
		btnLogOut.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		btnLogOut.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogOut.setPreferredSize(new Dimension(40, 20));
		menuBar.add(btnLogOut);
		 */

		//		table = new JTable(null, columnNames);
		//        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//        table.setFillsViewportHeight(true);
		//	
		//        panel_2.add(table);

		//[FIXME] Just a prank, bro.
		tableModel.randomImage();
		updateRowHeights();

	}

	private JPanel getTopPanel() {
		if(topPanel == null) {
			topPanel = new JPanel();
		}
		return topPanel;
	}

	private GridBagConstraints getGbcTopPanel() {
		if(gbcTopPanel == null) {
			gbcTopPanel = new GridBagConstraints();
			gbcTopPanel.anchor = GridBagConstraints.FIRST_LINE_START;
			gbcTopPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcTopPanel.insets = new Insets(0, 0, 0, 0);
			gbcTopPanel.gridx = 0;
			gbcTopPanel.gridy = 0;
		}
		return gbcTopPanel;
	}

	private JScrollPane getCenterPanel() {
		if(centerPanel == null) {
			centerPanel = new JScrollPane();
		}
		return centerPanel;
	}

	private GridBagConstraints getGbcCenterPanel() {
		if(gbcCenterPanel == null) {
			gbcCenterPanel = new GridBagConstraints();
			gbcCenterPanel.insets = new Insets(0, 0, 5, 0);
			gbcCenterPanel.fill = GridBagConstraints.BOTH;
			gbcCenterPanel.gridx = 0;
			gbcCenterPanel.gridy = 1;
		}
		return gbcCenterPanel;
	}

	private JPanel getBottomPanel() {
		if(bottomPanel == null) {
			bottomPanel = new JPanel();
		}
		return bottomPanel;
	}

	private GridBagConstraints getGbcBottomPanel() {
		if(gbcBottomPanel == null) {
			gbcBottomPanel = new GridBagConstraints();
			gbcBottomPanel.anchor = GridBagConstraints.NORTH;
			gbcBottomPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcBottomPanel.gridx = 0;
			gbcBottomPanel.gridy = 2;
		}
		return gbcBottomPanel;
	}

	private JTextField getSearchField() {
		if(searchField == null) {
			searchField = new JTextField();
			//			searchField.setColumns(10);
			//Whenever filterText changes, invoke refreshTableContent().
			searchField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent e) {
					refreshTableContent();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshTableContent();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshTableContent();
				}

			});

		}
		return searchField;
	}

	/** 
	 * Update the row filter regular expression from the expression in the search text box.
	 */
	private void refreshTableContent() {
		RowFilter<TableModel, Object> rf = null;
		//If current expression can't parse, don't update.
		try {
			//Case insensitive flag   (?i)
			rf = RowFilter.regexFilter("(?i)" + searchField.getText());
			sorter.setRowFilter(rf);
		} catch (PatternSyntaxException e) {
			System.err.println("Info: Expression could not parse. Syntax error in a regular-expression pattern.");
		}
	}

	private GridBagLayout getGridBagLayout() {
		if(gridBagLayout == null) {
			gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{420, 0};
			gridBagLayout.rowHeights = new int[]{30, 176, 33, 0};
			gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		}
		return gridBagLayout;
	}

	private JTable getTable() {
		if(table == null) {
			tableModel = new TableModel();
			sorter = new TableRowSorter<TableModel>(tableModel);
			table = new JTable(tableModel);
			table.setRowSorter(sorter);
			table.setPreferredScrollableViewportSize(new Dimension(500, 70));
			table.getTableHeader().setReorderingAllowed(false);
			table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			//When selection changes, provide user with row numbers for both view and model.
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent event) {
					int viewRow = table.getSelectedRow();
					if (viewRow < 0) {
						//Selection got filtered away.
						System.out.println("");
					} else {
						int modelRow = table.convertRowIndexToModel(viewRow);
						System.out.println(String.format("Selected Row in view: %d. Selected Row in model: %d.", viewRow, modelRow));
					}
				}

			});

		}
		return table;
	}

	private void updateRowHeights() {
		for (int row = 0; row < table.getRowCount(); row++) {
			int rowHeight = table.getRowHeight();
			for (int column = 0; column < table.getColumnCount(); column++) {
				Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}
			table.setRowHeight(row, rowHeight);
		}
	}

	private class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private String[] columnNames = {"Image", "First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};

		private Object[][] data = {
				{new ImageIcon("/img/house00.png"), "Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false) },
				{new ImageIcon("/img/house00.png"), "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				{new ImageIcon("/img/house00.png"), "Sue", "Black", "Knitting", new Integer(2), new Boolean(false) },
				{new ImageIcon("/img/house00.png"), "Jane", "White", "Speed reading", new Integer(20), new Boolean(true) },
				{new ImageIcon("/img/house00.png"), "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) }
		};

		private String[] images = {"/img/house00.png", "/img/house01.png", "/img/house02.png", "/img/house03.png", "/img/house04.png"};

		public void randomImage() {
			for (Object[] object: data) {
				object[0] = getImage();
			}
		}

		private ImageIcon getImage() {
			// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
			int randomNum = ThreadLocalRandom.current().nextInt(0, images.length);
			BufferedImage img;
			try {
				img = ImageIO.read(getClass().getResource(images[randomNum]));
				return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public String[] getColumns() {
			return columnNames;
		}

		public Object[][] getData() {
			return data;
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return true;
			}
		}
		 */

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value
						+ " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}
		}

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

}
