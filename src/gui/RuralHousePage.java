package gui;

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
import java.util.Date;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import domain.Booking;
import domain.Client;
import domain.Offer;
import domain.RuralHouse;

public class RuralHousePage {

	private JFrame frame;
	private JTable table;
	public JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	public JList list = null;
	
	private RuralHouse rh;
	private Client cl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RuralHouse r = new RuralHouse("Esto es una casa rural", 3, 50.0);
					
					Client c = new Client("fost@gma.com", "fost", "bob");
					RuralHousePage window = new RuralHousePage(r, c);
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
	public RuralHousePage(RuralHouse rh, Client cl) {
		this.rh=rh;
		this.cl=cl;
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
		
		JLabel lblavatarHere = new JLabel("[Avatar here]");
		panel.add(lblavatarHere);
		
		table = new JTable();
		table.setBounds(10, 466, 552, -182);
		frame.getContentPane().add(table);
		
		prepareReview();
		prepareMapXY();
		prepareListOfOffersOfRuralHouse();
		prepareDesc();
		prepareCity();
		prepareRuralHouseTitle();
		prepareFileChooser();
		prepareBtCancel();
		prepareBtBook();
	}	
	
	private void prepareDesc() {
		JTextArea txtrOffers = new JTextArea();
		txtrOffers.setText(rh.getDescription());
		txtrOffers.setEditable(false);
		txtrOffers.setBounds(302, 73, 271, 189);
		frame.getContentPane().add(txtrOffers);
	}
	
	//
	private void prepareCity() {
		JLabel lblNewLabel_1 = new JLabel();
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(365, 52, 174, 14);
		frame.getContentPane().add(lblNewLabel_1);
	}
	
	private void prepareRuralHouseTitle() {
		JLabel lblNewLabel = new JLabel(rh.toString());
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblNewLabel.setBounds(319, 11, 220, 30);
		frame.getContentPane().add(lblNewLabel);
	}
	
	private void prepareListOfOffersOfRuralHouse() {
		Vector<Integer> r = new Vector<Integer>();
		r.add(3);
		list = new JList(r);
		tabbedPane.addTab("Offers", null, list, null);
	}
	
	private void prepareMapXY() {
		Label label_1 = new Label("[Ubication of the rural house]");
		tabbedPane.addTab("Map", null, label_1, null);
	}
	
	private void prepareReview() {
		Label label = new Label("[Content of reviews here]");
		tabbedPane.addTab("Reviews", null, label, null);
	}
	
	private void prepareFileChooser() {
		tabbedPane.setBorder(UIManager.getBorder("FileChooser.listViewBorder"));
		tabbedPane.setBounds(10, 282, 595, 166);
		frame.getContentPane().add(tabbedPane);
	}
	
	private void prepareBtCancel() {
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnNewButton_1.setBounds(319, 459, 286, 30);
		frame.getContentPane().add(btnNewButton_1);
	}
	
	private void prepareBtBook() {
		JButton btnNewButton = new JButton("Book offer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!list.isSelectionEmpty()) {
					Integer of = (Integer) list.getSelectedValue();
					System.out.println(of);
					//Lamada a la función crear reserva de base de datos.
				}
			}
		});
		btnNewButton.setBounds(10, 459, 286, 30);
		frame.getContentPane().add(btnNewButton);
	}
	
}
