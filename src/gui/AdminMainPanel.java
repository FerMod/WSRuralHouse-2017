package gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.ButtonGroup;

import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class AdminMainPanel extends JPanel {
	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private DefaultComboBoxModel<RuralHouse> ruralHouses = new DefaultComboBoxModel<RuralHouse>();
	private Review.ReviewState rS;
	private RuralHouse rh;
	private String description;

	/**
	 * Create the panel.
	 */
	public AdminMainPanel(JFrame frame) {
		this.frame = frame;
		initializeComboBox();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{107, 105, 155, 0};
		gridBagLayout.rowHeights = new int[]{24, 36, 24, 38, 14, 131, 23, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(ruralHouses);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rh = (RuralHouse) comboBox.getSelectedItem();
			}
		});

		JLabel lblCasaRuralA = new JLabel("Casa rural a revisar");
		lblCasaRuralA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblCasaRuralA = new GridBagConstraints();
		gbc_lblCasaRuralA.fill = GridBagConstraints.BOTH;
		gbc_lblCasaRuralA.insets = new Insets(0, 0, 5, 0);
		gbc_lblCasaRuralA.gridwidth = 3;
		gbc_lblCasaRuralA.gridx = 0;
		gbc_lblCasaRuralA.gridy = 0;
		add(lblCasaRuralA, gbc_lblCasaRuralA);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);



		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Rejected");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rS = ReviewState.REJECTED;
			}
		});



		JRadioButton rdbtnNewRadioButton = new JRadioButton("Approved");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rS = ReviewState.APPROVED;
			}
		});

		JLabel lblEstadoDeRevisin = new JLabel("Estado de revisi\u00F3n");
		GridBagConstraints gbc_lblEstadoDeRevisin = new GridBagConstraints();
		gbc_lblEstadoDeRevisin.anchor = GridBagConstraints.WEST;
		gbc_lblEstadoDeRevisin.insets = new Insets(0, 0, 5, 5);
		gbc_lblEstadoDeRevisin.gridwidth = 2;
		gbc_lblEstadoDeRevisin.gridx = 0;
		gbc_lblEstadoDeRevisin.gridy = 2;
		add(lblEstadoDeRevisin, gbc_lblEstadoDeRevisin);
		buttonGroup.add(rdbtnNewRadioButton);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 3;
		add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);

		buttonGroup.add(rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton_1);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 1;
		gbc_rdbtnNewRadioButton_1.gridy = 3;
		add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		buttonGroup.add(rdbtnNewRadioButton_1);

		JLabel lblDescripcinDeLa = new JLabel("Descripci\u00F3n de la revisi\u00F3n");
		GridBagConstraints gbc_lblDescripcinDeLa = new GridBagConstraints();
		gbc_lblDescripcinDeLa.anchor = GridBagConstraints.SOUTH;
		gbc_lblDescripcinDeLa.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDescripcinDeLa.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcinDeLa.gridwidth = 2;
		gbc_lblDescripcinDeLa.gridx = 0;
		gbc_lblDescripcinDeLa.gridy = 4;
		add(lblDescripcinDeLa, gbc_lblDescripcinDeLa);


		JEditorPane editorPane = new JEditorPane();
		GridBagConstraints gbc_editorPane = new GridBagConstraints();
		gbc_editorPane.gridwidth = 3;
		gbc_editorPane.fill = GridBagConstraints.BOTH;
		gbc_editorPane.insets = new Insets(0, 0, 10, 5);
		gbc_editorPane.gridx = 0;
		gbc_editorPane.gridy = 5;
		add(editorPane, gbc_editorPane);
		description = editorPane.getText();

		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rS != null && rh != null) {	
					description = editorPane.getText();
					Review r = rh.getReview();
					r.setDescription(description);
					r.setState(r.getReviewer(), rS);
					//Send the review
					MainWindow.getBusinessLogic().updateReview(rh, r);
					rS = null;
					editorPane.setText("");
					buttonGroup.clearSelection();
					JOptionPane.showMessageDialog(null,	"Se ha enviado la revisión correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);	
					System.out.println("Review of " + rh.toString() + ": " + description);
				} else {	
					JOptionPane.showMessageDialog(null, "No se puede enviar una revisión sin su estado o su casa rural", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.SOUTH;
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 6;
		add(btnNewButton, gbc_btnNewButton);

	}

	public void initializeComboBox() {
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHouses();

		ruralHouses.addElement(null); //For that the user have selected a RuralHouse

		for(RuralHouse rh : rhs) {
			ruralHouses.addElement(rh);
		}

	}
}
