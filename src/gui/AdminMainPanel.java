package gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import domain.Offer;
import domain.Review;
import domain.Review.ReviewState;
import gui.components.TextPrompt;
import domain.RuralHouse;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.CompoundBorder;


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
	private JTextPane textPane;
	private boolean enableButtonGroup = false;
	private TextPrompt textPanePrompt;

	/**
	 * Create the panel.
	 */
	public AdminMainPanel(JFrame frame) {
		initializeComboBox();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{107, 105, 155, 0};
		gridBagLayout.rowHeights = new int[]{24, 36, 24, 21, 0, 14, 131, 23, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JComboBox<RuralHouse> comboBox = new JComboBox<RuralHouse>();
		comboBox.setModel(ruralHouses);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				boolean enable;

				if (comboBox.getSelectedIndex() != -1) {
					ruralHouse = (RuralHouse)comboBox.getSelectedItem();
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

		lblReviewState = new JLabel("Estado de revisi\u00F3n");
		lblReviewState.setEnabled(false);
		GridBagConstraints gbc_lblReviewState = new GridBagConstraints();
		gbc_lblReviewState.anchor = GridBagConstraints.WEST;
		gbc_lblReviewState.insets = new Insets(0, 0, 5, 5);
		gbc_lblReviewState.gridwidth = 2;
		gbc_lblReviewState.gridx = 0;
		gbc_lblReviewState.gridy = 2;
		add(lblReviewState, gbc_lblReviewState);


		JRadioButton rdbtnApproved = new JRadioButton("Approved");
		rdbtnApproved.setEnabled(false);
		rdbtnApproved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reviewState = ReviewState.APPROVED;
				lblDescription.setEnabled(false);
				textPane.setEnabled(false);
				textPane.setText("");
				textPanePrompt.setAlpha(64);
				btnSend.setEnabled(false);
			}
		});		
		buttonGroup.add(rdbtnApproved);
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 3;
		add(rdbtnApproved, gbc_rdbtnNewRadioButton_1);

		JRadioButton rdbtnRejected = new JRadioButton("Rejected");
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

		lblDescription = new JLabel("Descripci\u00F3n de la revisi\u00F3n");
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
					review.setState(review.getReviewer(), reviewState);
					//Send the review
					MainWindow.getBusinessLogic().updateReview(ruralHouse, review);
					reviewState = null;
					buttonGroup.clearSelection();
					JOptionPane.showMessageDialog(null,	"Se ha enviado la revisión correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);	
					System.out.println("Review of " + ruralHouse.toString() + ": " + description);
				} else {	
					JOptionPane.showMessageDialog(null, "No se puede enviar una revisión sin su estado o su casa rural", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.anchor = GridBagConstraints.SOUTH;
		gbc_btnSend.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 7;
		add(btnSend, gbc_btnSend);

	}

	public void initializeComboBox() {
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHouses();

		ruralHouses.addElement(null); //For that the user have selected a RuralHouse

		for(RuralHouse rh : rhs) {
			ruralHouses.addElement(rh);
		}

	}
}
