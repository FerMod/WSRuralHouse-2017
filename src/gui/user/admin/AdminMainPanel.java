package gui.user.admin;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import domain.Admin;
import domain.Review;
import domain.Review.ReviewState;
import domain.util.ExtendedIterator;
import gui.components.RightClickMenu;
import gui.components.TextPrompt;
import gui.user.MainWindow;
import domain.RuralHouse;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class AdminMainPanel extends JPanel {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = -1893946454333342942L;

	private final ButtonGroup buttonGroup = new ButtonGroup();
	private DefaultComboBoxModel<RuralHouse> ruralHouses = new DefaultComboBoxModel<RuralHouse>();
	private Review.ReviewState reviewState;
	private RuralHouse ruralHouse;
	private String description;
	private JButton btnSend;
	private JLabel lblReviewState, lblDescription;
	private JRadioButton rdbtnApproved, rdbtnRejected;
	private JTextPane textPane;
	private boolean enableButtonGroup = false;
	private TextPrompt textPanePrompt;
	private JLabel lblNombre;
	private JLabel lblCiudad;
	private JLabel lblDireccin;
	private JLabel lblNombreAtrib;
	private JLabel lblCity;
	private JLabel lblAdress;
	private JLabel lblDescripcin;
	private JTextPane textPane_1;

	/**
	 * Create the panel.
	 * @param frame the parent frame
	 */
	public AdminMainPanel(JFrame frame) {
		initComponents();
	}

	private void initComponents() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		initializeComboBox();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{107, 105, 151, 0};
		gridBagLayout.rowHeights = new int[]{24, 36, 24, 21, 0, 14, 131, 0, 24, 0, 207, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JComboBox<RuralHouse> comboBox = new JComboBox<RuralHouse>();
		comboBox.setModel(ruralHouses);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean enable;

				if (comboBox.getSelectedIndex() != -1) {
					ruralHouse = (RuralHouse)comboBox.getSelectedItem();
					lblNombreAtrib.setText(ruralHouse.getName());
					lblCity.setText(ruralHouse.getCity().getName());
					lblAdress.setText(ruralHouse.getAddress());
					System.out.println(ruralHouse.getImage(0).getDescription());
					textPane_1.setEnabled(true);
					textPane_1.setText(ruralHouse.getDescription());
					textPane_1.setEnabled(false);
					enable = true;
				} else {
					enable = false;
				}

				if(enable != enableButtonGroup) {

					enableButtonGroup = enable;

					Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
					while (enumeration.hasMoreElements()) {
						AbstractButton abstractButton = enumeration.nextElement();
						abstractButton.setEnabled(enableButtonGroup);
					}

					lblReviewState.setEnabled(enableButtonGroup);
					lblDescription.setEnabled(enableButtonGroup);
					textPane.setEnabled(rdbtnRejected.isSelected());

					if(enableButtonGroup && rdbtnRejected.isSelected()) {
						textPanePrompt.setAlpha(128);
					} else {
						textPanePrompt.setAlpha(64);
					}

					if(rdbtnApproved.isSelected() || rdbtnRejected.isSelected()) {
						btnSend.setEnabled(enableButtonGroup);
					}

				}

			}

		});

		JLabel lblCasaRural = new JLabel("Casa rural a revisar");
		lblCasaRural.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GridBagConstraints gbc_lblCasaRuralA = new GridBagConstraints();
		gbc_lblCasaRuralA.fill = GridBagConstraints.BOTH;
		gbc_lblCasaRuralA.insets = new Insets(0, 0, 5, 0);
		gbc_lblCasaRuralA.gridwidth = 3;
		gbc_lblCasaRuralA.gridx = 0;
		gbc_lblCasaRuralA.gridy = 0;
		add(lblCasaRural, gbc_lblCasaRuralA);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.gridwidth = 2;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 1;
		add(comboBox, gbc_comboBox);

		lblReviewState = new JLabel("Estado de revision");
		lblReviewState.setEnabled(false);
		GridBagConstraints gbc_lblReviewState = new GridBagConstraints();
		gbc_lblReviewState.anchor = GridBagConstraints.WEST;
		gbc_lblReviewState.insets = new Insets(0, 0, 5, 5);
		gbc_lblReviewState.gridwidth = 2;
		gbc_lblReviewState.gridx = 0;
		gbc_lblReviewState.gridy = 2;
		add(lblReviewState, gbc_lblReviewState);


		rdbtnApproved = new JRadioButton("Approved");
		rdbtnApproved.setEnabled(false);
		rdbtnApproved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reviewState = ReviewState.APPROVED;
				lblDescription.setEnabled(false);
				textPane.setEnabled(false);
				textPane.setText("");
				textPanePrompt.setAlpha(64);
				btnSend.setEnabled(true);
			}
		});		
		buttonGroup.add(rdbtnApproved);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 3;
		add(rdbtnApproved, gbc_rdbtnNewRadioButton_1);

		rdbtnRejected = new JRadioButton("Rejected");
		rdbtnRejected.setEnabled(false);
		rdbtnRejected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reviewState = ReviewState.REJECTED;
				textPane.setEnabled(true);
				textPanePrompt.setAlpha(200);
				lblDescription.setEnabled(true);
				btnSend.setEnabled(true);
			}
		});
		buttonGroup.add(rdbtnRejected);
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 4;
		add(rdbtnRejected, gbc_rdbtnNewRadioButton);

		lblDescription = new JLabel("Descripcion de la revision");
		lblDescription.setEnabled(false);
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.SOUTH;
		gbc_lblDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridwidth = 2;
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 5;
		add(lblDescription, gbc_lblDescription);

		textPane = new JTextPane();
		textPane.setEnabled(false);
		RightClickMenu rightClickMenu = new RightClickMenu(textPane);
		textPane.setComponentPopupMenu(rightClickMenu);

		textPanePrompt = new TextPrompt(textPane);
		textPanePrompt.setBorder(new CompoundBorder());
		textPanePrompt.setText("(optional)");
		textPanePrompt.setStyle(Font.BOLD);
		textPanePrompt.setAlpha(64);
		textPanePrompt.setVerticalAlignment(SwingConstants.TOP);

		GridBagConstraints gbc_editorPane = new GridBagConstraints();
		gbc_editorPane.gridwidth = 3;
		gbc_editorPane.fill = GridBagConstraints.BOTH;
		gbc_editorPane.insets = new Insets(0, 0, 10, 0);
		gbc_editorPane.gridx = 0;
		gbc_editorPane.gridy = 6;
		add(textPane, gbc_editorPane);

		btnSend = new JButton("Enviar");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ruralHouse != null) {	
					description = textPane.getText();
					Review review = ruralHouse.getReview();
					review.setDescription(description);
					review.setState((Admin) MainWindow.user, reviewState);
					//Send the review
					MainWindow.getBusinessLogic().updateReview(ruralHouse, review);
					reviewState = null;
					buttonGroup.clearSelection();
					JOptionPane.showMessageDialog(null,	"Se ha enviado la revision correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);	
					System.out.println("Review of " + ruralHouse.toString() + ": " + description);
				} else {	
					JOptionPane.showMessageDialog(null, "No se puede enviar una revision sin su estado o su casa rural", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		lblNombre = new JLabel("Nombre");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 7;
		add(lblNombre, gbc_lblNombre);

		lblCiudad = new JLabel("Ciudad");
		lblCiudad.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblCiudad = new GridBagConstraints();
		gbc_lblCiudad.insets = new Insets(0, 0, 5, 5);
		gbc_lblCiudad.gridx = 1;
		gbc_lblCiudad.gridy = 7;
		add(lblCiudad, gbc_lblCiudad);

		lblDireccin = new JLabel("Direccion");
		lblDireccin.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblDireccin = new GridBagConstraints();
		gbc_lblDireccin.insets = new Insets(0, 0, 5, 0);
		gbc_lblDireccin.gridx = 2;
		gbc_lblDireccin.gridy = 7;
		add(lblDireccin, gbc_lblDireccin);

		lblNombreAtrib = new JLabel("");
		GridBagConstraints gbc_lblNombreAtrib = new GridBagConstraints();
		gbc_lblNombreAtrib.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreAtrib.gridx = 0;
		gbc_lblNombreAtrib.gridy = 8;
		add(lblNombreAtrib, gbc_lblNombreAtrib);

		lblCity = new JLabel("");
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCity.gridx = 1;
		gbc_lblCity.gridy = 8;
		add(lblCity, gbc_lblCity);

		lblAdress = new JLabel("");
		GridBagConstraints gbc_lblAdress = new GridBagConstraints();
		gbc_lblAdress.insets = new Insets(0, 0, 5, 0);
		gbc_lblAdress.gridx = 2;
		gbc_lblAdress.gridy = 8;
		add(lblAdress, gbc_lblAdress);

		lblDescripcin = new JLabel("Descripcion");
		lblDescripcin.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblDescripcin = new GridBagConstraints();
		gbc_lblDescripcin.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcin.gridx = 0;
		gbc_lblDescripcin.gridy = 9;
		add(lblDescripcin, gbc_lblDescripcin);

		textPane_1 = new JTextPane();
		textPane_1.setBackground(UIManager.getColor("Button.background"));
		textPane_1.setEnabled(false);
		GridBagConstraints gbc_textPane_1 = new GridBagConstraints();
		gbc_textPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_textPane_1.fill = GridBagConstraints.BOTH;
		gbc_textPane_1.gridx = 0;
		gbc_textPane_1.gridy = 10;
		add(textPane_1, gbc_textPane_1);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.anchor = GridBagConstraints.SOUTH;
		gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 10;
		add(btnSend, gbc_btnSend);
	}

	public void initializeComboBox() {
		ExtendedIterator<RuralHouse> rhs = MainWindow.getBusinessLogic().ruralHouseIterator();

		ruralHouses.addElement(null); //For that the user have selected a RuralHouse

		while(rhs.hasNext()) {
			ruralHouses.addElement((RuralHouse) rhs.next());
		}

	}
}
