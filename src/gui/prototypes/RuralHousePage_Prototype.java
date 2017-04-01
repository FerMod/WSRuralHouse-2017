package gui.prototypes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import java.awt.Label;

public class RuralHousePage_Prototype {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RuralHousePage_Prototype window = new RuralHousePage_Prototype();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RuralHousePage_Prototype() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 631, 538);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 258, 247);
		frame.getContentPane().add(panel);
		
		JTextPane txtpnavatarAqu = new JTextPane();
		txtpnavatarAqu.setBackground(UIManager.getColor("Button.background"));
		txtpnavatarAqu.setText("[Avatar here]");
		panel.add(txtpnavatarAqu);
		
		JTextPane txtpnNombreDeLa = new JTextPane();
		txtpnNombreDeLa.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtpnNombreDeLa.setBackground(UIManager.getColor("Button.background"));
		txtpnNombreDeLa.setText("Name of the rural house");
		txtpnNombreDeLa.setBounds(345, 27, 235, 30);
		frame.getContentPane().add(txtpnNombreDeLa);
		
		JTextPane txtpnCiudad = new JTextPane();
		txtpnCiudad.setBackground(UIManager.getColor("Button.background"));
		txtpnCiudad.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtpnCiudad.setText("City");
		txtpnCiudad.setBounds(411, 54, 42, 20);
		frame.getContentPane().add(txtpnCiudad);
		
		table = new JTable();
		table.setBounds(10, 466, 552, -182);
		frame.getContentPane().add(table);
		
		JTextPane txtpndescripcinDeLa = new JTextPane();
		txtpndescripcinDeLa.setText("[Description of rural house]");
		txtpndescripcinDeLa.setBounds(278, 99, 327, 159);
		frame.getContentPane().add(txtpndescripcinDeLa);
		
		JButton btnNewButton = new JButton("Query prices");
		btnNewButton.setBounds(10, 459, 286, 30);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("View offers by date");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_1.setBounds(319, 459, 286, 30);
		frame.getContentPane().add(btnNewButton_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		tabbedPane.setBounds(10, 282, 595, 166);
		frame.getContentPane().add(tabbedPane);
		
		Label label = new Label("[Content of reviews here]");
		tabbedPane.addTab("Reviews", null, label, null);
		
		Label label_1 = new Label("[Ubication of the rural house]");
		tabbedPane.addTab("Map", null, label_1, null);
		
	}
}
