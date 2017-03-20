package gui.prototypes;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

public class ReviewersView_Prototype extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReviewersView_Prototype frame = new ReviewersView_Prototype();
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
	public ReviewersView_Prototype() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 593, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea txtrHoal = new JTextArea();
		txtrHoal.setText("Insert your review here.");
		txtrHoal.setToolTipText("");
		txtrHoal.setBounds(10, 55, 557, 262);
		contentPane.add(txtrHoal);
		
		JTextPane txtpnReview = new JTextPane();
		txtpnReview.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtpnReview.setBackground(UIManager.getColor("Button.background"));
		txtpnReview.setText("Review");
		txtpnReview.setBounds(10, 24, 79, 20);
		contentPane.add(txtpnReview);
		
		JButton btnNewButton = new JButton("Publish review");
		btnNewButton.setBounds(10, 328, 113, 23);
		contentPane.add(btnNewButton);
		
		JRadioButton rdbtnShowTheName = new JRadioButton("Show my name publicly as an author of the review");
		rdbtnShowTheName.setBounds(177, 328, 390, 23);
		contentPane.add(rdbtnShowTheName);
	}
}
