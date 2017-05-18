package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import gui.components.table.CellComponent;
import gui.components.table.CustomTableModel;
import gui.components.table.cell.BookingsCellComponent;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

public class BookingsTablePanel extends JPanel {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -3609329976279893020L;

	private JScrollPane scrollPane;
	private JTable table;
	private CustomTableModel tableModel;
	private TableRowSorter<CustomTableModel> sorter;
	private JFrame parentFrame;
	private CellComponent<Booking> selectedComponent;

	/**
	 * Create the panel.
	 * @param parentFrame 
	 */
	public BookingsTablePanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;

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

			List<Booking> bookingList = MainWindow.getBusinessLogic().getBookings((Client)MainWindow.user);

			List<CellComponent<Booking>> list = new Vector<>();

			for (Booking booking : bookingList) {
				list.add(new CellComponent<Booking>(booking));
			}

			list.stream().forEachOrdered(e -> System.out.println(e.getElement().getOffer()));
			tableModel = new CustomTableModel(list);
			sorter = new TableRowSorter<CustomTableModel>(tableModel);
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
			BookingsCellComponent bookingsTable = new BookingsCellComponent();
			table.setDefaultRenderer(Object.class, bookingsTable);
			table.setDefaultEditor(Object.class, bookingsTable);

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
		if(table.getColumnModel().getColumnCount() > 1) {
			for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
				TableColumn column = model.getColumn(columnIndex);
				column.setPreferredWidth((int) (percentages[columnIndex] * factor));
			}
		}
	}
	
}
