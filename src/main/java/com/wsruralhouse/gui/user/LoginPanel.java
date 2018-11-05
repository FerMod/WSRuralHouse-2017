package com.wsruralhouse.gui.user;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.wsruralhouse.domain.AbstractUser;
import com.wsruralhouse.exceptions.AuthException;
import com.wsruralhouse.gui.components.TextPrompt;

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
					JFrame frame = new MainWindow(null);
					frame.setVisible(true);
					sharedFrame.dispose();	
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

						String username = textFieldUsername.getText();
						String password = String.valueOf(passwordField.getPassword());
						try {
							AbstractUser user = MainWindow.getBusinessLogic().login(username, password); //[TODO]: Login con correo electronico
							JFrame frame = new MainWindow(user);
							frame.setVisible(true);
							sharedFrame.dispose();					
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
		clearFieldsColors();
		if(textFieldUsername.getText().trim().equals("")){
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			textFieldUsername.setBorder(border);
			textFieldUsername.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"username\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(passwordField.getPassword()).equals("")) {
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			passwordField.setBorder(border);
			passwordField.requestFocus();
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
			TextPrompt textPrompt = new TextPrompt(passwordField);
			textPrompt.setText("Password");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			passwordField.setBorder(border);
			//passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));	
			//ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/main/resources/img/password.png")));
			//tp.setIcon(imageIcon);
		}
		return passwordField;
	}

	@SuppressWarnings("unused")
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
			TextPrompt textPrompt = new TextPrompt(textFieldUsername);
			textPrompt.setText("Username");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			textFieldUsername.setBorder(border);
			//ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/main/resources/img/username.png")));
			//tp.setIcon(imageIcon);
			textFieldUsername.requestFocus();
		}
		return textFieldUsername;
	}

	@SuppressWarnings("unused")
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

	public void clearFieldsColors() {
		Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
		Border insideBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
		textFieldUsername.setBorder(border);
		passwordField.setBorder(border);
	}
}
