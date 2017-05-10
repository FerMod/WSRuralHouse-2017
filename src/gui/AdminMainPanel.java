package gui;

import javax.swing.DefaultComboBoxModel;
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

public class AdminMainPanel extends JPanel {
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private DefaultComboBoxModel<RuralHouse> ruralHouses = new DefaultComboBoxModel<RuralHouse>();
	private Review.ReviewState rS;

	/**
	 * Create the panel.
	 */
	public AdminMainPanel() {
		setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 39, 276, 36);
		initializeComboBox();
		comboBox.setModel(ruralHouses);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RuralHouse rh = (RuralHouse) comboBox.getSelectedItem();
			}
		});
		add(comboBox);
		
		
		
		
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(10, 203, 418, 131);
		add(editorPane);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Approved");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rS = ReviewState.APPROVED;
			}
		});
		buttonGroup.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setBounds(10, 135, 73, 38);
		add(rdbtnNewRadioButton);
		
		
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Rejected");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rS = ReviewState.REJECTED;
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setBounds(91, 135, 73, 38);
		add(rdbtnNewRadioButton_1);
		
		JLabel lblCasaRuralA = new JLabel("Casa rural a revisar");
		lblCasaRuralA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCasaRuralA.setBounds(10, 14, 276, 24);
		add(lblCasaRuralA);
		
		JLabel lblEstadoDeRevisin = new JLabel("Estado de revisi\u00F3n");
		lblEstadoDeRevisin.setBounds(10, 110, 144, 24);
		add(lblEstadoDeRevisin);
		
		JLabel lblDescripcinDeLa = new JLabel("Descripci\u00F3n de la revisi\u00F3n");
		lblDescripcinDeLa.setBounds(10, 178, 214, 14);
		add(lblDescripcinDeLa);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setBounds(273, 342, 155, 23);
		add(btnNewButton);
		
		buttonGroup.add(rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton_1);

	}
	
	public void initializeComboBox() {
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHouses();
		
		for(RuralHouse rh : rhs) {
			ruralHouses.addElement(rh);
			rhs.remove(rh);
		}
	}
}
