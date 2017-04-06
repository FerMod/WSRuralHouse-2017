package gui.prototypes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Font;

public class ClientViewBookings_Prototype extends JFrame {

	private static final long serialVersionUID = -8051624733236525210L;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientViewBookings_Prototype frame = new ClientViewBookings_Prototype();
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
	public ClientViewBookings_Prototype() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnVer = new JButton("Ver reserva");
		btnVer.setBounds(10, 64, 115, 29);
		contentPane.add(btnVer);
		
		JButton btnCancelar = new JButton("Cancelar reserva");
		btnCancelar.setBounds(10, 122, 115, 29);
		contentPane.add(btnCancelar);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(135, 64, 268, 176);
		contentPane.add(textPane);
		
		JTextPane txtpnReservas = new JTextPane();
		txtpnReservas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtpnReservas.setBackground(UIManager.getColor("Button.background"));
		txtpnReservas.setText("Reservas");
		txtpnReservas.setBounds(135, 33, 228, 20);
		contentPane.add(txtpnReservas);
		
		JButton btnModificarReserva = new JButton("Modificar reserva");
		btnModificarReserva.setBounds(10, 93, 115, 29);
		contentPane.add(btnModificarReserva);
	}
}
