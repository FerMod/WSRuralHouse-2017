package gui.user.client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import domain.Booking;
import domain.Client;
import gui.components.CustomTable;
import gui.components.table.CellComponent;
import gui.components.table.CustomTableModel;
import gui.components.table.cell.component.BookingsComponent;
import gui.user.MainWindow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BookingsTablePanel extends JPanel implements PropertyChangeListener {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -3609329976279893020L;

	private JScrollPane tableScrollPane;
	private JTable bookingsTable;
	private CustomTableModel tableModel;
	private TableRowSorter<CustomTableModel> sorter;
	private JFrame parentFrame;
	private JLabel lblBookings;

	private static PropertyChangeSupport pcs = new PropertyChangeSupport(BookingsTablePanel.class);

	/**
	 * Create the panel.
	 * 
	 * @param parentFrame the parent frame
	 * 
	 */
	public BookingsTablePanel(JFrame parentFrame) {

		this.parentFrame = parentFrame;

		initComponents();

		addPropertyChangeListener(this);
	}

	private void initComponents() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		setLayout(gridBagLayout);

		GridBagConstraints gbcLblBookings = new GridBagConstraints();
		gbcLblBookings.anchor = GridBagConstraints.WEST;
		gbcLblBookings.insets = new Insets(10, 5, 0, 0);
		gbcLblBookings.gridx = 1;
		gbcLblBookings.gridy = 0;
		add(getLblBookings(), gbcLblBookings);

		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(5, 5, 0, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridx = 1;
		gbcScrollPane.gridy = 1;
		add(getTableScrollPane(), gbcScrollPane);

	}

	private void updateRowHeights() {
		for (int row = 0; row < bookingsTable.getRowCount(); row++) {
			int rowHeight = bookingsTable.getRowHeight();
			for (int column = 0; column < bookingsTable.getColumnCount(); column++) {
				Component comp = bookingsTable.prepareRenderer(bookingsTable.getCellRenderer(row, column), row, column);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
			}
			bookingsTable.setRowHeight(row, rowHeight);
		}
	}

	private JLabel getLblBookings() {
		if (lblBookings == null) {
			lblBookings = new JLabel("Bookings:");
		}
		return lblBookings;
	}

	private JScrollPane getTableScrollPane() {
		if (tableScrollPane == null) {
			tableScrollPane = new JScrollPane(getBookingsTable());
			updateRowHeights();
		}
		return tableScrollPane;
	}

	private JTable getBookingsTable() {

		boolean needsUpdate = true;
		List<Booking> bookingList = MainWindow.getBusinessLogic().getBookings((Client)MainWindow.user);

		if(bookingsTable != null) {
			needsUpdate = bookingsTable.getRowCount() != bookingList.size();
		}

		if(needsUpdate) {

			List<CellComponent<Booking>> bookingComponentList = new Vector<>();
			List<ImageIcon> imageVector = new Vector<ImageIcon>();
			for (Booking booking : bookingList) {
				imageVector.add(booking.getOffer().getRuralHouse().getImage(0));
				bookingComponentList.add(new CellComponent<Booking>(booking));
			}

			bookingComponentList.stream().forEachOrdered(e -> System.out.println(e.getElement().getOffer()));

			tableModel = new CustomTableModel(bookingComponentList);
			sorter = new TableRowSorter<CustomTableModel>(tableModel);
			bookingsTable = new CustomTable(tableModel);
			
			bookingsTable.setRowSorter(sorter);
			bookingsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
			bookingsTable.getTableHeader().setReorderingAllowed(false);
			// table.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			bookingsTable.setFocusable(false);
			bookingsTable.getTableHeader().setUI(null); //Hide the header
			bookingsTable.setShowVerticalLines(false);
			bookingsTable.setIntercellSpacing(new Dimension(0, 1));
			bookingsTable.setUpdateSelectionOnSort(true);
			// DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			// centerRenderer.setHorizontalAlignment( JLabel.CENTER );
			// table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );

			//Center the content in the column
			DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer();
			centerCellRenderer.setHorizontalAlignment(JLabel.CENTER);	
			// table.setDefaultRenderer(Double.class, centerCellRenderer);
			// table.setDefaultRenderer(String.class, centerCellRenderer);
			// table.getColumnModel().getColumn(1).setCellRenderer(leftCellRenderer);

			setTableColumnWidthPercentages(bookingsTable, new double[] {1.0});
			bookingsTable.setDefaultRenderer(Object.class, new BookingsComponent(parentFrame));
			bookingsTable.setDefaultEditor(Object.class, new BookingsComponent(parentFrame));

			// When selection changes, provide user with row numbers for both view and model.
			bookingsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					int row = bookingsTable.getSelectedRow();
					if (row < 0) {
						//Selection got filtered away.
						//btnDetails.setEnabled(false);
					} else {
						//btnDetails.setEnabled(true);
						int modelRow = bookingsTable.convertRowIndexToModel(row);
						System.out.println(String.format("Selected Row in view: %d. Selected Row in model: %d.", row, modelRow));
					}
				}

			});

			/*
			bookingsTable.addFocusListener(new FocusListener() {				
				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					bookingsTable.clearSelection();
				}
			});
			 */

			/*
			bookingsTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					// JTable table =(JTable) me.getSource();
					// Point p = me.getPoint();
					// int row = table.rowAtPoint(p);
					if (me.getClickCount() == 2) {
						JOptionPane.showMessageDialog(null,	"Double clicked the row.\nWhen implemented, info window will show...", "WIP", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			 */

		}
		return bookingsTable;
	}

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
		if(table.getColumnModel().getColumnCount() > 1) {
			for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
				TableColumn column = model.getColumn(columnIndex);
				column.setPreferredWidth((int) (percentages[columnIndex] * factor));
			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.addPropertyChangeListener(propertyChangeListener);
	}

	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		pcs.removePropertyChangeListener(propertyChangeListener);
	}

	public static PropertyChangeSupport getPropertyChangeSupport() {
		return pcs;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("rowDeleted")) {
			System.out.println("### Booking Canceled ###");
			int index = bookingsTable.getSelectedRow();
			((CustomTableModel)bookingsTable.getModel()).fireTableRowsDeleted(index, index);
			JOptionPane.showMessageDialog(parentFrame,	"Booking canceled successfully.", "Booking canceled", JOptionPane.INFORMATION_MESSAGE);
		} else if(evt.getPropertyName().equals("rowInserted")) {
			System.out.println("### Booking Added ###");
			((CustomTableModel)bookingsTable.getModel()).fireTableDataChanged();
			getTableScrollPane();
			updateRowHeights();
			JOptionPane.showMessageDialog(parentFrame,	"Offer booked successfully.", "Booking Information", JOptionPane.INFORMATION_MESSAGE);
		}
	}




}
