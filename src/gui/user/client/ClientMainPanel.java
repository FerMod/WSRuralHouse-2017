package gui.user.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import configuration.ConfigXML;
import domain.Offer;
import domain.Review.ReviewState;
import gui.components.CustomTable;
import gui.components.RightClickMenu;
import gui.components.TextPrompt;
import gui.components.table.CellComponent;
import gui.components.table.CustomTableModel;
import gui.components.table.cell.component.OffersComponent;
import gui.user.MainWindow;

public class ClientMainPanel extends JPanel {

	private static final long serialVersionUID = 3063325462028186709L;

	private JFrame parentFrame;
	//	private JFormattedTextField minPriceField
	private JFormattedTextField maxPriceField;
	private JTextField searchField;
	private JTable offersTable;
	private CustomTableModel tableModel;
	private TableRowSorter<CustomTableModel> sorter;
	//	private JPanel topPanel, bottomPanel;
	private JScrollPane tableScrollPanel;
	//	private GridBagLayout gridBagLayout;
	//	private GridBagConstraints gbcTopPanel, gbcCenterPanel, gbcBottomPanel;
	private JButton btnAdd, btnEdit, btnRemove;
	private JSlider priceSlider;
	private boolean isLogged;

	private NumberFormat priceFormat;

	/**
	 * Create the panel. If the user is logger the window will show different things than the non logged one.
	 * 
	 * @param frame the parent frame
	 * @param isLogged <code>true</code> if the user is logged, <code>false</code> otherwise
	 */
	public ClientMainPanel(JFrame frame, boolean isLogged) {

		this.parentFrame = frame;
		this.isLogged = isLogged;

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
		gbcMaxPriceField.insets = new Insets(5, 10, 0, 10);
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

		updateRowHeights();

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

	}

	private JSlider getPriceSlider() {
		if(priceSlider == null) {
			double maxPrice = MainWindow.getBusinessLogic().getOffersHighestPrice();
			maxPrice = ((int) ((maxPrice + 29) / 30) * 30);
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

		NumberFormat currencyFormatter = ConfigXML.getInstance().getLocale().getNumberFormatter();

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
		priceFormat = ConfigXML.getInstance().getLocale().getNumberFormatter();
		priceFormat.setMinimumIntegerDigits(1);
		priceFormat.setMaximumFractionDigits(2);
	}

	// Create and set up number formats. These objects also parse numbers input by user.
	//	private void setUpFormats(CurrencyLocale currentLocale) {
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
					getOffersTable().clearSelection();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					refreshTableContent(searchField.getText());
					System.out.println("Finished");
					getOffersTable().clearSelection();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					refreshTableContent(searchField.getText());
					System.out.println("Finished");
					getOffersTable().clearSelection();
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
		RowFilter<? super CustomTableModel, ? super Integer> rf = null;
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
			@SuppressWarnings("unchecked")
			@Override
			public boolean include(Entry<? extends Object, ? extends Object> entry) {

				boolean include = false;

				CellComponent<Offer> cellComponent = (CellComponent<Offer>) entry.getValue(1);

				System.out.println("cellDetails.getDescription().contains(text) -> " + cellComponent.getElement().getRuralHouse().getDescription().contains(text));
				if(cellComponent.getElement().getRuralHouse().getDescription().contains(text)) {
					include = true;
				}

				System.out.println("cellDetails.getAddress().contains(text) -> " + cellComponent.getElement().getRuralHouse().getAddress().contains(text));
				if(cellComponent.getElement().getRuralHouse().getAddress().contains(text)) {
					include = true;
				}
				return include;
			}
		};
		sorter.setRowFilter(filter);
	}

	private void refreshTableContent(Number minPrice, Number maxPrice) {
		RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
			@SuppressWarnings("unchecked")
			public boolean include(Entry<? extends Object, ? extends Object> entry) {
				CellComponent<Offer> cellComponent = (CellComponent<Offer>) entry.getValue(1);
				return cellComponent.getElement().getPrice() > minPrice.doubleValue() && cellComponent.getElement().getPrice() <= maxPrice.doubleValue();
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

	// Create and set up number formats. These objects also parse numbers input by user.
	//	private void setUpFormats(CurrencyLocale currentLocale) {
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
			tableScrollPanel = new JScrollPane(getOffersTable());
		}
		return tableScrollPanel;
	}

	private JTable getOffersTable() {
		if(offersTable == null) {

			//TODO Move to another method /////
			List<Offer> activeOffersList = MainWindow.getBusinessLogic().getActiveOffers(ReviewState.APPROVED);

			List<CellComponent<Offer>> offerComponentList = new Vector<>();
			List<ImageIcon> imageVector = new Vector<ImageIcon>();

			for (Offer offer : activeOffersList) {
				imageVector.add(offer.getRuralHouse().getImage(0));
				offerComponentList.add(new CellComponent<Offer>(offer));
			}

			imageVector.stream().forEachOrdered(e -> System.out.println(e.getDescription()));
			///////////////////////////////////

			tableModel = new CustomTableModel(imageVector, offerComponentList);
			sorter = new TableRowSorter<CustomTableModel>(tableModel);
			offersTable = new CustomTable(tableModel);
			offersTable.setRowSorter(sorter);
			offersTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
			offersTable.getTableHeader().setReorderingAllowed(false);
			//table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			offersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			offersTable.setFocusable(false);
			offersTable.getTableHeader().setUI(null); //Hide the header
			offersTable.setShowVerticalLines(false);
			offersTable.setIntercellSpacing(new Dimension(0, 1));
			offersTable.setUpdateSelectionOnSort(true);
			//			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			//			centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			//			table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );

			//Center the content in the column
			DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer();
			centerCellRenderer.setHorizontalAlignment(JLabel.CENTER);	
			//			table.setDefaultRenderer(Double.class, centerCellRenderer);
			//			table.setDefaultRenderer(String.class, centerCellRenderer);
			//table.getColumnModel().getColumn(1).setCellRenderer(leftCellRenderer);

			setTableColumnWidthPercentages(offersTable, new double[] {0.1, 0.9});
			offersTable.setDefaultRenderer(Object.class, new OffersComponent(parentFrame, isLogged));
			offersTable.setDefaultEditor(Object.class, new OffersComponent(parentFrame, isLogged));

			//When selection changes, provide user with row numbers for both view and model.
			offersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					int row = offersTable.getSelectedRow();
					if (row < 0) {
						//Selection got filtered away.
						//btnDetails.setEnabled(false);
					} else {
						//btnDetails.setEnabled(true);
						int modelRow = offersTable.convertRowIndexToModel(row);
						System.out.println(String.format("Selected Row in view: %d. Selected Row in model: %d.", row, modelRow));
					}
				}

			});

			//			offersTable.addFocusListener(new FocusListener() {				
			//				@Override
			//				public void focusGained(FocusEvent e) {
			//				}
			//
			//				@Override
			//				public void focusLost(FocusEvent e) {
			//					offersTable.clearSelection();
			//				}
			//			});

			offersTable.addMouseListener(new MouseAdapter() {
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
		return offersTable;
	}

	private void updateRowHeights() {
		for (int row = 0; row < offersTable.getRowCount(); row++) {
			int rowHeight = offersTable.getRowHeight();
			for (int column = 0; column < offersTable.getColumnCount(); column++) {
				Component comp = offersTable.prepareRenderer(offersTable.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}
			offersTable.setRowHeight(row, rowHeight);
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
	 * @param percentages the widths of the columns as percentages<p>
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
