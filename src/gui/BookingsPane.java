package gui;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import domain.Offer;
import domain.Review.ReviewState;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

public class BookingsPane extends JPanel {
	
	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -3609329976279893020L;
	
	private JScrollPane scrollPane;
	private JTable table;
	private TableModel tableModel;
	private TableRowSorter<TableModel> sorter;

	/**
	 * Create the panel.
	 */
	public BookingsPane() {

		initComponents();
	}
	
	private void initComponents() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 0;
		add(getScrollPane(), gbcScrollPane);
		
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane((Component) getTable());
		}
		return scrollPane;
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

			setTableColumnWidthPercentages(table, new double[]{0.1, 0.9});
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
//					System.out.println(rowContent.getOffer().toString());					
//					OfferInfoDialog dialog = new OfferInfoDialog(frame, rowContent);
//					dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//					dialog.validate();
//					//Set location relative to the parent frame. ALWAYS BEFORE SHOWING THE DIALOG.
//					dialog.setLocationRelativeTo(frame);
//					dialog.setVisible(true);
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
	
}
