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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import businessLogic.util.TextPrompt;

import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;

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
		//		add(getLblUsername());
		add(getTextFieldUsername());
		//		add(getLblPassword());
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
			btnLoginGuest.setBounds(20, 222, 204, 28);
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
			btnSignUp.setBounds(20, 319, 204, 28);
			btnSignUp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sharedFrame.setSize(sharedFrame.getWidth()+100, sharedFrame.getHeight());
					sharedFrame.showSignUpPanel();
				}
			});
		}
		return btnSignUp;
	}

	private JLabel getLblInfo() {
		if(lblInfo == null) {
			lblInfo = new JLabel("Dont have an account?");
			lblInfo.setBounds(20, 294, 204, 14);
		}
		return lblInfo;
	}

	private JButton getBtnLogin() {
		if(btnLogin == null) {
			btnLogin = new JButton("Login");
			btnLogin.setBounds(20, 171, 204, 28);
			btnLogin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {

						DataAccess dbManager = new DataAccess();
						String username = textFieldUsername.getText();
						String password = String.valueOf(passwordField.getPassword());
						try {
							dbManager.login(username, password); //[TODO]: Login con correo electronico
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
			passwordField.setBounds(20, 115, 204, 30);
			passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));
			TextPrompt tp = new TextPrompt(passwordField);
			tp.setText("Password");
			tp.setStyle(Font.BOLD);
			tp.setAlpha(128);	
			//ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/password.png")));
			//tp.setIcon(imageIcon);
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
			textFieldUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
			textFieldUsername.setBounds(20, 62, 204, 30);
			textFieldUsername.setColumns(10);
			textFieldUsername.setBorder(BorderFactory.createCompoundBorder(textFieldUsername.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));
			TextPrompt tp = new TextPrompt(textFieldUsername);
			tp.setText("Username");
			tp.setStyle(Font.BOLD);
			tp.setAlpha(128);
			//ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/username.png")));
			//tp.setIcon(imageIcon);
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
