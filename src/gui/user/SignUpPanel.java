package gui.user;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import domain.UserType;
import exceptions.AuthException;
import exceptions.DuplicatedEntityException;
import gui.components.TextPrompt;

public class SignUpPanel extends JPanel {

	private static final long serialVersionUID = -2764750885506622368L;

	/**
	 * 	<pre>
	 *	^                   # start of the line
	 *	[_A-Za-z0-9-\\+]+   # must start with string in the bracket [ ], must contains one or more (+)
	 *	(                   # start of group #1
	 *	\\.[_A-Za-z0-9-]+   # follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 *	)*                  # end of group 1, this group is optional (*)
	 *	<tt>@</tt>                   # must contains a "@" symbol
	 *	[A-Za-z0-9-]+       # follow by string in the bracket [ ], must contains one or more (+)
	 *	(                   # start of group 2 - first level TLD checking
	 *	\\.[A-Za-z0-9]+     # follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 *	)*                  # end of group 2, this group is optional (*)
	 *	(                   # start of group 3 - second level TLD checking
	 *	\\.[A-Za-z]{2,}     # follow by a dot "." and string in the bracket [ ], with minimum length of 2
	 *	)                   # end of group 3
	 *	$                   # end of the line
	 *	</pre>
	 */
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private SharedFrame sharedFrame;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnSignUp;
	private JButton btnCancel;
	private JLabel lblConfirmPassword;
	private JLabel lblSignUp;
	private JPanel accountType;
	private JRadioButton rbClient;
	private JRadioButton rbOwner;
	private JLabel lblAccountType;

	private Image img;

	private UserType userType;
	private JPanel panel;
	private JTextField textFieldEmail;
	//	private JScrollPane scrollPane;

	public SignUpPanel(SharedFrame sharedFrame) {
		this.sharedFrame = sharedFrame;
		img = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/required_field.png"));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		// setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		initialize();

	}

	private void initialize() {
		add(getPanel());
		//		add(getScrollPane());
		add(getBtnSignUp());
		add(getBtnCancel());
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
			userType = UserType.CLIENT;
			rbClient.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						userType = UserType.CLIENT;
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
						userType = UserType.OWNER;
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
			lblSignUp.setBounds(10, 11, 338, 28);
		}
		return lblSignUp;
	}

	private JPasswordField getConfirmPasswordField() {
		if(confirmPasswordField == null) {
			confirmPasswordField = new JPasswordField();
			confirmPasswordField.setBounds(182, 95, 150, 28);
			TextPrompt textPrompt = new TextPrompt(confirmPasswordField);
			textPrompt.setText("Confirm password");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			textPrompt.setIcon(new ImageIcon(img));	
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			confirmPasswordField.setBorder(border);
			//confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(confirmPasswordField.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));
		}
		return confirmPasswordField;
	}

	private JButton getBtnCancel() {
		if(btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(182, 319, 115, 33);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sharedFrame.setSize(sharedFrame.getWidth()-100, sharedFrame.getHeight());
					sharedFrame.showLoginPanel();
				}
			});
		}
		return btnCancel;
	}

	private JButton getBtnSignUp() {
		if(btnSignUp == null) {
			btnSignUp = new JButton("Sign Up");
			btnSignUp.setBounds(55, 319, 115, 33);
			btnSignUp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						if(passwordMatch() && correctEmailFormat()) {
							clearFieldsColors();

							String email = textFieldEmail.getText();
							String username = textFieldUsername.getText();
							String password = String.valueOf(passwordField.getPassword());
							try {
								MainWindow.getBusinessLogic().createUser(email, username, password, userType);
								JFrame jframe = new MainWindow(MainWindow.getBusinessLogic().login(username, password));
								jframe.setVisible(true);
								sharedFrame.dispose();								
								JOptionPane.showMessageDialog(jframe, "Welcome!", "Account created", JOptionPane.INFORMATION_MESSAGE);
							} catch(DuplicatedEntityException ex) {
								clearFieldsColors();
								System.err.println(ex.getMessage());
								System.out.println(ex.getError().toString());

								Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
								Border insideBorder = new EmptyBorder(0, 5, 0, 0);
								CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);

								switch(ex.getError().getCode()) {
								case 1:
									textFieldUsername.setBorder(border);
									break;
								case 2:
									textFieldEmail.setBorder(border);
									break;
								default:
									break;
								}

								JOptionPane.showMessageDialog(sharedFrame,	ex.getError().getDescription() + "\nTry with another one.", "Already in use!", JOptionPane.WARNING_MESSAGE);
							} catch (AuthException | AccountNotFoundException ex) {
								System.err.println(ex.getMessage());
								JOptionPane.showMessageDialog(sharedFrame,	"Wrong username or password.", "Login Failed!", JOptionPane.WARNING_MESSAGE);							
							}
						}
					}
				}
			});
		}
		return btnSignUp;
	}

	private boolean fieldsFilled() {
		
		clearFieldsColors();
		
		Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
		Border insideBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
		
		if(textFieldEmail.getText().equals("")) {
			textFieldEmail.setBorder(border);
			textFieldEmail.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"email\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(textFieldUsername.getText().trim().equals("")){
			textFieldUsername.setBorder(border);
			textFieldUsername.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"username\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(passwordField.getPassword()).equals("")) {
			passwordField.setBorder(border);
			passwordField.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"password\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(String.valueOf(confirmPasswordField.getPassword()).equals("")) {
			confirmPasswordField.setBorder(border);
			confirmPasswordField.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The field \"confirm password\", cannot be empty.", "Empty field", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean passwordMatch() {
		clearFieldsColors();		
		if(!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			confirmPasswordField.setBorder(border);
			confirmPasswordField.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The two passwords does not match.", "Password mismatch", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private boolean correctEmailFormat() {
		clearFieldsColors();
		if(!textFieldEmail.getText().matches(EMAIL_PATTERN)) {
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(255, 51, 51));
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			textFieldEmail.setBorder(border);
			textFieldEmail.requestFocus();
			JOptionPane.showMessageDialog(sharedFrame,	"The email dont have the correct format.", "Invalid email format", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private JPasswordField getPasswordField() {
		if(passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setBounds(20, 95, 150, 28);		
			TextPrompt textPrompt = new TextPrompt(passwordField);
			textPrompt.setText("Password");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			textPrompt.setIcon(new ImageIcon(img));
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			passwordField.setBorder(border);
			//passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));			
		}
		return passwordField;
	}

	private JTextField getTextFieldUsername() {
		if(textFieldUsername == null) {
			textFieldUsername = new JTextField();
			textFieldUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
			textFieldUsername.setBounds(20, 54, 312, 28);
			textFieldUsername.setColumns(10);
			TextPrompt textPrompt = new TextPrompt(textFieldUsername);
			textPrompt.setText("Username");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			textPrompt.setIcon(new ImageIcon(img));	
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			textFieldUsername.setBorder(border);
			//textFieldUsername.setBorder(BorderFactory.createCompoundBorder(textFieldUsername.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));
		}
		return textFieldUsername;
	}

	@SuppressWarnings("unused")
	private JLabel getLblUsername() {
		if(lblUsername == null) {
			lblUsername = new JLabel("Username:");
			lblUsername.setBounds(20, 11, 204, 14);
		}
		return lblUsername;
	}

	@SuppressWarnings("unused")
	private JLabel getLblPassword() {
		if(lblPassword == null) {
			lblPassword = new JLabel("Password:");
			lblPassword.setBounds(20, 75, 204, 14);
		}
		return lblPassword;
	}

	@SuppressWarnings("unused")
	private JLabel getLblConfirmPassword() {
		if(lblConfirmPassword == null) {
			lblConfirmPassword = new JLabel("Confirm Password:");
			lblConfirmPassword.setBounds(20, 139, 204, 14);
		}
		return lblConfirmPassword;
	}

	private JLabel getLblAccountType() {
		if(lblAccountType == null) {
			lblAccountType = new JLabel("Select account type:");
			lblAccountType.setBounds(20, 50, 204, 14);
		}
		return lblAccountType;
	}

	private JTextField getTextFieldEmail() {
		if(textFieldEmail == null) {
			textFieldEmail = new JTextField();
			textFieldEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
			textFieldEmail.setColumns(10);
			textFieldEmail.setBounds(20, 13, 312, 28);
			TextPrompt textPrompt = new TextPrompt(textFieldEmail);
			textPrompt.setText("email");
			textPrompt.setStyle(Font.BOLD);
			textPrompt.setAlpha(128);
			textPrompt.setIcon(new ImageIcon(img));	
			Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border insideBorder = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
			textFieldEmail.setBorder(border);
			//textFieldEmail.setBorder(BorderFactory.createCompoundBorder(textFieldEmail.getBorder(), BorderFactory.createEmptyBorder(0, 1, 0, 0)));
		}
		return textFieldEmail;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(255, 51, 51), 0, true), "* Required information  ", TitledBorder.RIGHT, TitledBorder.BOTTOM, null, Color.RED));
			panel.setBounds(0, 118, 360, 157);
			panel.setLayout(null);
			//panel.add(getLblUsername());
			panel.add(getTextFieldUsername());
			//panel.add(getLblPassword());
			panel.add(getPasswordField());
			panel.add(getConfirmPasswordField());
			panel.add(getTextFieldEmail());
			//panel.add(getLblConfirmPassword());
		}
		return panel;
	}

	public void clearFieldsColors() {
		Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
		Border insideBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
		textFieldEmail.setBorder(border);
		textFieldUsername.setBorder(border);
		passwordField.setBorder(border);
		confirmPasswordField.setBorder(border);
	}
}
