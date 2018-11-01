package gui.components.table.cell.component;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import configuration.ConfigXML;
import domain.Booking;
import gui.components.table.CellComponent;
import gui.components.table.CellComponentInterface;
import gui.user.MainWindow;
import gui.user.client.BookingsTablePanel;

public class BookingsComponent extends AbstractCellEditor implements CellComponentInterface {

	private static final long serialVersionUID = 2711709042458345572L;

	private JFrame parentFrame;
	private JTextArea titleComponent, descriptionComponent, addressComponent, priceComponent;
	private JButton btnCancelBooking;
	private JPanel panel;
	private CellComponent<Booking> selectedComponent;
	private int index = -1;

	public BookingsComponent(JFrame parent) {

		this.setParentFrame(parent);

		panel = new JPanel();
		panel.setBorder(new EmptyBorder(2, 5, 2, 5));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {128, 34, 0, 2};
		gridBagLayout.rowHeights = new int[] {0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0};
		panel.setLayout(gridBagLayout);

		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(0, 0, 5, 5);
		gbcTitle.fill = GridBagConstraints.HORIZONTAL;
		gbcTitle.gridx = 0;
		gbcTitle.gridy = 0;
		panel.add(getTitleComponent(), gbcTitle);

		GridBagConstraints gbcDescription = new GridBagConstraints();
		gbcDescription.gridwidth = 2;
		gbcDescription.insets = new Insets(5, 0, 5, 5);
		gbcDescription.fill = GridBagConstraints.BOTH;
		gbcDescription.gridx = 0;
		gbcDescription.gridy = 1;
		panel.add(getDescriptionComponent(), gbcDescription);

		GridBagConstraints gbcAdress = new GridBagConstraints();
		gbcAdress.gridwidth = 1;
		gbcAdress.insets = new Insets(0, 0, 0, 5);
		gbcAdress.fill = GridBagConstraints.HORIZONTAL;
		gbcAdress.gridx = 0;
		gbcAdress.gridy = 2;
		panel.add(getAddressComponent(), gbcAdress);

		GridBagConstraints gbcPrice = new GridBagConstraints();
		gbcPrice.fill = GridBagConstraints.HORIZONTAL;
		gbcPrice.insets = new Insets(0, 0, 0, 5);
		gbcPrice.gridx = 1;
		gbcPrice.gridy = 2;
		panel.add(getPriceComponent(), gbcPrice);

		GridBagConstraints gbcCancelButton = new GridBagConstraints();
		gbcCancelButton.anchor = GridBagConstraints.NORTHEAST;
		gbcCancelButton.gridx = 2;
		gbcCancelButton.gridy = 0;	
		gbcCancelButton.insets = new Insets(5, 5, 0, 0);
		panel.add(getBtnCancelBooking(), gbcCancelButton);

	}

	private void updateData(CellComponent<Booking> value, boolean isSelected, JTable table) {
		//value.setCellComponentTable(this);
		selectedComponent = value;
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		titleComponent.setText(value.getElement().getOffer().getRuralHouse().getName() +"\n"
				+ date.format(value.getElement().getStartDate()) + " - " + date.format(value.getElement().getStartDate()));
		String description = "Booked: " + value.getElement().getBookingDate();
		//Limit to 300 char, if the text exceeds the limit it will place ' (...)'
		if(description.length() >= 300) {
			description = String.format("%1.140s%1.140s", description, " (...)");
		}
		descriptionComponent.setText(description);
		NumberFormat currencyFormatter = ConfigXML.getInstance().getLocale().getNumberFormatter();
		addressComponent.setText(value.getElement().getOffer().getRuralHouse().getCity() + " " + value.getElement().getOffer().getRuralHouse().getAddress());
		priceComponent.setText(currencyFormatter.format(value.getElement().getOffer().getPrice()));


		//		if (isSelected) {
		//			panel.setBackground(table.getSelectionBackground());
		//		}else{
		//			panel.setBackground(table.getBackground());
		//		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		index = row;
		updateData((CellComponent<Booking>) value, true, table);	
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject e){
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		index = row;
		updateData((CellComponent<Booking>) value, isSelected, table);
		return panel;
	}
	
	public JFrame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public JTextArea getTitleComponent() {
		if (titleComponent == null) {
			titleComponent = new JTextArea();
			titleComponent.setText("title\nDate: startDate - endDate");
			titleComponent.setOpaque(false);
			titleComponent.setEditable(false);
			titleComponent.setFocusable(false);
			titleComponent.setRows(2);
		}
		return titleComponent;
	}

	public JTextArea getDescriptionComponent() {
		if (descriptionComponent == null) {
			descriptionComponent = new JTextArea("description");
			descriptionComponent.setOpaque(false);
			descriptionComponent.setEditable(false);
			descriptionComponent.setFocusable(false);
			descriptionComponent.setLineWrap(true);
			descriptionComponent.setWrapStyleWord(true);
			descriptionComponent.setRows(3);
		}
		return descriptionComponent;
	}

	public void setDescriptionComponent(JTextArea descriptionComponent) {
		this.descriptionComponent = descriptionComponent;
	}

	public JTextArea getAddressComponent() {
		if (addressComponent == null) {
			addressComponent = new JTextArea("address [ city/address ]");
			addressComponent.setOpaque(false);
			addressComponent.setEditable(false);
			addressComponent.setFocusable(false);
			addressComponent.setColumns(10);
		}
		return addressComponent;
	}

	public void setAddressComponent(JTextArea addressComponent) {
		this.addressComponent = addressComponent;
	}

	public JTextArea getPriceComponent() {
		if (priceComponent == null) {
			priceComponent = new JTextArea("price");
			priceComponent.setOpaque(false);
			priceComponent.setEditable(false);
			priceComponent.setFocusable(false);
			priceComponent.setColumns(4);
		}
		return priceComponent;
	}

	public void setPriceComponent(JTextArea priceComponent) {
		this.priceComponent = priceComponent;
	}

	public JButton getBtnCancelBooking() {
		if(btnCancelBooking == null) {
			btnCancelBooking = new JButton();
			btnCancelBooking.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnCancelBooking.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btnCancelBooking.setBorderPainted(false);
			btnCancelBooking.setIcon(new ImageIcon(BookingsComponent.class.getResource("/img/icons/cancel_button/cancel-offer_default.png")));
			btnCancelBooking.setSelectedIcon(new ImageIcon(BookingsComponent.class.getResource("/img/icons/cancel_button/cancel-offer_default.png")));
			btnCancelBooking.setRolloverIcon(new ImageIcon(BookingsComponent.class.getResource("/img/icons/cancel_button/cancel-offer_hover.png")));
			btnCancelBooking.setRolloverSelectedIcon(new ImageIcon(BookingsComponent.class.getResource("/img/icons/cancel_button/cancel-offer_pressed.png")));
			btnCancelBooking.setPressedIcon(new ImageIcon(BookingsComponent.class.getResource("/img/icons/cancel_button/cancel-offer_pressed.png")));
			btnCancelBooking.setContentAreaFilled(false);
			btnCancelBooking.setDefaultCapable(false);
			btnCancelBooking.setFocusPainted(false);
			btnCancelBooking.setFocusable(false);
			btnCancelBooking.setFocusTraversalKeysEnabled(false);
			btnCancelBooking.setBackground(UIManager.getColor("Button.light"));
			btnCancelBooking.setAlignmentX(Component.CENTER_ALIGNMENT);
			btnCancelBooking.setPreferredSize(new Dimension(16, 16));
			btnCancelBooking.setMinimumSize(new Dimension(16, 16));
			btnCancelBooking.setMaximumSize(new Dimension(16, 16));
			btnCancelBooking.setIconTextGap(0);
			btnCancelBooking.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() == btnCancelBooking && selectedComponent != null) {
						if(cancelQuestion()) {
							Booking canceledBooking = MainWindow.getBusinessLogic().remove(selectedComponent.getElement());
							System.out.println("Canceled booking: " + canceledBooking);
							BookingsTablePanel.getPropertyChangeSupport().firePropertyChange("rowDeleted", index, null);
							//	MainWindow.getPropertyChangeSupport().firePropertyChange("bookingRemoved", selectedComponent, null);
						}
					}
				}
			});	
		}
		return btnCancelBooking;
	}

	public void setBtnCancelBooking(JButton btnCancelBooking) {
		this.btnCancelBooking = btnCancelBooking;
	}

	private boolean cancelQuestion() {
		int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the booking?\nThis cannot undone.", null, JOptionPane.YES_NO_OPTION);
		switch (reply) {
		case JOptionPane.YES_OPTION:	
			MainWindow.user = null;
			return true;
		case JOptionPane.NO_OPTION:
		default:
			return false;
		}
	}

}
