package gui;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class AdminMainPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public AdminMainPanel() {
		setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 39, 276, 36);
		add(comboBox);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(10, 203, 418, 131);
		add(editorPane);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Approved");
		rdbtnNewRadioButton.setBounds(10, 135, 73, 38);
		add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Rejected");
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

	}
}
