package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dataAccess.DataAccess;
import domain.User.Role;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;

import exceptions.AuthException;

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import businessLogic.ApplicationFacadeInterfaceWS;

import javax.swing.UIManager;

import java.awt.Color;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {

	private SharedFrame sharedFrame;
	private JTextField textFieldUsername;
	private JLabel lblUsername;
	private JPasswordField passwordField;
	private JLabel lblPassword;
	private JSeparator separator;
	private JButton btnLogin;
	private JButton btnLoginGuest;
	private JButton btnSignUp;
	private JLabel lblInfo;
	private JLabel lblLogin;

	public LoginPanel(SharedFrame sharedFrame) {
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
		add(getSeparator());
		add(getBtnLogin());	
		add(getLblInfo());
		add(getBtnSignUp());
		add(getBtnLoginGuest());
		add(getLblLogin());
	}

	public JButton getDefaultButton() {
		return getBtnLogin();
	}	

	private JButton getBtnLoginGuest() {
		if(btnLoginGuest == null) {
			btnLoginGuest = new JButton("Login As Guest");
			btnLoginGuest.setBounds(20, 232, 204, 28);
			btnLoginGuest.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(sharedFrame,	"This feature currently is being developed or is planned to do so.", "WIP", JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return btnLoginGuest;
	}

	private JButton getBtnSignUp() {
		if(btnSignUp == null) {
			btnSignUp = new JButton("Sign Up");
			btnSignUp.setBounds(20, 325, 204, 28);
			btnSignUp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sharedFrame.showSignUpPanel();
				}
			});
		}
		return btnSignUp;
	}

	private JLabel getLblInfo() {
		if(lblInfo == null) {
			lblInfo = new JLabel("Dont have an account?");
			lblInfo.setBounds(20, 300, 204, 14);
		}
		return lblInfo;
	}

	private JButton getBtnLogin() {
		if(btnLogin == null) {
			btnLogin = new JButton("Login");
			btnLogin.setBounds(20, 193, 204, 28);
			btnLogin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {

						DataAccess dbManager = new DataAccess();
						String username = textFieldUsername.getText();
						String password = String.valueOf(passwordField.getPassword());
						try {
							dbManager.login(username, password);
							System.out.println(dbManager.getRole(username));
							if(dbManager.getRole(username) != Role.OWNER) {//FIXME: TEMPORAL SOLUTION
								JOptionPane.showMessageDialog(sharedFrame,	"The " + dbManager.getRole(username) +" view is not implemented jet.", "WIP", JOptionPane.INFORMATION_MESSAGE);
							} else {
								JFrame jframe = new MainGUI(dbManager.getRole(username));						
								jframe.setVisible(true);
								sharedFrame.dispose();
							}

						} catch (AuthException | AccountNotFoundException ex) {
							System.err.println(ex.getMessage());
							JOptionPane.showMessageDialog(sharedFrame,	"Wrong username or password.", "Login Failed!", JOptionPane.WARNING_MESSAGE);							
						}
					}
				}
			});
		}
		return btnLogin;
	}
	
	private boolean fieldsFilled() {
		if(textFieldUsername.getText().trim().equals("")){
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"username\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(passwordField.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"password\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private JSeparator getSeparator() {
		if(separator == null) {
			separator = new JSeparator();
			separator.setBounds(20, 281, 204, 2);
		}
		return separator;
	}

	private JPasswordField getPasswordField() {
		if(passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setBounds(20, 139, 204, 28);
		}
		return passwordField;
	}

	private JLabel getLblUsername() {
		if(lblUsername == null) {
			lblUsername = new JLabel("Username:");
			lblUsername.setBounds(20, 50, 204, 14);
		}
		return lblUsername;
	}

	private JTextField getTextFieldUsername() {
		if(textFieldUsername == null) {
			textFieldUsername = new JTextField();
			textFieldUsername.setToolTipText("");
			textFieldUsername.setBounds(20, 75, 204, 28);
			textFieldUsername.setColumns(10);
		}
		return textFieldUsername;
	}

	private JLabel getLblPassword() {
		if(lblPassword == null) {
			lblPassword = new JLabel("Password:");
			lblPassword.setBounds(20, 114, 204, 14);
		}
		return lblPassword;
	}

	//	private int exitQuestion() {
	//		int reply = JOptionPane.showConfirmDialog(null, "Seguro que desea salir?", null, JOptionPane.YES_NO_OPTION);
	//		if (reply == JOptionPane.YES_OPTION) {
	//			//[TODO]: Close here db conection
	//			System.exit(0);
	//		}
	//		return reply;
	//	}

	private JLabel getLblLogin() {
		if (lblLogin == null) {
			lblLogin = new JLabel("Log In");
			lblLogin.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
			lblLogin.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblLogin.setBounds(10, 11, 234, 28);
		}
		return lblLogin;
	}
}
