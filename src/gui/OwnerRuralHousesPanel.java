package gui;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OwnerRuralHousesPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Create the panel.
	 */
	public OwnerRuralHousesPanel() {
		
		JLabel lblMisCasas = new JLabel("Mis casas");
		lblMisCasas.setBounds(25, 11, 95, 31);
		
		JComboBox comboBox = new JComboBox();
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
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(25, 274, 260, 38);
		add(comboBox_1);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Editar casa");
		chckbxNewCheckBox.setBounds(18, 152, 102, 23);
		add(chckbxNewCheckBox);
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.setBounds(154, 568, 131, 26);
		add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Crear oferta");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		

	}
}
