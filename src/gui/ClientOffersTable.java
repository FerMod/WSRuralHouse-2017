package gui;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import domain.Offer;
import gui.components.component.table.CellComponent;
import gui.components.component.table.CellComponentInterface;

public class ClientOffersTable extends AbstractCellEditor implements CellComponentInterface {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 2711709042458345572L;

	private JFrame parentFrame;
	private JTextArea titleComponent, descriptionComponent, addressComponent, priceComponent;
	private JButton infoButton;
	private JPanel panel;
	private CellComponent<Offer> selectedComponent;

	public ClientOffersTable(JFrame frame) {
		this.parentFrame = frame;

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

		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == infoButton && selectedComponent != null) {			
					openOfferDialog(parentFrame, selectedComponent);
				}
			}
		});

	}

	private void updateData(CellComponent<Offer> value, boolean isSelected, JTable table) {
		value.setCellComponentTable(this);
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		titleComponent.setText(value.getElement().getRuralHouse().getName() + "\n" + date.format(value.getElement().getStartDate()) + " - " + date.format(value.getElement().getEndDate()));
		String description = value.getElement().getRuralHouse().getDescription();
		//Limit to 300 char, if the text exceeds the limit it will place ' (...)'
		if(description.length() >= 300) {
			description = String.format("%1.140s%1.140s", description, " (...)");
		}
		descriptionComponent.setText(description);
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
		addressComponent.setText(value.getElement().getRuralHouse().getCity() + " " + value.getElement().getRuralHouse().getAddress());
		priceComponent.setText(currencyFormatter.format(value.getElement().getPrice()));

		selectedComponent = value;

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
		}else{
			panel.setBackground(table.getBackground());
		}

	}

	private void openOfferDialog(JFrame parentFrame, CellComponent<Offer> rowContent) {
		System.out.printf("openDialog(%s, %s)%n", parentFrame.getClass().getSimpleName(), ((Offer)rowContent.getElement()).toString());
		OfferInfoDialog dialog = new OfferInfoDialog(parentFrame, rowContent);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.validate();
		//Set location relative to the parent frame. ALWAYS BEFORE SHOWING THE DIALOG.
		dialog.setLocationRelativeTo(parentFrame);
		dialog.setVisible(true);				
	}

	/* (non-Javadoc)
	 * @see gui.components.table.CellComponentInterface#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		updateData((CellComponent<Offer>) value, true, table);		
		return panel;
	}

	/* (non-Javadoc)
	 * @see gui.components.table.CellComponentInterface#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see gui.components.table.CellComponentInterface#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject e){
		return true;
	}

	/* (non-Javadoc)
	 * @see gui.components.table.CellComponentInterface#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		updateData((CellComponent<Offer>) value, isSelected, table);
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
