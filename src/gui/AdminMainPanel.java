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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
		setLayout(null);

		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 39, 276, 36);
		initializeComboBox();
		comboBox.setModel(ruralHouses);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rh = (RuralHouse) comboBox.getSelectedItem();
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
		rdbtnNewRadioButton.setBounds(10, 135, 107, 38);
		add(rdbtnNewRadioButton);



		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Rejected");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rS = ReviewState.REJECTED;
			}
		});
		buttonGroup.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setBounds(119, 135, 105, 38);
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
		description = editorPane.getText();

		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(rS != null && rh != null) {
					System.out.println(description);
					editorPane.setText("");
					Review r = rh.getReview();
					r.setDescription(description);
					//r.setState(, rS); <--- Help here pls.
					//Send the review
					//MainWindow.getBusinessLogic().updateReview(rh, r);
					rS = null;
					buttonGroup.clearSelection();
					JOptionPane.showMessageDialog(null,	"Se ha enviado la revisión correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);					
				} else {	
					JOptionPane.showMessageDialog(null, "No se puede enviar una revisión sin su estado o su casa rural", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(273, 342, 155, 23);
		add(btnNewButton);

		buttonGroup.add(rdbtnNewRadioButton);
		buttonGroup.add(rdbtnNewRadioButton_1);

	}

	public void initializeComboBox() {
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHouses();

		ruralHouses.addElement(null); //For that the user selected a RuralHouse

		for(RuralHouse rh : rhs) {
			ruralHouses.addElement(rh);
		}

	}
}
