package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.util.EventObject;
import java.util.Hashtable;
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
import gui.components.TextPrompt;
import gui.debug.MouseXYLabel;

public class ClientMainPanel extends JPanel {

	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	private static final long serialVersionUID = 3063325462028186709L;

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
	private JButton btnAdd, btnEdit, btnRemove, btnDetails;
	private JSlider priceSlider;

	private NumberFormat priceFormat;

	/**
	 * Create the panel.
	 */
	public ClientMainPanel() {
		
		setLayout(new GridBagLayout());

		setupNumberFormat();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

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

		gbc.ipady = 10;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 5;
		gbc.insets = new Insets(20, 5, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(getSearchField(), gbc);

		gbc.weightx = 0.1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 10, -5, 10);
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(getMaxPriceField(), gbc);

		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 5, 2, 0);
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(getPriceSlider(), gbc);

		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;

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

		gbc.ipady = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 2;
		gbc.gridheight = 3;
		gbc.insets = new Insets(0, 10, 10, 10);
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridwidth = 3;
		gbc.gridx = 2;
		gbc.gridy = 1;
		add(getTableScrollPanel(), gbc);

		gbc.anchor = GridBagConstraints.PAGE_START;		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.weighty = 0;
		gbc.gridwidth = 5;

		gbc.insets = new Insets(10, 100, 10, 100);
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(getBtnDetails(), gbc);

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

		//[FIXME] Just a prank, bro.
		tableModel.setRandomImages();
		updateRowHeights();

	}

	private JSlider getPriceSlider() {
		if(priceSlider == null) {
			double maxPrice = getRuralHouseMaxPrice();
			priceSlider = new JSlider(JSlider.HORIZONTAL, 0, (int)(maxPrice* 100), (int)(maxPrice* 100));
			priceSlider.setMajorTickSpacing((priceSlider.getMaximum()*25)/100); //each 25% of the value
			priceSlider.setMinorTickSpacing((priceSlider.getMajorTickSpacing()*10)/100); //each 10% of the 25% of the value
			priceSlider.setLabelTable(getSliderLabelTable(0, maxPrice));
			priceSlider.setPaintTicks(true);
			priceSlider.setPaintLabels(true);
			priceSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(e.getSource() == priceSlider) {
						maxPriceField.setValue(priceSlider.getValue()/100);
						//refreshTableContent((Number)minPriceField.getValue(), (Number)maxPriceField.getValue());
						refreshTableContent(0, (Number)maxPriceField.getValue());
					}
				}
			});
		}
		return priceSlider;
	}

	private Hashtable<Integer, JLabel> getSliderLabelTable(double minPrice, double maxPrice) {

		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(getDefaultLocale());

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();		
		labelTable.put(getPriceSlider().getMinimum(), new JLabel(currencyFormatter.format(minPrice)));
		labelTable.put(getPriceSlider().getMaximum()/2, new JLabel(currencyFormatter.format((maxPrice-minPrice)/2)));
		labelTable.put(getPriceSlider().getMaximum(), new JLabel(currencyFormatter.format(maxPrice)));
		return labelTable;
	}

	/**XXX TEMPORAL. TODO REMOVE!!
	 * This must be replaced with the original one.
	 * @return
	 */
	@Deprecated
	private double getRuralHouseMaxPrice(){
		return 800.00;
	}

	//	private JFormattedTextField getMinPriceField() {
	//		if(minPriceField == null) {
	//			if(priceFormat == null) {
	//				setupNumberFormat();
	//			}
	//			minPriceField = new JFormattedTextField(priceFormat);
	//			minPriceField.setColumns(4);
	//			minPriceField.setValue(0);
	//			minPriceField.setInputVerifier(new FormattedTextFieldVerifier());
	//
	//			///////////
	//			// minPriceField.setVisible(false);
	//			///////////
	//
	//
	//			minPriceField.addPropertyChangeListener("value", new PropertyChangeListener() {
	//				@Override
	//				public void propertyChange(PropertyChangeEvent e) {
	//					if(e.getPropertyName() == "value" && e.getSource() == minPriceField) {
	//						if(getPriceSlider().getMaximum() != 0) {
	//							updatePriceRange((Number)minPriceField.getValue(), getPriceSlider().getMaximum()/100);
	//
	//						} else {
	//							updatePriceRange((Number)minPriceField.getValue(), 0);
	//						}
	//						//TODO filter table
	//						//refreshTableContent((Number)minPriceField.getValue(), (Number)maxPriceField.getValue());
	//					}
	//				}
	//			});
	//
	//		}
	//		return minPriceField;
	//	}

	//	private JFormattedTextField getMaxPriceField() {
	//		if(maxPriceField == null) {
	//			if(priceFormat == null) {
	//				setupNumberFormat();
	//			}
	//			maxPriceField = new JFormattedTextField(priceFormat);
	//			maxPriceField.setColumns(4);
	//			maxPriceField.setValue(getPriceSlider().getMaximum()/100);
	//			maxPriceField.setInputVerifier(new FormattedTextFieldVerifier());
	//
	//			///////////
	//			// maxPriceField.setFocusable(false);
	//			// maxPriceField.setEditable(false);
	//			// maxPriceField.setOpaque(false);
	//			// maxPriceField.setHorizontalAlignment(SwingConstants.RIGHT);
	//			// maxPriceField.setBorder(new EmptyBorder(0, 0, 0, 0));
	//			///////////
	//
	//
	//			maxPriceField.addPropertyChangeListener("value", new PropertyChangeListener() {
	//				@Override
	//				public void propertyChange(PropertyChangeEvent e) {
	//					if(e.getPropertyName() == "value" && e.getSource() == maxPriceField) {
	//						if(getPriceSlider().getMinimum() != 0) {
	//							// Number oldValue = (Number) e.getOldValue();
	//							// Number newValue = (Number) e.getNewValue();
	//
	//							// if(oldValue.doubleValue() <= newValue.doubleValue()) {
	//							//	 getPriceSlider().setValue(newValue.intValue()/100);
	//							// }
	//
	//							updatePriceRange(getPriceSlider().getMinimum()/100, (Number)maxPriceField.getValue());
	//
	//						} else {
	//							updatePriceRange(0, (Number)maxPriceField.getValue());
	//						}
	//						//TODO filter table
	//						refreshTableContent((Number)minPriceField.getValue(), (Number)maxPriceField.getValue());
	//					}
	//
	//				}
	//			});
	//
	//
	//			//			maxPriceField.addPropertyChangeListener("value", new PropertyChangeListener() {				
	//			//				@Override
	//			//				public void propertyChange(PropertyChangeEvent evt) {	
	//			//					System.out.println(evt.getSource());
	//			//					updatePriceRange((Number)minPriceField.getValue(), (Number)maxPriceField.getValue());
	//			//				}
	//			//			});
	//		}
	//		return maxPriceField;
	//	}

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
		priceFormat = NumberFormat.getCurrencyInstance();
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

				System.out.println("cellDetails.getDescription().contains(text) -> " + cellDetails.getDescription().contains(text));
				if(cellDetails.getDescription().contains(text)) {
					//textArea = cellDetails.getTableDetailsCell().getDescriptionTextArea();
					include = true;
				}

				System.out.println("cellDetails.getAddress().contains(text) -> " + cellDetails.getAddress().contains(text));
				if(cellDetails.getAddress().contains(text)) {
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
				return cellDetails.getPrice() > minPrice.doubleValue() && cellDetails.getPrice() <= maxPrice.doubleValue();
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
			table.setDefaultRenderer(Object.class, new TableCellComponents());
			table.setDefaultEditor(Object.class, new TableCellComponents());

			//When selection changes, provide user with row numbers for both view and model.
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					int row = table.getSelectedRow();
					if (row < 0) {
						//Selection got filtered away.
						btnDetails.setEnabled(false);
					} else {
						btnDetails.setEnabled(true);
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
						JOptionPane.showMessageDialog(null,	"Double clicked the row.\nWhen implemented, more details window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
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

	private JButton getBtnDetails() {
		if (btnDetails == null) {
			btnDetails = new JButton("Details");
			btnDetails.setEnabled(false);
			btnDetails.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null,	"\"Details\" button pressed.\nWhen implemented, more details window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return btnDetails;
	}

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

	private class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private int width;
		private int height;

		//		private String[] columnNames = {"Image", "Description", "Address", "Price"};
		private String[] columnNames = {"Image", "Details"};


		//		private Object[][] data = {
		//				{new ImageIcon("/img/house00.png"), "Description of the house 1 with minor details", "Address 1", new Double(110.2)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 2 with minor details", "Address 2", new Double(154.52)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 3 with minor details", "Address 3", new Double(356.0)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 4 with minor details", "Address 4", new Double(165.4)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 5 with minor details", "Address 5", new Double(170.2)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 6 with minor details", "Address 6", new Double(666.5)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 7 with minor details", "Address 7", new Double(336.6)},
		//				{new ImageIcon("/img/house00.png"), "Description of the house 8 with minor details", "Address 8", new Double(63.1)},
		//		};

		private Object[][] data = {
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 1 with minor details.\nHi", "Address 1",  new Double(110.2))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 2 with minor details.", "Address 2", new Double(154.52))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 3 with minor details", "Direccion 3", new Double(356.0))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 4 with minor details\nHi, more details.", "Address 4", new Double(165.4))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 5 with minor details\nHi", "Direccion 5", new Double(170.2))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 6 with minor details, text.", "Address 6", new Double(666.5))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 7 with minor details\nMore details.", "Address 7", new Double(336.6))},
				{new ImageIcon("/img/house00.png"), new CellDetails("Description of the house 8 with minor details", "ñeñeñe 8", new Double(63.1))},
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
		//			for (int i = 0; i < data.length; i++) {
		//				int randomNum = ThreadLocalRandom.current().nextInt(0, data.length);
		//				System.out.println(images[randomNum]);
		//				setValueAt(i, 0, new ImageIcon(images[randomNum]));
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

	public class TableCellComponents extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{

		private static final long serialVersionUID = 2711709042458345572L;

		JPanel panel;
		JTextArea descriptionTextArea, addressField, priceField;
		JButton infoButton;


		public TableCellComponents() {

			panel = new JPanel();
			panel.setBorder(new EmptyBorder(2, 5, 2, 5));

			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] {128, 34, 0, 2};
			gridBagLayout.rowHeights = new int[] {0, 0, 2};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gridBagLayout);

			descriptionTextArea = new JTextArea();
			descriptionTextArea.setOpaque(false);
			descriptionTextArea.setEditable(false);
			descriptionTextArea.setFocusable(false);
			GridBagConstraints gbcDescriptionTextArea = new GridBagConstraints();
			gbcDescriptionTextArea.gridwidth = 2;
			gbcDescriptionTextArea.insets = new Insets(0, 0, 5, 5);
			gbcDescriptionTextArea.fill = GridBagConstraints.BOTH;
			gbcDescriptionTextArea.gridx = 0;
			gbcDescriptionTextArea.gridy = 0;
			panel.add(descriptionTextArea, gbcDescriptionTextArea);

			addressField = new JTextArea();
			addressField.setOpaque(false);
			addressField.setEditable(false);
			addressField.setFocusable(false);
			GridBagConstraints gbcAdressField = new GridBagConstraints();
			gbcDescriptionTextArea.gridwidth = 1;
			gbcAdressField.insets = new Insets(0, 0, 0, 5);
			gbcAdressField.fill = GridBagConstraints.HORIZONTAL;
			gbcAdressField.gridx = 0;
			gbcAdressField.gridy = 1;
			panel.add(addressField, gbcAdressField);
			addressField.setColumns(10);

			priceField = new JTextArea();
			priceField.setOpaque(false);
			priceField.setEditable(false);
			priceField.setFocusable(false);
			priceField.setColumns(4);
			
			GridBagConstraints gbcPriceField = new GridBagConstraints();
			gbcPriceField.fill = GridBagConstraints.HORIZONTAL;
			gbcPriceField.insets = new Insets(0, 0, 0, 10);
			gbcPriceField.gridx = 1;
			gbcPriceField.gridy = 1;
			panel.add(priceField, gbcPriceField);
			

			infoButton = new JButton("Info. ");
			infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			GridBagConstraints gbcInfoButton = new GridBagConstraints();
			gbcInfoButton.fill = GridBagConstraints.HORIZONTAL;
			gbcInfoButton.gridheight = 3;
			gbcInfoButton.gridx = 2;
			gbcInfoButton.gridy = 0;	
			gbcInfoButton.insets = new Insets(5, 5, 5, 5);
			panel.add(infoButton, gbcInfoButton);

		}

		private void updateData(CellDetails rowContent, boolean isSelected, JTable table) {
			rowContent.setTableDetailsCell(this);
			descriptionTextArea.setText(rowContent.getDescription());
			addressField.setText(rowContent.getAddress());
			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(getDefaultLocale());
			priceField.setText(currencyFormatter.format(rowContent.getPrice()));

			infoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Boton");

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

		public JTextArea getDescriptionTextArea() {
			return descriptionTextArea;
		}

		public void setDescriptionTextArea(JTextArea descriptionTextArea) {
			this.descriptionTextArea = descriptionTextArea;
		}

		public JTextArea getAddressField() {
			return addressField;
		}

		public void setAddressField(JTextArea addressField) {
			this.addressField = addressField;
		}

		public JTextArea getPriceField() {
			return priceField;
		}

		public void setPriceField(JTextArea priceField) {
			this.priceField = priceField;
		}

		public JButton getInfoButton() {
			return infoButton;
		}

		public void setInfoButton(JButton infoButton) {
			this.infoButton = infoButton;
		}

	}

	public class CellDetails {

		private TableCellComponents tableDetailsCell;
		private String description;
		private String address;
		private Double price;

		public CellDetails(String description, String address, double price) {
			this.description = description;
			this.address = address;
			this.price = price;
		}

		public void setTableDetailsCell(TableCellComponents tableDetailsCell) {
			this.tableDetailsCell = tableDetailsCell;
		}

		public TableCellComponents getTableDetailsCell() {
			return tableDetailsCell;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

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
