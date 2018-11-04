package gui.user.owner;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import domain.Admin;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.Review;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.BadDatesException;
import exceptions.OverlappingOfferException;
import gui.components.TextPrompt;
import gui.user.MainWindow;

public class OwnerRuralHousesPanel extends JPanel {
	
	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 6667016759542877447L;
	
	private JTextField textField;	//Name of RuralHouse
	private JTextField textField_1; //Date fin	
	private JTextField textField_2; //Address
	private JTextField textField_3; //Date init
	private JTextField textField_4; //Price
	private JEditorPane editorPane; //Description
	private JFileChooser fileChooser = new JFileChooser();
	
	private RuralHouse rh;
	private City city;

	private DefaultComboBoxModel<RuralHouse> ruralHousesOfOwner = new DefaultComboBoxModel<RuralHouse>();
	private DefaultComboBoxModel<City> someCities = new DefaultComboBoxModel<City>();

	private JComboBox<RuralHouse> comboBox; //RuralHouses
	private JComboBox<City> comboBox_1; 	//Cities

	private static String pattern = "dd/MM/yyyy";
	private static SimpleDateFormat format = new SimpleDateFormat(pattern);

	private Date dateIn;
	private Date dateFin;
	private JTextField textField_5;
	private JCheckBox chckbxNuevaCiudad;

	private JLabel lblSeleccionada;

	protected String imagePath;

	private JLabel lblNewLabel_1;

	private JFrame parentFrame;

	/**
	 * Create the panel.
	 * @param frame the parent frame
	 */
	public OwnerRuralHousesPanel(JFrame frame) { //Need a JScrollPane		
		
		this.setParentFrame(frame);
		
		initializeRuralHousesComboBox((Owner)MainWindow.user);
		
		initializeCitiesComboBox();
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String name = textField.getText();
					String address = textField_2.getText();
					String description = editorPane.getText();

					if(!chckbxNuevaCiudad.isSelected()) {
						city = (City) comboBox_1.getSelectedItem();
					} else {
						city = MainWindow.getBusinessLogic().createCity(textField_5.getText());
					}

					rh.setName(name);
					rh.setCity(city);
					rh.setAddress(address);
					rh.setDescription(description);
					rh.removeImage(0);
					rh.addImage(new File(imagePath).toURI());
					Review review = rh.getReview(); 
					Admin reviewer = review.getReviewer();
					review.setState(reviewer, ReviewState.AWAITING_REVIEW); //The state of review pass to be awaiting for edit the rural house
					rh.setReview(review);
					textField.setText("");
					textField_2.setText("");
					textField_5.setText("");
					editorPane.setText("");


					MainWindow.getBusinessLogic().update(rh);
					JOptionPane.showMessageDialog(null,	"Se ha actualizado la casa rural correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);

					if(chckbxNuevaCiudad.isSelected()) {
						someCities.addElement(city);
						comboBox_1.setModel(someCities);
					}
				} catch(NullPointerException er) {
					JOptionPane.showMessageDialog(null,	"Compruebe que no se deja ningún campo vacio a la hora de editar la casa", "No se ha podido editar la casa rural", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(154, 502, 131, 26);
		add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Crear oferta");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Here create the booking with the dates and the rural house
				try {
					dateIn = format.parse(textField_3.getText());
					dateFin = format.parse(textField_1.getText());
					System.out.println(dateIn);
					System.out.println(dateFin);

					if(rh!=null && rh.getReview().getState() == ReviewState.APPROVED) {
						if(dateIn.compareTo(new Date()) >= 0 && dateFin.compareTo(new Date()) >= 0) {
							Offer o = MainWindow.getBusinessLogic().createOffer(rh, dateIn, dateFin, Double.valueOf(textField_4.getText()));
							textField_1.setText("");
							textField_3.setText("");
							textField_4.setText("");
							JOptionPane.showMessageDialog(null, "La oferta se ha creado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "La fecha de inicio debe de ser anterior a la final y estas no pueden ser fechas pasadas", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Debe seleccionar una casa rural válida y aprobada por un admin", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
					}
				} catch (ParseException | NullPointerException | NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Revise si ha escrito mal el precio o el formato de las fechas introducidas no es el adecuado e inténtelo de nuevo", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
				} catch (OverlappingOfferException e) {
					JOptionPane.showMessageDialog(null, "La fecha de inicio debe de ser anterior a la final y estas no pueden ser fechas pasadas", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
				} catch (BadDatesException e) {
					JOptionPane.showMessageDialog(null, "Las fechas no se han escrito correctamente, por favor, vuelva a intentarlo", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
				}
				// formatting
				System.out.println(format.format(new Date()));
			}
		});
		btnNewButton_1.setBounds(357, 135, 131, 23);
		add(btnNewButton_1);

		JLabel lblMisCasas = new JLabel("Mis casas");
		lblMisCasas.setBounds(25, 11, 95, 31);

		comboBox = new JComboBox<RuralHouse>();
		comboBox.setModel(ruralHousesOfOwner);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rh = (RuralHouse) comboBox.getSelectedItem();
				textField.setText(rh.getName()); //Get the name of the rural house selected
				editorPane.setText(rh.getDescription()); //Get the description of the rural house selected
				textField_2.setText(rh.getAddress());	////Get the address of the rural house selected
				comboBox_1.setEditable(true);
				comboBox_1.setSelectedItem(rh.getCity());
				comboBox_1.setEditable(false);
				
				if(rh.getReview().getState() == ReviewState.APPROVED) {
					lblNewLabel_1.setText("APROBADA");
					lblNewLabel_1.setForeground(new Color(0, 128, 0));
				} else if(rh.getReview().getState() == ReviewState.REJECTED) {
					lblNewLabel_1.setText("DENEGADA");
					lblNewLabel_1.setForeground(new Color(255, 0, 0));
				} else if(rh.getReview().getState() == ReviewState.AWAITING_REVIEW) {
					lblNewLabel_1.setText("EN ESPERA");
					lblNewLabel_1.setForeground(new Color(255, 140, 0));
				}
			}
		});
		comboBox.setBounds(25, 44, 260, 52);

		textField = new JTextField();

		applyStyle("Nombre", textField);

		textField.setEnabled(false);
		textField.setBounds(25, 222, 260, 20);
		textField.setColumns(10);

		textField_2 = new JTextField();

		applyStyle("Dirección", textField_2);

		textField_2.setEnabled(false);
		textField_2.setBounds(25, 333, 260, 20);
		textField_2.setColumns(10);
		setLayout(null);
		add(lblMisCasas);
		add(comboBox);
		add(textField);
		add(textField_2);

		JLabel lblCiudad = new JLabel("Ciudad");
		lblCiudad.setBounds(25, 253, 72, 20);
		add(lblCiudad);

		comboBox_1 = new JComboBox<City>();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				city = (City) comboBox_1.getSelectedItem();
			}
		});
		comboBox_1.setModel(someCities);
		comboBox_1.setEnabled(false);
		comboBox_1.setBounds(25, 284, 260, 38);
		add(comboBox_1);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Editar casa");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
					chckbxNuevaCiudad.setEnabled(true);
					comboBox_1.setEnabled(true);
					textField.setEnabled(true);
					textField_1.setEnabled(false);
					textField_3.setEnabled(false);
					editorPane.setEnabled(true);
					textField_2.setEnabled(true);
					textField_4.setEnabled(false);
					btnNewButton_1.setEnabled(false);
					btnNewButton.setEnabled(true);
				} else {
					chckbxNuevaCiudad.setEnabled(false);
					comboBox_1.setEnabled(false);
					textField.setEnabled(false);
					textField_1.setEnabled(true);
					textField_3.setEnabled(true);
					editorPane.setEnabled(false);
					textField_2.setEnabled(false);
					textField_4.setEnabled(true);
					btnNewButton_1.setEnabled(true);
					btnNewButton.setEnabled(false);
				}
			}
		});
		chckbxNewCheckBox.setBounds(21, 192, 99, 23);
		add(chckbxNewCheckBox);

		editorPane = new JEditorPane();

		applyStyle("Descripción", editorPane);

		editorPane.setEnabled(false);
		editorPane.setBounds(25, 364, 260, 127);
		add(editorPane);

		textField_1 = new JTextField();
		textField_1.setBounds(130, 135, 95, 22);

		applyStyle("Fecha final", textField_1);

		add(textField_1);
		textField_1.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBounds(25, 135, 95, 22);

		applyStyle("Fecha inicio", textField_3);

		add(textField_3);
		textField_3.setColumns(10);

		JLabel lblformatoAUtilizar = new JLabel("*Formato a utilizar DD/MM/YYYY");
		lblformatoAUtilizar.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblformatoAUtilizar.setForeground(Color.RED);
		lblformatoAUtilizar.setBounds(25, 168, 196, 14);
		add(lblformatoAUtilizar);

		textField_4 = new JTextField();
		textField_4.setBounds(235, 135, 112, 23);

		applyStyle("Precio por noche", textField_4);

		add(textField_4);
		textField_4.setColumns(10);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setToolTipText("Refresh rural house list");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBox.setEnabled(false);
				ruralHousesOfOwner = new DefaultComboBoxModel<RuralHouse>();
				comboBox.setModel(ruralHousesOfOwner);
				initializeRuralHousesComboBox((Owner)MainWindow.user);
				comboBox.setEnabled(true);
			}
		});
		btnNewButton_2.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/updaterhs0.gif")));
		btnNewButton_2.setBounds(295, 44, 52, 52);
		add(btnNewButton_2);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/editrh.gif")));
		lblNewLabel.setBounds(546, 192, 19, 19);
		add(lblNewLabel);

		JButton button = new JButton("");
		button.setToolTipText("Add new rural house");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog newRuralHouse = new NewRuralHouseWindow((Owner)MainWindow.user);
				newRuralHouse.setVisible(true);
			}
		});
		button.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/newrhs0.gif")));
		button.setBounds(357, 44, 52, 52);
		add(button);

		chckbxNuevaCiudad = new JCheckBox("Nueva ciudad");
		chckbxNuevaCiudad.setEnabled(false);
		chckbxNuevaCiudad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxNuevaCiudad.isSelected()) {
					comboBox_1.setEnabled(false);
					textField_5.setEnabled(true);
				} else {
					comboBox_1.setEnabled(true);
					textField_5.setEnabled(false);
				}
			}
		});
		chckbxNuevaCiudad.setBounds(295, 252, 150, 23);
		add(chckbxNuevaCiudad);

		textField_5 = new JTextField();
		textField_5.setEnabled(false);
		applyStyle("Nombre de la ciudad", textField_5);
		textField_5.setBounds(305, 284, 260, 20);
		add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblAvatarDeLa = new JLabel("Avatar de la casa");
		lblAvatarDeLa.setBounds(317, 336, 112, 14);
		add(lblAvatarDeLa);
		
		JButton btnCambiarImagen = new JButton("Cambiar imagen");
		btnCambiarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imagePath = getImage();
			}
		});
		btnCambiarImagen.setBounds(317, 358, 128, 26);
		add(btnCambiarImagen);
		
		lblSeleccionada = new JLabel("Seleccionada");
		lblSeleccionada.setEnabled(false);
		lblSeleccionada.setVisible(false);
		lblSeleccionada.setForeground(SystemColor.textHighlight);
		lblSeleccionada.setBounds(455, 364, 86, 14);
		add(lblSeleccionada);
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(305, 225, 260, 14);
		add(lblNewLabel_1);

	}

	private void initializeRuralHousesComboBox(Owner ow) {
		
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHouses(ow);
		for(RuralHouse rh : rhs) {
			ruralHousesOfOwner.addElement(rh);
		}	
		
	}

	private void initializeCitiesComboBox() {
		
		Vector<City> cities = MainWindow.getBusinessLogic().getCities();
		
		for(City city : cities) {
			someCities.addElement(city);
		}	
		
	}

	private JTextComponent applyStyle(String tipText, JTextComponent textComponent) {
		TextPrompt textPrompt = new TextPrompt(textComponent);
		textPrompt.setText(tipText);
		textPrompt.setStyle(Font.BOLD);
		textPrompt.setAlpha(128);
		Border outsideBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
		Border insideBorder = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(outsideBorder, insideBorder);
		textComponent.setBorder(border);
		return textComponent;
	}
	
	private String getImage() {
		int selectFile = fileChooser.showOpenDialog(this);
		if(selectFile == JFileChooser.APPROVE_OPTION) {
			lblSeleccionada.setVisible(true);
			return fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			lblSeleccionada.setVisible(false);
			return null;
		}
	}

	public JFrame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}
}
