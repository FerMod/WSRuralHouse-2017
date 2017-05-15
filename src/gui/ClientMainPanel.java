package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import domain.Offer;
import domain.Review.ReviewState;
import gui.components.RightClickMenu;
import gui.components.TextPrompt;

public class ClientMainPanel extends JPanel {

	private static final long serialVersionUID = 3063325462028186709L;

	private JFrame frame;
	//	private JFormattedTextField minPriceField
	private JFormattedTextField maxPriceField;
	private JTextField searchField;
	private JTable table;
	private TableModel tableModel;
	private TableRowSorter<TableModel> sorter;
	//	private JPanel topPanel, bottomPanel;
	private JScrollPane tableScrollPanel;
	//	private GridBagLayout gridBagLayout;
	//	private GridBagConstraints gbcTopPanel, gbcCenterPanel, gbcBottomPanel;
	private JButton btnAdd, btnEdit, btnRemove;
	private JSlider priceSlider;

	private NumberFormat priceFormat;

	/**
	 * Create the panel.
	 */
	public ClientMainPanel(JFrame frame) {

		this.frame = frame;

		setLayout(new GridBagLayout());

		setupNumberFormat();



		//// GridBagConstraints fields ///////////////////
		// gbc.ipadx = 0;								//
		// gbc.ipady = 0;								//
		// gbc.weightx = 0.0;							//
		// gbc.weighty = 1.0;							//
		// gbc.anchor = GridBagConstraints.PAGE_END;	//
		// gbc.insets = new Insets(10,0,0,0);			//
		// gbc.gridwidth = 2;							//
		// gbc.gridheight = 1;							//
		// gbc.gridx = 1;								//
		// gbc.gridy = 2;								//
		//////////////////////////////////////////////////

		GridBagConstraints gbcSearchField = new GridBagConstraints();
		gbcSearchField.fill = GridBagConstraints.HORIZONTAL;
		gbcSearchField.ipady = 10;
		gbcSearchField.weightx = 0.5;
		gbcSearchField.weighty = 0;
		gbcSearchField.anchor = GridBagConstraints.PAGE_START;
		gbcSearchField.fill = GridBagConstraints.HORIZONTAL;
		gbcSearchField.gridwidth = 5;
		gbcSearchField.insets = new Insets(20, 5, 0, 10);
		gbcSearchField.gridx = 0;
		gbcSearchField.gridy = 0;
		add(getSearchField(), gbcSearchField);
		RightClickMenu rightClickMenu = new RightClickMenu(getSearchField());
		getSearchField().setComponentPopupMenu(rightClickMenu);

		GridBagConstraints gbcMaxPriceField = new GridBagConstraints();
		gbcMaxPriceField.weightx = 0.1;
		gbcMaxPriceField.anchor = GridBagConstraints.CENTER;
		gbcMaxPriceField.insets = new Insets(5, 10, -5, 10);
		gbcMaxPriceField.gridwidth = 2;
		gbcMaxPriceField.gridx = 0;
		gbcMaxPriceField.gridy = 1;
		add(getMaxPriceField(), gbcMaxPriceField);

		GridBagConstraints gbcPriceSlider = new GridBagConstraints();
		gbcPriceSlider.ipadx = 0;
		gbcPriceSlider.ipady = 0;
		gbcPriceSlider.anchor = GridBagConstraints.CENTER;
		gbcPriceSlider.fill = GridBagConstraints.HORIZONTAL;
		gbcPriceSlider.weightx = 0.1;
		gbcPriceSlider.gridwidth = 2;
		gbcPriceSlider.insets = new Insets(0, 5, 2, 0);
		gbcPriceSlider.gridx = 0;
		gbcPriceSlider.gridy = 2;
		add(getPriceSlider(), gbcPriceSlider);

		GridBagConstraints gbcTableScrollPanel = new GridBagConstraints();
		gbcTableScrollPanel.ipadx = 0;
		gbcTableScrollPanel.ipady = 0;
		gbcTableScrollPanel.weighty = 0;
		gbcTableScrollPanel.gridwidth = 1;
		gbcTableScrollPanel.gridheight = 1;

		//		gbc.weightx = 0.1;
		//		gbc.anchor = GridBagConstraints.PAGE_START;
		//		gbc.insets = new Insets(2, 10, 5, 10);
		//		gbc.gridx = 0;
		//		gbc.gridy = 2;
		//		add(getMinPriceField(), gbc);

		//		gbc.weightx = 0.1;
		//		gbc.anchor = GridBagConstraints.PAGE_END;
		//		gbc.insets = new Insets(2, 5, 15, 10);
		//		gbc.gridx = 1;
		//		gbc.gridy = 2;
		//		add(getMaxPriceField(), gbc);

		gbcTableScrollPanel.ipady = 0;
		gbcTableScrollPanel.fill = GridBagConstraints.BOTH;
		gbcTableScrollPanel.anchor = GridBagConstraints.CENTER;
		gbcTableScrollPanel.gridwidth = 2;
		gbcTableScrollPanel.gridheight = 3;
		gbcTableScrollPanel.insets = new Insets(0, 10, 10, 10);
		gbcTableScrollPanel.weightx = 1;
		gbcTableScrollPanel.weighty = 1;

		gbcTableScrollPanel.gridwidth = 3;
		gbcTableScrollPanel.gridx = 2;
		gbcTableScrollPanel.gridy = 1;
		add(getTableScrollPanel(), gbcTableScrollPanel);

		//		gbc.anchor = GridBagConstraints.PAGE_START;		
		//		gbc.fill = GridBagConstraints.HORIZONTAL;
		//		gbc.weightx = 0.5;
		//		gbc.weighty = 0;
		//		gbc.gridwidth = 5;
		//
		//		gbc.insets = new Insets(10, 100, 10, 100);
		//		gbc.gridx = 0;
		//		gbc.gridy = 4;
		//		add(getBtnDetails(), gbc);

		//		setupClientWindow();

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

		//[FIXME] Temporal images.
		//tableModel.setRandomImages();
		updateRowHeights();

	}

	private JSlider getPriceSlider() {
		if(priceSlider == null) {
			double maxPrice = MainWindow.getBusinessLogic().getOffersHighestPrice();
			maxPrice = ((int) ((maxPrice + 99) / 100) * 100);
			priceSlider = new JSlider(JSlider.HORIZONTAL, 0, (int)(maxPrice * 100), (int)(maxPrice* 100));
			priceSlider.setMajorTickSpacing((priceSlider.getMaximum() * 25) / 100); //each 25% of the value
			priceSlider.setMinorTickSpacing((priceSlider.getMajorTickSpacing() * 10) / 100); //each 10% of the 25% of the value
			priceSlider.setLabelTable(getSliderLabelTable(0, maxPrice));
			priceSlider.setPaintTicks(true);
			priceSlider.setPaintLabels(true);
			priceSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(e.getSource() == priceSlider) {
						maxPriceField.setValue(priceSlider.getValue() / 100);
						//refreshTableContent((Number)minPriceField.getValue(), (Number)maxPriceField.getValue());
						refreshTableContent(0, (Number)maxPriceField.getValue());
					}
				}
			});
		}
		return priceSlider;
	}

	private Hashtable<Integer, JLabel> getSliderLabelTable(double minPrice, double maxPrice) {

		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();		
		labelTable.put(getPriceSlider().getMinimum(), new JLabel(currencyFormatter.format(minPrice)));
		labelTable.put(getPriceSlider().getMaximum()/2, new JLabel(currencyFormatter.format((maxPrice-minPrice)/2)));
		labelTable.put(getPriceSlider().getMaximum(), new JLabel(currencyFormatter.format(maxPrice)));
		return labelTable;
	}

	private Component getMaxPriceField() {
		if(maxPriceField == null) {
			if(priceFormat == null) {
				setupNumberFormat();
			}
			maxPriceField = new JFormattedTextField(priceFormat);
			maxPriceField.setColumns(4);
			maxPriceField.setValue(getPriceSlider().getMaximum()/100);
			maxPriceField.setInputVerifier(new FormattedTextFieldVerifier());
			maxPriceField.setFocusable(false);
			maxPriceField.setEditable(false);
			maxPriceField.setOpaque(false);
			maxPriceField.setHorizontalAlignment(SwingConstants.CENTER);
			maxPriceField.setBorder(new EmptyBorder(0, 0, 0, 0));

		}
		return maxPriceField;
	}

	@SuppressWarnings("unused")
	private void updatePriceRange(Number minPrice, Number maxPrice) {
		priceSlider.setMinimum(minPrice.intValue() * 100);
		priceSlider.setMaximum(maxPrice.intValue() * 100);
		priceSlider.setLabelTable(getSliderLabelTable(minPrice.doubleValue(), maxPrice.doubleValue()));
	}

	private void setupNumberFormat() {
		priceFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
		priceFormat.setMinimumIntegerDigits(1);
		priceFormat.setMaximumFractionDigits(2);
	}

	// Create and set up number formats. These objects also parse numbers input by user.
	//	private void setUpFormats(Locale currentLocale) {
	//		
	//		System.out.println(currentLocale.getCountry() + " \\\\ " + currentLocale.getLanguage());
	//
	//		
	//		currencyInstance =  Currency.getInstance(currentLocale);
	//		
	//		//priceFormat = NumberFormat.getNumberInstance();
	//		priceFormat = NumberFormat.getCurrencyInstance(currentLocale);
	//		priceFormat.setCurrency(currencyInstance);
	//	}

	private JScrollPane getTableScrollPanel() {
		if(tableScrollPanel == null) {
			tableScrollPanel = new JScrollPane(getTable());
		}
		return tableScrollPanel;
	}

	private JTextField getSearchField() {
		if(searchField == null) {
			searchField = new JTextField();
			searchField.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY));
			//Whenever filterText changes, invoke refreshTableContent().
			searchField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent e) {
					refreshTableContent(searchField.getText());
					System.out.println("Finished");
					getTable().clearSelection();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshTableContent(searchField.getText());
					System.out.println("Finished");
					getTable().clearSelection();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshTableContent(searchField.getText());
					System.out.println("Finished");
					getTable().clearSelection();
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
	 * <strong>TODO</strong> Remove unused method
	 * Update the row filter regular expression from the expression in the search text box.
	 * 
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void refreshTableContent(Matcher expression) {
		RowFilter<TableModel, Object> rf = null;
		//If current expression can't parse, don't update.
		try {
			//Case insensitive flag   (?i)

			//^[a-zA-Z0-9]*$
			//
			// ^ - Start of string
			//
			// [a-zA-Z0-9]* - multiple characters to include
			//
			// $ - End of string

			rf = RowFilter.regexFilter(expression.toString() + searchField.getText());
			sorter.setRowFilter(rf);
		} catch (PatternSyntaxException e) {
			System.err.println("Info: Expression could not parse. Syntax error in a regular-expression pattern.");
		}
	}

	private void refreshTableContent(String text) {
		RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
			@Override
			public boolean include(Entry<? extends Object, ? extends Object> entry) {

				boolean include = false;

				CellDetails cellDetails = (CellDetails) entry.getValue(1);
				//JTextArea textArea = null;

				System.out.println("cellDetails.getDescription().contains(text) -> " + cellDetails.getOffer().getRuralHouse().getDescription().contains(text));
				if(cellDetails.getOffer().getRuralHouse().getDescription().contains(text)) {
					//textArea = cellDetails.getTableDetailsCell().getDescriptionTextArea();
					include = true;
				}

				System.out.println("cellDetails.getAddress().contains(text) -> " + cellDetails.getOffer().getRuralHouse().getAddress().contains(text));
				if(cellDetails.getOffer().getRuralHouse().getAddress().contains(text)) {
					//textArea = cellDetails.getTableDetailsCell().getAddressField();
					include = true;
				}

				// System.out.println("textArea != null -> " + textArea != null);
				// if(include && !text.equals("") && textArea != null) {
				// 	 highlightTextAreaWord(textArea, text, Color.YELLOW);
				// }

				return include;
			}
		};
		sorter.setRowFilter(filter);
	}

	private void refreshTableContent(Number minPrice, Number maxPrice) {
		RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
			public boolean include(Entry<? extends Object, ? extends Object> entry) {
				CellDetails cellDetails = (CellDetails) entry.getValue(1);
				return cellDetails.getOffer().getPrice() > minPrice.doubleValue() && cellDetails.getOffer().getPrice() <= maxPrice.doubleValue();
			}
		};
		sorter.setRowFilter(filter);		
	}

	/**
	 * Highlight the text in the {@code JTextArea} with the passed color. </p>
	 * If no matches are found, no text is highlighted. </p>
	 * 
	 * @param textArea the {@code JTextArea} where the text is going to be highlighted
	 * @param text the string to be highlighted
	 * @param color the color of the highlighted text
	 */
	@SuppressWarnings("unused")
	private void highlightTextAreaWord(JTextArea textArea, String text, Color color) {

		Highlighter highlighter = textArea.getHighlighter();
		//highlighter.removeAllHighlights();

		text = text.toLowerCase();
		String textAreaText = textArea.getText().toLowerCase();
		int index = textAreaText.indexOf(text);
		while(index >= 0){
			try {            
				highlighter.addHighlight(index, index + text.length(), new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
				index = textAreaText.indexOf(text, index + text.length());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

	}

	private JTable getTable() {
		if(table == null) {
			tableModel = new TableModel();
			sorter = new TableRowSorter<TableModel>(tableModel);
			table = new JTable(tableModel);
			table.setRowSorter(sorter);
			table.setPreferredScrollableViewportSize(new Dimension(500, 70));
			table.getTableHeader().setReorderingAllowed(false);
			//table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setFocusable(false);
			table.getTableHeader().setUI(null); //Hide the header
			table.setShowVerticalLines(false);
			table.setIntercellSpacing(new Dimension(0, 1));
			table.setUpdateSelectionOnSort(true);
			//			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			//			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			//			table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );

			//Center the content in the column
			DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer();
			centerCellRenderer.setHorizontalAlignment(JLabel.CENTER);	
			//			table.setDefaultRenderer(Double.class, centerCellRenderer);
			//			table.setDefaultRenderer(String.class, centerCellRenderer);
			//table.getColumnModel().getColumn(1).setCellRenderer(leftCellRenderer);

			setTableColumnWidthPercentages(table, 0.1, 0.9);
			table.setDefaultRenderer(Object.class, new TableCellComponent());
			table.setDefaultEditor(Object.class, new TableCellComponent());

			//When selection changes, provide user with row numbers for both view and model.
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					int row = table.getSelectedRow();
					if (row < 0) {
						//Selection got filtered away.
						//btnDetails.setEnabled(false);
					} else {
						//btnDetails.setEnabled(true);
						int modelRow = table.convertRowIndexToModel(row);
						System.out.println(String.format("Selected Row in view: %d. Selected Row in model: %d.", row, modelRow));
					}
				}

			});

			table.addFocusListener(new FocusListener() {				
				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					table.clearSelection();
				}
			});

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					//					JTable table =(JTable) me.getSource();
					//					Point p = me.getPoint();
					//					int row = table.rowAtPoint(p);
					if (me.getClickCount() == 2) {
						JOptionPane.showMessageDialog(null,	"Double clicked the row.\nWhen implemented, info window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
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

	@SuppressWarnings("unused")
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton("Add");
		}
		return btnAdd;
	}

	@SuppressWarnings("unused")
	private JButton getBtnEdit() {
		if (btnEdit == null) {
			btnEdit = new JButton("Edit");
		}
		return btnEdit;
	}

	@SuppressWarnings("unused")
	private JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove");
		}
		return btnRemove;
	}

	//	private JButton getBtnDetails() {
	//		if (btnDetails == null) {
	//			btnDetails = new JButton("Details");
	//			btnDetails.setEnabled(false);
	//			btnDetails.addActionListener(new ActionListener() {
	//				@Override
	//				public void actionPerformed(ActionEvent e) {
	//					JOptionPane.showMessageDialog(null,	"\"Details\" button pressed.\nWhen implemented, more details window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
	//				}
	//			});
	//		}
	//		return btnDetails;
	//	}

	/**
	 * Set the width of the columns as percentages.
	 * 
	 * @param table the {@link JTable} whose columns will be set
	 * @param percentages the widths of the columns as percentages</p>
	 * <b>Note</b>: this method does <b>NOT</b> verify that all percentages add up to 100% and for
	 * the columns to appear properly, it is recommended that the widths for <b>ALL</b> columns be specified.
	 */
	public void setTableColumnWidthPercentages(JTable table, double... percentages) {
		final double factor = 10000;
		TableColumnModel model = table.getColumnModel();
		for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
			TableColumn column = model.getColumn(columnIndex);
			column.setPreferredWidth((int) (percentages[columnIndex] * factor));
		}
	}

	class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private Dimension imageDimension;

		private String[] columnNames = {"Image", "Details"};

		private Object[][] data; 

		//TODO REMOVE
		private String[] images = {"/img/house00.png", "/img/house01.png", "/img/house02.png", "/img/house03.png", "/img/house04.png"};

		private TableModel() {
			this.imageDimension = new Dimension(60, 60);			
			initTableData();			
		}

		private void initTableData() {
			Vector<Offer> offerVector = MainWindow.getBusinessLogic().getActiveOffers(ReviewState.APPROVED);
			data = new Object[offerVector.size()][2];
			int i = 0;
			for (Offer offer : offerVector) {
				data[i][0] = getScaledImage(offer.getRuralHouse().getImage(0));
				System.out.println("data[" + i + "][0] " + offer.getRuralHouse().getImage(0).getDescription());			
				data[i][1] = new CellDetails(offer);
				System.out.println("data[" + i + "][1] " + offer);
				i++;					
			}
			System.out.println();
		}

		@Deprecated
		public void setRandomImages() {		
			for (Object[] object: data) {
				// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
				int randomNum = ThreadLocalRandom.current().nextInt(0, images.length);
				object[0] = getScaledImage(images[randomNum]);
			}
		}

		private ImageIcon getScaledImage(String path) {
			ImageIcon imageIcon = null;
			try {
				imageIcon = getScaledImage(ImageIO.read(getClass().getResource(path)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return imageIcon;
		}

		private ImageIcon getScaledImage(BufferedImage bufferedImage) {	 
			return new ImageIcon(bufferedImage.getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_SMOOTH));
		}

		private ImageIcon getScaledImage(ImageIcon imageIcon) {
			return new ImageIcon(imageIcon.getImage().getScaledInstance(imageDimension.width, imageDimension.height, Image.SCALE_SMOOTH));
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
			if (col < 1) {
				return false;
			} else {
				return true;
			}
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

	public class TableCellComponent extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{

		private static final long serialVersionUID = 2711709042458345572L;

		private JTextArea titleComponent, descriptionComponent, addressComponent, priceComponent;
		private JButton infoButton;
		private JPanel panel;

		public TableCellComponent() {

			panel = new JPanel();
			panel.setBorder(new EmptyBorder(2, 5, 2, 5));

			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] {128, 34, 0, 2};
			gridBagLayout.rowHeights = new int[] {0, 0, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0};
			panel.setLayout(gridBagLayout);

			titleComponent = new JTextArea();
			titleComponent.setText("title\nDate: startDate - endDate");
			titleComponent.setOpaque(false);
			titleComponent.setEditable(false);
			titleComponent.setFocusable(false);
			titleComponent.setRows(2);
			GridBagConstraints gbcTitle = new GridBagConstraints();
			gbcTitle.insets = new Insets(0, 0, 5, 5);
			gbcTitle.fill = GridBagConstraints.HORIZONTAL;
			gbcTitle.gridx = 0;
			gbcTitle.gridy = 0;
			panel.add(titleComponent, gbcTitle);

			descriptionComponent = new JTextArea("description");
			descriptionComponent.setOpaque(false);
			descriptionComponent.setEditable(false);
			descriptionComponent.setFocusable(false);
			descriptionComponent.setLineWrap(true);
			descriptionComponent.setWrapStyleWord(true);
			descriptionComponent.setRows(3);
			GridBagConstraints gbcDescription = new GridBagConstraints();
			gbcDescription.gridwidth = 2;
			gbcDescription.insets = new Insets(5, 0, 5, 5);
			gbcDescription.fill = GridBagConstraints.BOTH;
			gbcDescription.gridx = 0;
			gbcDescription.gridy = 1;
			panel.add(descriptionComponent, gbcDescription);

			addressComponent = new JTextArea("address [ city/address ]");
			addressComponent.setOpaque(false);
			addressComponent.setEditable(false);
			addressComponent.setFocusable(false);
			GridBagConstraints gbcAdress = new GridBagConstraints();
			gbcDescription.gridwidth = 1;
			gbcAdress.insets = new Insets(0, 0, 0, 5);
			gbcAdress.fill = GridBagConstraints.HORIZONTAL;
			gbcAdress.gridx = 0;
			gbcAdress.gridy = 2;
			panel.add(addressComponent, gbcAdress);
			addressComponent.setColumns(10);

			priceComponent = new JTextArea("price");
			priceComponent.setOpaque(false);
			priceComponent.setEditable(false);
			priceComponent.setFocusable(false);
			priceComponent.setColumns(4);

			GridBagConstraints gbcPrice = new GridBagConstraints();
			gbcPrice.fill = GridBagConstraints.HORIZONTAL;
			gbcPrice.insets = new Insets(0, 0, 0, 5);
			gbcPrice.gridx = 1;
			gbcPrice.gridy = 2;
			panel.add(priceComponent, gbcPrice);

			infoButton = new JButton("Info. ");
			infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			GridBagConstraints gbcInfoButton = new GridBagConstraints();
			gbcInfoButton.fill = GridBagConstraints.HORIZONTAL;
			gbcInfoButton.gridheight = 3;
			gbcInfoButton.gridx = 2;
			gbcInfoButton.gridy = 0;	
			gbcInfoButton.insets = new Insets(5, 5, 0, 0);
			panel.add(infoButton, gbcInfoButton);

		}

		private void updateData(CellDetails rowContent, boolean isSelected, JTable table) {
			rowContent.setTableDetailsCell(this);
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
			titleComponent.setText(rowContent.getOffer().getRuralHouse().getName() + "\n" + date.format(rowContent.getOffer().getStartDate()) + " - " + date.format(rowContent.getOffer().getEndDate()));
			String description = rowContent.getOffer().getRuralHouse().getDescription();
			//Limit to 300 char, if the text exceeds the limit it will place ' (...)'
			if(description.length() >= 300) {
				description = String.format("%1.140s%1.140s", description, " (...)");
			}
			descriptionComponent.setText(description);
			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
			addressComponent.setText(rowContent.getOffer().getRuralHouse().getCity() + " " + rowContent.getOffer().getRuralHouse().getAddress());
			priceComponent.setText(currencyFormatter.format(rowContent.getOffer().getPrice()));

			infoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(rowContent.getOffer().toString());					
					OfferInfoDialog dialog = new OfferInfoDialog(frame, rowContent);
					dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					dialog.validate();
					//Set location relative to the parent frame. ALWAYS BEFORE SHOWING THE DIALOG.
					dialog.setLocationRelativeTo(frame);
					dialog.setVisible(true);
				}
			});

			if (isSelected) {
				panel.setBackground(table.getSelectionBackground());
			}else{
				panel.setBackground(table.getBackground());
			}
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			updateData((CellDetails) value, true, table);
			return panel;
		}

		public Object getCellEditorValue() {
			return null;
		}

		@Override
		public boolean isCellEditable(EventObject e){
			return true;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			updateData((CellDetails) value, isSelected, table);
			return panel;
		}

		public JPanel getPanel() {
			return panel;
		}

		public void setPanel(JPanel panel) {
			this.panel = panel;
		}

		public JTextArea getDescriptionComponent() {
			return descriptionComponent;
		}

		public void setDescriptionComponent(JTextArea descriptionComponent) {
			this.descriptionComponent = descriptionComponent;
		}

		public JTextArea getAddressComponent() {
			return addressComponent;
		}

		public void setAddressComponent(JTextArea addressComponent) {
			this.addressComponent = addressComponent;
		}

		public JTextArea getPriceComponent() {
			return priceComponent;
		}

		public void setPriceComponent(JTextArea priceComponent) {
			this.priceComponent = priceComponent;
		}

		public JButton getInfoButton() {
			return infoButton;
		}

		public void setInfoButton(JButton infoButton) {
			this.infoButton = infoButton;
		}

	}

	public class CellDetails {

		private TableCellComponent tableCellComponent;
		//		private String description;
		//		private String city;
		//		private String address;
		//		private Date startDate;
		//		private Date endDate;
		//		private double price;
		private Offer offer;

		//startDate endDate description address(city/address) price Image Offer
		public CellDetails(Offer offer) {
			this.offer = offer;
		}

		public TableCellComponent getTableDetailsCell() {
			return tableCellComponent;
		}

		public void setTableDetailsCell(TableCellComponent tableCellComponent) {
			this.tableCellComponent = tableCellComponent;
		}

		public Offer getOffer() {
			return offer;
		}

		public void setOffer(Offer offer) {
			this.offer = offer;
		}
		//
		//		public Date getStartDate() {
		//			return offer.getStartDate();
		//		}
		//
		//		public void setStartDate(Date startDate) {
		//			offer.setStartDate(startDate);
		//		}
		//
		//		public Date getEndDate() {
		//			return offer.getEndDate();
		//		}
		//
		//		public void setEndDate(Date endDate) {
		//			offer.getEndDate();
		//		}
		//		
		//		public String getName() {
		//			return offer.getRuralHouse().getName();
		//		}
		//		
		//		public void setName(String name) {
		//			offer.getRuralHouse().setName(name);
		//		}
		//
		//		public String getDescription() {
		//			return offer.getRuralHouse().getDescription();
		//		}
		//
		//		public void setDescription(String description) {
		//			offer.getRuralHouse().setDescription(description);
		//		}
		//
		//		public String getCity() {
		//			return offer.getRuralHouse().getCity().getName();
		//		}
		//
		//		public void setCity(String cityName) {
		//			offer.getRuralHouse().getCity().setName(cityName);
		//		}
		//
		//		public String getAddress() {
		//			return offer.getRuralHouse().getAddress();
		//		}
		//
		//		public void setAddress(String address) {
		//			offer.getRuralHouse().setAddress(address);
		//		}
		//
		//		public double getPrice() {
		//			return offer.getPrice();
		//		}
		//
		//		public void setPrice(double price) {
		//			offer.setPrice(price);
		//		}

	}

	public class FormattedTextFieldVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
			if (input instanceof JFormattedTextField) {
				JFormattedTextField ftf = (JFormattedTextField)input;
				AbstractFormatter formatter = ftf.getFormatter();
				if (formatter != null) {
					String text = ftf.getText();
					try {
						formatter.stringToValue(text);
						return true;
					} catch (ParseException pe) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public boolean shouldYieldFocus(JComponent input) {
			return verify(input);
		}

	}

}
