package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.List;

public class ClientView extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView frame = new ClientView();
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
	public ClientView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 634, 425);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("Button.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane txtpnBuscadorDeOfertas = new JTextPane();
		txtpnBuscadorDeOfertas.setBackground(UIManager.getColor("Button.background"));
		txtpnBuscadorDeOfertas.setFont(new Font("Tahoma", Font.PLAIN, 34));
		txtpnBuscadorDeOfertas.setText("BUSCADOR DE OFERTAS");
		txtpnBuscadorDeOfertas.setBounds(117, 11, 380, 47);
		contentPane.add(txtpnBuscadorDeOfertas);
		
		textField = new JTextField();
		textField.setBounds(117, 70, 380, 23);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JSlider slider = new JSlider();
		slider.setBounds(10, 156, 97, 23);
		contentPane.add(slider);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 193, 21, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(79, 193, 21, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JTextPane txtpnPorPrecio = new JTextPane();
		txtpnPorPrecio.setText("Precio");
		txtpnPorPrecio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtpnPorPrecio.setBackground(SystemColor.menu);
		txtpnPorPrecio.setBounds(10, 139, 59, 14);
		contentPane.add(txtpnPorPrecio);
		
		JTextPane txtpnMin = new JTextPane();
		txtpnMin.setText("Min");
		txtpnMin.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtpnMin.setBackground(SystemColor.menu);
		txtpnMin.setBounds(10, 176, 41, 20);
		contentPane.add(txtpnMin);
		
		JTextPane txtpnMax = new JTextPane();
		txtpnMax.setText("Max");
		txtpnMax.setFont(new Font("Tahoma", Font.PLAIN, 9));
		txtpnMax.setBackground(SystemColor.menu);
		txtpnMax.setBounds(79, 176, 41, 20);
		contentPane.add(txtpnMax);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(419, 99, 156, 23);
		contentPane.add(comboBox);
		
		JTextPane txtpnTipo = new JTextPane();
		txtpnTipo.setBackground(UIManager.getColor("Button.background"));
		txtpnTipo.setForeground(Color.BLACK);
		txtpnTipo.setText("Tipo:");
		txtpnTipo.setBounds(378, 99, 31, 20);
		contentPane.add(txtpnTipo);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(499, 70, 76, 23);
		contentPane.add(btnBuscar);
		
		JButton btnNewButton = new JButton("Fecha de entrada");
		btnNewButton.setBounds(117, 99, 129, 23);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("Fecha de entrada");
		button.setBounds(245, 99, 129, 23);
		contentPane.add(button);
		
		JLabel lblComodidades = new JLabel("Comodidades");
		lblComodidades.setBounds(10, 224, 86, 14);
		contentPane.add(lblComodidades);
		
		Checkbox checkbox = new Checkbox("Wi-Fi");
		checkbox.setBounds(10, 244, 41, 22);
		contentPane.add(checkbox);
		
		Checkbox checkbox_1 = new Checkbox("Buffet");
		checkbox_1.setBounds(10, 298, 49, 22);
		contentPane.add(checkbox_1);
		
		Checkbox checkbox_2 = new Checkbox("Piscina");
		checkbox_2.setBounds(10, 272, 49, 22);
		contentPane.add(checkbox_2);
		
		Checkbox checkbox_3 = new Checkbox("Otros...");
		checkbox_3.setBounds(10, 326, 95, 22);
		contentPane.add(checkbox_3);
		
		List list = new List();
		list.setBounds(117, 139, 458, 221);
		contentPane.add(list);
	}
}
