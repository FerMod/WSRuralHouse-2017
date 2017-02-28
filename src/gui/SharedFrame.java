package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SharedFrame extends JFrame {

	private LoginPanel loginPanel;
	private SignUpPanel signUpPanel;

	/**
	 * Create the frame.
	 */
	public SharedFrame() {

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 260, 398);
		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/login.png"));
		setIconImage(icon);
		
		loginPanel = new LoginPanel(this);
		signUpPanel = new SignUpPanel(this);

		showLoginPanel();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Get screen dimension
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //Set the screen location to the center of the screen			

	}

	public void showLoginPanel() {
		setTitle("Log In");
		setContentPane(loginPanel);
		getRootPane().setDefaultButton(loginPanel.getDefaultButton());
		loginPanel.setVisible(true);
		signUpPanel.setVisible(false);
	}

	public void showSignUpPanel() {
		setTitle("Sign Up");
		setContentPane(signUpPanel);
		getRootPane().setDefaultButton(signUpPanel.getDefaultButton());
		loginPanel.setVisible(false);
		signUpPanel.setVisible(true);
	}

}
