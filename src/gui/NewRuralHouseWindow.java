package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.ApplicationFacadeInterfaceWS;
import exceptions.DuplicatedEntityException;

@SuppressWarnings("serial")
public class NewRuralHouseWindow extends JDialog {

	private JPanel contentPane;
	private JTextField textFieldDescription;
	private JTextField textFieldCity;
	private JLabel lblNewRuralHouse;
	private JLabel lblDescription;
	private JLabel lblCity;
	private JButton btnAdd;
	private JButton btnCancel;

	/**
	 * Create the frame.
	 */
	public NewRuralHouseWindow() {
		setModalityType(ModalityType.TOOLKIT_MODAL);
		setResizable(false);
		setTitle("New Rural House");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 242, 240);
		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/rural_house.png")));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Get screen dimension
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //Set the screen location to the center of the screen

		initialize();

		getRootPane().setDefaultButton(btnAdd);

	}

	private void initialize() {

		setContentPane(getJContentPane());

		contentPane.add(getLblNewRuralHouse());
		contentPane.add(getLblDescription());		
		contentPane.add(getTextFieldDescription());
		contentPane.add(getTextFieldCity());
		contentPane.add(getLblCity());
		contentPane.add(getBtnCreate());
		contentPane.add(getBtnCancel());

	}

	private JPanel getJContentPane() {
		if(contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);
		}
		return contentPane;
	}

	private JButton getBtnCancel() {		
		if(btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(130, 170, 96, 28);
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancel;
	}

	private JButton getBtnCreate() {
		if(btnAdd == null) {
			btnAdd = new JButton("Add");
			btnAdd.setBounds(10, 170, 96, 28);
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						ApplicationFacadeInterfaceWS facade = MainGUI.getBusinessLogic();
						String description = textFieldDescription.getText();
						String city = textFieldCity.getText();
						try {
							facade.createRuralHouse(description, city);
							dispose();
							JOptionPane.showMessageDialog(null,	"Rural house added successfuly", "Info", JOptionPane.INFORMATION_MESSAGE);
						} catch (DuplicatedEntityException e1) {
							JOptionPane.showMessageDialog(null,	"There is already a rural house with the same name and city.", "Duplicated rural house", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			});
		}
		return btnAdd;
	}

	private boolean fieldsFilled() {
		if(textFieldDescription.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,	"The field \"description\", cannot be empty.", "Empty value", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(textFieldCity.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this,	"The field \"city\", cannot be empty.", "Empty value", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private JLabel getLblCity() {
		if(lblCity == null) {
			lblCity = new JLabel("City: ");
			lblCity.setBounds(20, 104, 117, 14);
		}
		return lblCity;
	}

	private JTextField getTextFieldCity() {
		if(textFieldCity == null) {	
			textFieldCity = new JTextField();
			textFieldCity.setColumns(10);
			textFieldCity.setBounds(20, 127, 191, 20);
		}
		return textFieldCity;
	}

	private JTextField getTextFieldDescription() {
		if(textFieldDescription == null) {	
			textFieldDescription = new JTextField();
			textFieldDescription.setBounds(20, 73, 191, 20);
			textFieldDescription.setColumns(10);
		}
		return textFieldDescription;
	}

	private JLabel getLblDescription() {
		if(lblDescription == null) {	
			lblDescription = new JLabel("Description: ");
			lblDescription.setBounds(20, 50, 117, 14);
		}
		return lblDescription;
	}

	private JLabel getLblNewRuralHouse() {
		if(lblNewRuralHouse == null) {	
			lblNewRuralHouse = new JLabel("Rural House");
			lblNewRuralHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewRuralHouse.setBounds(10, 11, 137, 28);
		}
		return lblNewRuralHouse;
	}

}
