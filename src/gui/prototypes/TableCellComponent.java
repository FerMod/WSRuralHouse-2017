package gui.prototypes;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import gui.components.ImagePanel;

public class TableCellComponent extends JPanel {

	private static final long serialVersionUID = -4506206726643163476L;

	private JTextArea descriptionComponent, addressComponent, priceComponent, dateRangeComponent;
	private JButton infoButton;
	private JPanel panel;

	public TableCellComponent() {
		
		panel = this;
		panel.setBorder(new EmptyBorder(2, 5, 2, 5));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {128, 34, 0, 2};
		gridBagLayout.rowHeights = new int[] {0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, 0.0};
		panel.setLayout(gridBagLayout);
		
		dateRangeComponent = new JTextArea();
		dateRangeComponent.setText("startDate - endDate");
		GridBagConstraints gbcDateRange = new GridBagConstraints();
		gbcDateRange.insets = new Insets(0, 0, 5, 5);
		gbcDateRange.fill = GridBagConstraints.HORIZONTAL;
		gbcDateRange.gridx = 0;
		gbcDateRange.gridy = 0;
		panel.add(dateRangeComponent, gbcDateRange);

		descriptionComponent = new JTextArea("description");
		descriptionComponent.setOpaque(true);
		descriptionComponent.setEditable(true);
		descriptionComponent.setFocusable(false);
		GridBagConstraints gbcDescription = new GridBagConstraints();
		gbcDescription.gridwidth = 2;
		gbcDescription.insets = new Insets(5, 0, 5, 5);
		gbcDescription.fill = GridBagConstraints.BOTH;
		gbcDescription.gridx = 0;
		gbcDescription.gridy = 1;
		panel.add(descriptionComponent, gbcDescription);

		addressComponent = new JTextArea("address [ city/address ]");
		addressComponent.setOpaque(true);
		addressComponent.setEditable(true);
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
		priceComponent.setOpaque(true);
		priceComponent.setEditable(true);
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

}
