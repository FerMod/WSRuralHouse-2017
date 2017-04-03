package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import businessLogic.ApplicationFacadeInterface;
import domain.City;
import exceptions.DuplicatedEntityException;

public class NewRuralHouseWindow extends JDialog {

	private static final long serialVersionUID = 6770177109582209536L;
	
	private JPanel contentPane;
	private JTextField textFieldDescription;
	private JComboBox<City> comboBoxCity;
	private JLabel lblNewRuralHouse;
	private JLabel lblDescription;
	private JLabel lblCity;
	private JButton btnAdd;
	private JButton btnCancel;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the frame.
	 */
	public NewRuralHouseWindow() {
		setModalityType(ModalityType.TOOLKIT_MODAL);
		setResizable(false);
		setTitle("New Rural House");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 529, 454);
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
		contentPane.add(getComboBoxCity());
		contentPane.add(getLblCity());
		contentPane.add(getBtnCreate());
		contentPane.add(getBtnCancel());

	}

	private JPanel getJContentPane() {
		if(contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);
			
			JLabel lblPrice = new JLabel("Price:");
			lblPrice.setBounds(10, 328, 46, 14);
			contentPane.add(lblPrice);
			
			textField = new JTextField();
			textField.setBounds(10, 353, 46, 20);
			contentPane.add(textField);
			textField.setColumns(10);
			
			JLabel lblImages = new JLabel("Images:");
			lblImages.setBounds(260, 198, 46, 14);
			contentPane.add(lblImages);
			
			JButton btnNewButton = new JButton("Open directory");
			btnNewButton.setBounds(260, 222, 128, 23);
			contentPane.add(btnNewButton);
			
			JLabel lblTags = new JLabel("Tags:");
			lblTags.setBounds(260, 254, 46, 14);
			contentPane.add(lblTags);
			
			textField_1 = new JTextField();
			textField_1.setBounds(260, 274, 251, 73);
			contentPane.add(textField_1);
			textField_1.setColumns(10);
			
			JButton btnAddNewCity = new JButton("Add new city");
			btnAddNewCity.setBounds(10, 250, 191, 23);
			contentPane.add(btnAddNewCity);
		}
		return contentPane;
	}

	private JButton getBtnCancel() {		
		if(btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(260, 384, 251, 28);
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
			btnAdd.setBounds(10, 384, 240, 28);
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						ApplicationFacadeInterface facade = MainGUI.getBusinessLogic();
						String description = textFieldDescription.getText();
						City city = (City) comboBoxCity.getSelectedItem();
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
		} else if(comboBoxCity.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this,	"The field \"city\", cannot be empty.", "Empty value", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private JLabel getLblCity() {
		if(lblCity == null) {
			lblCity = new JLabel("City: ");
			lblCity.setBounds(10, 198, 117, 14);
		}
		return lblCity;
	}

	private JComboBox<City> getComboBoxCity() {
		if(comboBoxCity == null) {	
			ApplicationFacadeInterface facade = MainGUI.getBusinessLogic();
			comboBoxCity = new JComboBox<City>(new DefaultComboBoxModel<City>(facade.getCities()));
			comboBoxCity.setBounds(10, 223, 191, 20);
		}
		return comboBoxCity;
	}

	private JTextField getTextFieldDescription() {
		if(textFieldDescription == null) {	
			textFieldDescription = new JTextField();
			textFieldDescription.setBounds(10, 75, 501, 101);
			textFieldDescription.setColumns(10);
		}
		return textFieldDescription;
	}

	private JLabel getLblDescription() {
		if(lblDescription == null) {	
			lblDescription = new JLabel("Description: ");
			lblDescription.setBounds(10, 50, 117, 14);
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
