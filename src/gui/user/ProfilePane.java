package gui.user;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import domain.AbstractUser;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class ProfilePane extends JPanel {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 6097793697042734433L;

	private JTextField lblUsername, usernameField, lblPassword, lblEmail, emailField, lblRole, roleField;
	private JPasswordField passwordField;

	private AbstractUser user;

	public ProfilePane(AbstractUser user) {
		setOpaque(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setFocusable(false);

		this.user = user;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{90, 222, 0, 0};
		gridBagLayout.rowHeights = new int[]{40, 35, 35, 35, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		GridBagConstraints gbcLblUsername = new GridBagConstraints();
		gbcLblUsername.fill = GridBagConstraints.BOTH;
		gbcLblUsername.insets = new Insets(10, 10, 5, 0);
		gbcLblUsername.gridx = 0;
		gbcLblUsername.gridy = 0;
		add(getLblUsername(), gbcLblUsername);

		GridBagConstraints gbcUsernameField = new GridBagConstraints();
		gbcUsernameField.fill = GridBagConstraints.BOTH;
		gbcUsernameField.insets = new Insets(10, 0, 5, 5);
		gbcUsernameField.gridx = 1;
		gbcUsernameField.gridy = 0;
		add(getUsernameField(), gbcUsernameField);

		GridBagConstraints gbcLblPassword = new GridBagConstraints();
		gbcLblPassword.fill = GridBagConstraints.BOTH;
		gbcLblPassword.insets = new Insets(5, 10, 5, 0);
		gbcLblPassword.gridx = 0;
		gbcLblPassword.gridy = 1;
		add(getLblPassword(), gbcLblPassword);

		GridBagConstraints gbcPasswordField = new GridBagConstraints();
		gbcPasswordField.fill = GridBagConstraints.BOTH;
		gbcPasswordField.insets = new Insets(5, 0, 5, 5);
		gbcPasswordField.gridx = 1;
		gbcPasswordField.gridy = 1;
		add(getPasswordField(), gbcPasswordField);

		GridBagConstraints gbcLblEmail = new GridBagConstraints();
		gbcLblEmail.fill = GridBagConstraints.BOTH;
		gbcLblEmail.insets = new Insets(5, 10, 5, 0);
		gbcLblEmail.gridx = 0;
		gbcLblEmail.gridy = 2;
		add(getLblEmail(), gbcLblEmail);

		GridBagConstraints gbcEnailField = new GridBagConstraints();
		gbcEnailField.fill = GridBagConstraints.BOTH;
		gbcEnailField.insets = new Insets(5, 0, 5, 5);
		gbcEnailField.gridx = 1;
		gbcEnailField.gridy = 2;
		add(getEmailField(), gbcEnailField);

		GridBagConstraints gbcLblRole = new GridBagConstraints();
		gbcLblRole.fill = GridBagConstraints.BOTH;
		gbcLblRole.insets = new Insets(5, 10, 5, 0);
		gbcLblRole.gridx = 0;
		gbcLblRole.gridy = 3;
		add(getLblRole(), gbcLblRole);

		GridBagConstraints gbcRoleField = new GridBagConstraints();
		gbcRoleField.fill = GridBagConstraints.BOTH;
		gbcRoleField.insets = new Insets(5, 0, 5, 5);
		gbcRoleField.gridx = 1;
		gbcRoleField.gridy = 3;
		add(getRoleField(), gbcRoleField);	

	}

	private JTextField getLblUsername() {
		if(lblUsername == null) {
			lblUsername = new JTextField("User");
			lblUsername.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblUsername.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			lblUsername.setOpaque(false);
			lblUsername.setFocusable(false);
			lblUsername.setEditable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			lblUsername.setBorder(border);
		}
		return lblUsername;
	}

	private JTextField getUsernameField() {
		if(usernameField == null) {
			usernameField = new JTextField(user.getUsername());
			usernameField.setFocusTraversalKeysEnabled(false);
			usernameField.setOpaque(false);
			usernameField.setEditable(false);
			usernameField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			usernameField.setFocusable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			usernameField.setBorder(border);
		}
		return usernameField;
	}

	private JTextField getLblPassword() {
		if(lblPassword == null) {
			lblPassword = new JTextField("Password");
			lblPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblPassword.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			lblPassword.setOpaque(false);
			lblPassword.setFocusable(false);
			lblPassword.setEditable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			lblPassword.setBorder(border);
		}
		return lblPassword;
	}

	private JPasswordField getPasswordField() {
		if(passwordField == null) {
			passwordField = new JPasswordField(user.getPassword().replaceAll(".", "*"));
			passwordField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			passwordField.setFocusTraversalKeysEnabled(false);
			passwordField.setOpaque(false);
			passwordField.setEditable(false);
			passwordField.setEchoChar('☺');
			passwordField.setFocusable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			passwordField.setBorder(border);
		}
		return passwordField;
	}

	private JTextField getLblEmail() {
		if(lblEmail == null) {
			lblEmail = new JTextField("e-mail");
			lblEmail.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblEmail.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			lblEmail.setOpaque(false);
			lblEmail.setFocusable(false);
			lblEmail.setEditable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			lblEmail.setBorder(border);
		}
		return lblEmail;
	}

	private JTextField getEmailField() {
		if(emailField == null) {
			emailField = new JTextField(user.getEmail());
			emailField.setFocusTraversalKeysEnabled(false);
			emailField.setOpaque(false);
			emailField.setEditable(false);
			emailField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			emailField.setFocusable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			emailField.setBorder(border);
		}
		return emailField;
	}

	private JTextField getLblRole() {
		if(lblRole == null) {
			lblRole = new JTextField("UserType");
			lblRole.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblRole.setOpaque(false);
			lblRole.setFocusable(false);
			lblRole.setEditable(false);
			lblRole.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			Border matterBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			lblRole.setBorder(border);
		}
		return lblRole;
	}

	private JTextField getRoleField() {
		if(roleField == null) {
			roleField = new JTextField(user.getRole().toString());
			roleField.setFocusTraversalKeysEnabled(false);
			roleField.setOpaque(false);
			roleField.setEditable(false);
			roleField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			roleField.setFocusable(false);

			Border matterBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
			Border empty = new EmptyBorder(0, 5, 0, 0);
			CompoundBorder border = new CompoundBorder(matterBorder, empty);
			roleField.setBorder(border);
		}
		return roleField;
	}

}
