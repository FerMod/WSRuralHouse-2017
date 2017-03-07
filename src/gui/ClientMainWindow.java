package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.ComponentOrientation;

public class ClientMainWindow extends JFrame {

	private static final long serialVersionUID = 3063325462028186709L;
	private JTextField textField;
	private JTable table;

	String[] columnNames = {"Rural House", "Name", "Desc", "# of Years", "Vegetarian"};


	Object[][] data = {
			{"Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false)},
			{"John", "Doe", "Rowing", new Integer(3), new Boolean(true)},
			{"Sue", "Black", "Knitting", new Integer(2), new Boolean(false)},
			{"Jane", "White", "Speed reading", new Integer(20), new Boolean(true)},
			{"Joe", "Brown", "Pool", new Integer(10), new Boolean(false)}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMainWindow frame = new ClientMainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientMainWindow() {
		getContentPane().setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{420, 0};
		gridBagLayout.rowHeights = new int[]{30, 176, 33, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		JPanel topPanel = new JPanel();
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.anchor = GridBagConstraints.NORTH;
		gbc_topPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_topPanel.insets = new Insets(0, 0, 5, 0);
		gbc_topPanel.gridx = 0;
		gbc_topPanel.gridy = 0;
		getContentPane().add(topPanel, gbc_topPanel);

		textField = new JTextField();
		topPanel.add(textField);
		textField.setColumns(10);

		JPanel centerPanel = new JPanel();
		GridBagConstraints gbc_centerPanel = new GridBagConstraints();
		gbc_centerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_centerPanel.fill = GridBagConstraints.BOTH;
		gbc_centerPanel.gridx = 0;
		gbc_centerPanel.gridy = 1;
		getContentPane().add(centerPanel, gbc_centerPanel);

		JPanel bottomPanel = new JPanel();
		GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
		gbc_bottomPanel.anchor = GridBagConstraints.NORTH;
		gbc_bottomPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_bottomPanel.gridx = 0;
		gbc_bottomPanel.gridy = 2;
		getContentPane().add(bottomPanel, gbc_bottomPanel);

		JButton btnNewButton = new JButton("New button");
		bottomPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		bottomPanel.add(btnNewButton_1);

		//		table = new JTable(null, columnNames);
		//        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//        table.setFillsViewportHeight(true);
		//	
		//        panel_2.add(table);

	}

}
