package gui.user.owner;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.PatternSyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import gui.components.TextPrompt;

public class OwnerMainPanel extends JPanel {

	private static final long serialVersionUID = -7588182068778737214L;

	private static boolean DEBUG = true;

	private JTextField searchField;
	private JTable table;
	private TableModel tableModel;
	private TableRowSorter<TableModel> sorter;
	//	private JPanel topPanel, bottomPanel;
	private JScrollPane tableScrollPanel;
	//	private GridBagLayout gridBagLayout;
	//	private GridBagConstraints gbcTopPanel, gbcCenterPanel, gbcBottomPanel;
	private JButton btnAdd, btnEdit, btnRemove;//, btnDetails;

	/**
	 * Launch the application.
	 * @param args arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
					frame.setMinimumSize(new Dimension(400, 300));
					frame.setSize(700, 365);
					//frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the panel.
	 */
	public OwnerMainPanel() {

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		//		//GridBagConstraints variables
		//		gbc.ipadx = 0;
		//		gbc.ipady = 0;
		//		gbc.weightx = 0.0;
		//		gbc.weighty = 1.0;
		//		gbc.anchor = GridBagConstraints.PAGE_END;
		//		gbc.insets = new Insets(10,0,0,0);
		//		gbc.gridwidth = 2;
		//		gbc.gridheight = 1;
		//		gbc.gridx = 1;
		//		gbc.gridy = 2;

		gbc.ipady = 10;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(getSearchField(), gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 0.5;
		gbc.weighty = 1;

		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(getTableScrollPanel(), gbc);

		setupOwnerWindow();
		
		

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

		//[FIXME] Just a prank, bro.
		tableModel.setRandomImages();
		updateRowHeights();

	}

	private void setupOwnerWindow() {

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.anchor = GridBagConstraints.PAGE_START;		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridwidth = 1;

		gbc.insets = new Insets(10, 50, 10, 5);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(getBtnAdd(), gbc);

		gbc.insets = new Insets(10, 5, 10, 5);
		gbc.gridx = 1;
		gbc.gridy = 2;
		add(getBtnEdit(), gbc);

		gbc.insets = new Insets(10, 5, 10, 50);
		gbc.gridx = 2;
		gbc.gridy = 2;
		add(getBtnRemove(), gbc);

	}

	/*
	private JPanel getTopPanel() {
		if(topPanel == null) {
			topPanel = new JPanel();
		}
		return topPanel;
	}
	 */

	/*
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
	 */

	private JScrollPane getTableScrollPanel() {
		if(tableScrollPanel == null) {
			tableScrollPanel = new JScrollPane(getTable());
		}
		return tableScrollPanel;
	}

	/*
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
	 */

	/*
	private JPanel getBottomPanel() {
		if(bottomPanel == null) {
			bottomPanel = new JPanel();
			//			bottomPanel.add(getBtnAdd());
			//			bottomPanel.add(getBtnEdit());
			//			bottomPanel.add(getBtnRemove());
		}
		return bottomPanel;
	}
	 */

	/*
	private GridBagConstraints getGbcBottomPanel() {
		if(gbcBottomPanel == null) {
			gbcBottomPanel = new GridBagConstraints();
			gbcBottomPanel.anchor = GridBagConstraints.PAGE_END;
			gbcBottomPanel.fill = GridBagConstraints.HORIZONTAL;
			gbcBottomPanel.gridx = 0;
			gbcBottomPanel.gridy = 2;
		}
		return gbcBottomPanel;
	}
	 */

	private JTextField getSearchField() {
		if(searchField == null) {
			searchField = new JTextField();
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
		TextPrompt tp = new TextPrompt(searchField);
		tp.setText("Search...");
		tp.setStyle(Font.BOLD);
		tp.setAlpha(128);	
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

	/*
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
	 */

	private JTable getTable() {
		if(table == null) {
			tableModel = new TableModel();
			sorter = new TableRowSorter<TableModel>(tableModel);
			table = new JTable(tableModel);
			table.setRowSorter(sorter);
			table.setPreferredScrollableViewportSize(new Dimension(500, 70));
			table.getTableHeader().setReorderingAllowed(false);
//			table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			//When selection changes, provide user with row numbers for both view and model.
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent event) {
					int row = table.getSelectedRow();
					if (row < 0) {
						//Selection got filtered away.
						System.out.println("");
					} else {
						int modelRow = table.convertRowIndexToModel(row);
					System.out.println(String.format("Selected Row in view: %d. Selected Row in model: %d.", row, modelRow));
					}
				}

			});

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
//					JTable table =(JTable) me.getSource();
//					Point p = me.getPoint();
//					int row = table.rowAtPoint(p);
					if (me.getClickCount() == 2) {
						JOptionPane.showMessageDialog(null,	"Double clicked the row.\nWhen implemented, more details window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});

			tableScrollPanel = new JScrollPane(table);

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

	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton("Add");
		}
		return btnAdd;
	}

	private JButton getBtnEdit() {
		if (btnEdit == null) {
			btnEdit = new JButton("Edit");
		}
		return btnEdit;
	}

	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove");
		}
		return btnRemove;
	}

	private class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private int width;
		private int height;

		private String[] columnNames = {"Image", "First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};

		private Object[][] data = {
				{new ImageIcon("/img/house00.png"), "Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false) },
				{new ImageIcon("/img/house00.png"), "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				{new ImageIcon("/img/house00.png"), "Sue", "Black", "Knitting", new Integer(2), new Boolean(false) },
				{new ImageIcon("/img/house00.png"), "Jane", "White", "Speed reading", new Integer(20), new Boolean(true) },
				{new ImageIcon("/img/house00.png"), "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) }
		};

		private String[] images = {"/img/house00.png", "/img/house01.png", "/img/house02.png", "/img/house03.png", "/img/house04.png"};

		private TableModel() {
			this.width = 50;
			this.height = 50;
		}

		public void setRandomImages() {		
			for (Object[] object: data) {
				// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
				int randomNum = ThreadLocalRandom.current().nextInt(0, images.length);
				object[0] = getImage(images[randomNum]);
			}
		}

		//			// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
		//			for (int  = 0;  < data.length; ++) {
		//				int randomNum = ThreadLocalRandom.current().nextInt(0, data.length);
		//				System.out.println(images[randomNum]);
		//				setValueAt(, 0, new ImageIcon(images[randomNum]));
		//			}

		private ImageIcon getImage(String path) {
			BufferedImage bufferedImage;
			ImageIcon imageIcon = null;
			try {
				bufferedImage = ImageIO.read(getClass().getResource(path));
				imageIcon =  new ImageIcon(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return imageIcon;
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

		@SuppressWarnings("unused")
		public void setValueAt(int row, int col, ImageIcon value) {
			data[row][col] = getScaledImage(value);
		}

		private ImageIcon getScaledImage(ImageIcon imageIcon) {
			return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height,Image.SCALE_SMOOTH));
		}
		
		@SuppressWarnings("unused")
		public void setValueAt(int row, int col, Object value) {
			data[row][col] = value;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@SuppressWarnings("unused")
		public int getDefaultImageWidth() {
			return width;
		}

		@SuppressWarnings("unused")
		public void setDefaultImageWidth(int width) {
			this.width = width;
		}

		@SuppressWarnings("unused")
		public int getDefaultImageHeight() {
			return height;
		}

		@SuppressWarnings("unused")
		public void setDefaultImageHeight(int height) {
			this.height = height;
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
		@Override
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "+ value.getClass() + ")");
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
