package gui;

import domain.City;
import exceptions.DuplicatedEntityException;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import businessLogic.ApplicationFacadeInterface;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import exceptions.DuplicatedEntityException;

public class NewCity extends JFrame {

	private JFrame frame;
	private JTextField textFieldCity;

	
	public NewCity() {
		initialize();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 283, 155);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		setTitle("New city");
		getlblCity();
		getBtnCreate();
		getCancelbttn();
	}
	
	private void getlblCity() {
		
		textFieldCity = new JTextField();
		textFieldCity.setBounds(10, 50, 247, 20);
		frame.getContentPane().add(textFieldCity);
		textFieldCity.setColumns(10);
	}
	
	private JButton getBtnCreate() {
		JLabel lblCity = new JLabel("City:");
		lblCity.setBounds(10, 25, 46, 14);
		frame.getContentPane().add(lblCity);
		JButton btnAccept = new JButton("Accept");
		btnAccept.setBounds(10, 81, 103, 22);
		frame.getContentPane().add(btnAccept);
		btnAccept.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						ApplicationFacadeInterface facade = MainGUI.getBusinessLogic();
						String city = textFieldCity.getText();
						try {
							facade.createCity(city);
							dispose();
							JOptionPane.showMessageDialog(null,	"City added successfuly", "Info", JOptionPane.INFORMATION_MESSAGE);
						} catch (DuplicatedEntityException e1) {
							JOptionPane.showMessageDialog(null,	"There is already a city with the same name.", "Duplicated city", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			});
		return btnAccept;
	}
	

	
	private void getCancelbttn() {
		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.setBounds(147, 81, 110, 22);
		frame.getContentPane().add(btnNewButton);
		
		
	}
	
	private boolean fieldsFilled() {
		if(textFieldCity.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,	"The field \"description\", cannot be empty.", "Empty value", JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			return true;
		}
	}
	
}
