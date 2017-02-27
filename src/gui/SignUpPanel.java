package gui;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import dataAccess.DataAccess;
import domain.User.Role;
import exceptions.DuplicatedEntityException;

@SuppressWarnings("serial")
public class SignUpPanel extends JPanel {

	private SharedFrame sharedFrame;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField repeatPasswordField;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnSignUp;
	private JButton btnCancel;
	private JLabel lblRepeatPassword;
	private JLabel lblSignUp;
	private JPanel accountType;
	private JRadioButton rbClient;
	private JRadioButton rbOwner;
	private JLabel lblAccountType;

	private Role role;

	public SignUpPanel(SharedFrame sharedFrame) {		
		this.sharedFrame = sharedFrame;

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		initialize();

	}

	private void initialize() {
		add(getLblUsername());
		add(getTextFieldUsername());		
		add(getLblPassword());
		add(getPasswordField());		
		add(getBtnSignUp());
		add(getBtnCancel());
		add(getRepeatPasswordField());
		add(getLblRepeatPassword());
		add(getLblSignUp());
		add(getAccountType());
		add(getLblAccountType());

	}

	public JButton getDefaultButton() {
		return getBtnSignUp();
	}

	private JPanel getAccountType() {
		if(accountType == null) {
			accountType = new JPanel();
			accountType.setBounds(20, 74, 144, 33);

			FlowLayout fl_accountType = (FlowLayout) accountType.getLayout();
			fl_accountType.setAlignment(FlowLayout.LEFT);

			ButtonGroup group = new ButtonGroup();
			group.add(getRbClient());
			group.add(getRbOwner());

			accountType.add(getRbClient());
			accountType.add(getRbOwner());
		}
		return accountType;
	}

	private JRadioButton getRbClient() {
		if(rbClient == null) {
			rbClient = new JRadioButton("Client");
			rbClient.setSelected(true);
			role = Role.CLIENT;
			rbClient.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						role = Role.CLIENT;
					}
				}
			});
		}
		return rbClient;
	}

	private JRadioButton getRbOwner() {
		if(rbOwner == null) {
			rbOwner = new JRadioButton("Owner");
			rbOwner.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						role = Role.OWNER;
					}
				}
			});
		}
		return rbOwner;
	}

	private Component getLblSignUp() {
		if(lblSignUp == null) {
			lblSignUp = new JLabel("Sign Up");
			lblSignUp.setBorder(new TitledBorder(null, "", TitledBorder.CENTER, TitledBorder.TOP, null, null));
			lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
			lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblSignUp.setBounds(10, 11, 234, 28);
		}
		return lblSignUp;
	}

	private JLabel getLblRepeatPassword() {
		if(lblRepeatPassword == null) {
			lblRepeatPassword = new JLabel("Repeat Password:");
			lblRepeatPassword.setBounds(20, 246, 204, 14);
		}
		return lblRepeatPassword;
	}

	private JPasswordField getRepeatPasswordField() {
		if(repeatPasswordField == null) {
			repeatPasswordField = new JPasswordField();
			repeatPasswordField.setBounds(20, 271, 204, 28);
		}
		return repeatPasswordField;
	}

	private JButton getBtnCancel() {
		if(btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(135, 327, 115, 33);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sharedFrame.showLoginPanel();					
				}
			});
		}
		return btnCancel;
	}

	private JButton getBtnSignUp() {
		if(btnSignUp == null) {
			btnSignUp = new JButton("Sign Up");
			btnSignUp.setBounds(10, 327, 115, 33);
			btnSignUp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						if(passwordMatch()) {
							DataAccess dbManager = new DataAccess();
							String username = textFieldUsername.getText();
							String password = String.valueOf(passwordField.getPassword());
							try {
								dbManager.createUser(username, password, role);
								sharedFrame.showLoginPanel();
								JOptionPane.showMessageDialog(sharedFrame,	"Account successfuly created", "Account created", JOptionPane.INFORMATION_MESSAGE);
							} catch(DuplicatedEntityException ex) {
								System.err.println(ex.getMessage());
								JOptionPane.showMessageDialog(sharedFrame,	"The username " + username + " is already in use.\nTry with another one.", "Username in use", JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			});
		}
		return btnSignUp;
	}

	private boolean fieldsFilled() {
		if(textFieldUsername.getText().trim().equals("")){
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"username\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(passwordField.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"password\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(repeatPasswordField.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"repeat password\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean passwordMatch() {
		if(!Arrays.equals(passwordField.getPassword(), repeatPasswordField.getPassword())) {
			JOptionPane.showMessageDialog(sharedFrame,	"The two passwords does not match.", "Password mismatch", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;

	}

	private JPasswordField getPasswordField() {
		if(passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setBounds(20, 207, 204, 28);
		}
		return passwordField;
	}

	private JTextField getTextFieldUsername() {
		if(textFieldUsername == null) {
			textFieldUsername = new JTextField();
			textFieldUsername.setBounds(20, 143, 204, 28);
			textFieldUsername.setColumns(10);
		}
		return textFieldUsername;
	}

	private JLabel getLblPassword() {
		if(lblPassword == null) {
			lblPassword = new JLabel("Password:");
			lblPassword.setBounds(20, 182, 204, 14);
		}
		return lblPassword;
	}

	private JLabel getLblUsername() {
		if(lblUsername == null) {
			lblUsername = new JLabel("Username:");
			lblUsername.setBounds(20, 118, 204, 14);
		}
		return lblUsername;
	}

	private JLabel getLblAccountType() {
		if(lblAccountType == null) {
			lblAccountType = new JLabel("Select account type:");
			lblAccountType.setBounds(20, 50, 204, 14);
		}
		return lblAccountType;
	}

}
