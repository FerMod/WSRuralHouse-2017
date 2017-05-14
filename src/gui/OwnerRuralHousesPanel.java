package gui;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import domain.Admin;
import domain.City;
import domain.Owner;
import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;


import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class OwnerRuralHousesPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private RuralHouse rh;
	private DefaultComboBoxModel<RuralHouse> ruralHousesOfOwner = new DefaultComboBoxModel<RuralHouse>();
	private JComboBox comboBox_1;
	private JComboBox comboBox;
	
	/**
	 * Create the panel.
	 */
	public OwnerRuralHousesPanel(JFrame frame) {
		//initializeRuralHousesComboBox(o); o should be the owner that is using the application
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				City city = (City) comboBox_1.getSelectedItem();
				String address = textField_1.getText();
				String description = textField_2.getText();
				
				rh.setName(name);
				rh.setCity(city);
				rh.setAddress(address);
				rh.setDescription(description);
				Review review = rh.getReview(); 
				Admin reviewer = review.getReviewer();
				review.setState(reviewer, ReviewState.AWAITING_REVIEW); //The state of review pass to be awaiting for edit the rural house
				rh.setReview(review);
				
				MainWindow.getBusinessLogic().update(rh);
				JOptionPane.showMessageDialog(null,	"Se ha actualizado la casa rural correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(154, 568, 131, 26);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Crear oferta");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Here create the booking with the dates and the rural house
			}
		});
		btnNewButton_1.setBounds(154, 112, 131, 23);
		add(btnNewButton_1);
		
		JLabel lblDiaEnt = new JLabel("Fecha in");
		lblDiaEnt.setBounds(25, 116, 46, 14);
		add(lblDiaEnt);
		
		JLabel lblFechaFin = new JLabel("Fecha fin");
		lblFechaFin.setBounds(86, 116, 46, 14);
		add(lblFechaFin);
		
		JLabel lblMisCasas = new JLabel("Mis casas");
		lblMisCasas.setBounds(25, 11, 95, 31);
		
		comboBox = new JComboBox();
		comboBox.setModel(ruralHousesOfOwner);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rh = (RuralHouse) comboBox.getSelectedItem();
				textField.setText(rh.getName()); //Get the name of the rural house selected
				textField_1.setText(rh.getDescription()); //Get the description of the rural house selected
				textField_2.setText(rh.getAddress());	////Get the address of the rural house selected
			}
		});
		comboBox.setBounds(25, 44, 260, 52);
		
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(25, 182, 72, 20);
		
		textField = new JTextField();
		textField.setBounds(25, 213, 260, 20);
		textField.setColumns(10);
		
		JLabel lblDescripcin = new JLabel("Descripci\u00F3n");
		lblDescripcin.setBounds(25, 372, 95, 20);
		
		textField_1 = new JTextField();
		textField_1.setBounds(25, 398, 260, 159);
		textField_1.setColumns(10);
		
		JLabel lblDireccin = new JLabel("Direcci\u00F3n");
		lblDireccin.setBounds(25, 323, 72, 20);
		
		textField_2 = new JTextField();
		textField_2.setBounds(25, 346, 260, 20);
		textField_2.setColumns(10);
		setLayout(null);
		add(lblMisCasas);
		add(comboBox);
		add(lblNombre);
		add(textField);
		add(lblDescripcin);
		add(textField_1);
		add(lblDireccin);
		add(textField_2);
		
		JLabel lblCiudad = new JLabel("Ciudad");
		lblCiudad.setBounds(25, 243, 72, 20);
		add(lblCiudad);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(25, 274, 260, 38);
		add(comboBox_1);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Editar casa");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
					textField.setEnabled(false);
					textField_1.setEnabled(false);
					textField_2.setEnabled(false);
					btnNewButton_1.setEnabled(false);
					btnNewButton.setEnabled(true);
				} else {
					textField.setEnabled(true);
					textField_1.setEnabled(true);
					textField_2.setEnabled(true);
					btnNewButton_1.setEnabled(true);
					btnNewButton.setEnabled(false);
				}
			}
		});
		chckbxNewCheckBox.setBounds(18, 152, 102, 23);
		add(chckbxNewCheckBox);

	}

	private void initializeRuralHousesComboBox(Owner ow) {
			Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHousesOfOwner(ow);

			ruralHousesOfOwner.addElement(null); //For that the owner have selected a RuralHouse

			for(RuralHouse rh : rhs) {
				ruralHousesOfOwner.addElement(rh);
			}	
	}
}
