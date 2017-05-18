package gui;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JDateChooser;

import domain.Admin;
import domain.City;
import domain.Offer;
import domain.Owner;
import domain.Review;
import domain.Review.ReviewState;
import gui.components.TextPrompt;
import gui.prototypes.NewRuralHouseWindow;
import domain.RuralHouse;


import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.ImageIcon;

public class OwnerRuralHousesPanel extends JPanel {
	private JTextField textField;	//Name of RuralHouse
	private JTextField textField_1; //Date fin	
	private JTextField textField_2; //Address
	private JTextField textField_3; //Date init
	private JTextField textField_4; //Price
	private JEditorPane editorPane; //Description

	private RuralHouse rh;
	private City city;

	private DefaultComboBoxModel<RuralHouse> ruralHousesOfOwner = new DefaultComboBoxModel<RuralHouse>();
	private DefaultComboBoxModel<City> someCities = new DefaultComboBoxModel<City>();


	private JComboBox comboBox;   //RuralHouses
	private JComboBox comboBox_1; //Cities

	private static String pattern = "dd/MM/yyyy";
	private static SimpleDateFormat format = new SimpleDateFormat(pattern);

	private Date dateIn;
	private Date dateFin;


	/**
	 * Create the panel.
	 */
	public OwnerRuralHousesPanel(JFrame frame) { //Need a JScrollPane
		//initializeRuralHousesComboBox(o);
		initializeCitiesComboBox();
		
		Border matterBorder = BorderFactory.createMatteBorder(1, 5, 1, 1, Color.GRAY);
		Border empty = new EmptyBorder(0, 5, 0, 0);
		CompoundBorder border = new CompoundBorder(matterBorder, empty);
		
		JButton btnNewButton = new JButton("Guardar");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String name = textField.getText();
					City city = (City) comboBox_1.getSelectedItem();
					String address = textField_2.getText();
					String description = editorPane.getText();

					rh.setName(name);
					rh.setCity(city);
					rh.setAddress(address);
					rh.setDescription(description);
					Review review = rh.getReview(); 
					Admin reviewer = review.getReviewer();
					review.setState(reviewer, ReviewState.AWAITING_REVIEW); //The state of review pass to be awaiting for edit the rural house
					rh.setReview(review);
					textField.setText("");
					textField_2.setText("");
					editorPane.setText("");


					//MainWindow.getBusinessLogic().update(rh);
					JOptionPane.showMessageDialog(null,	"Se ha actualizado la casa rural correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
				} catch(NullPointerException er) {
					JOptionPane.showMessageDialog(null,	"Compruebe que no se deja ningún campo vacío a la hora de editar la casa", "No se ha podido editar la casa rural", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(154, 568, 131, 26);
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


					if(dateIn.compareTo(new Date()) >= 0 && dateFin.compareTo(new Date()) >= 0 && dateFin.compareTo(dateIn) >= 0) {
						//Offer o = new Offer(dateIn, dateFin, Double.valueOf(textField_4.getText()), rh);
						textField_1.setText("");
						textField_3.setText("");
						textField_4.setText("");
						JOptionPane.showMessageDialog(null, "La oferta se ha creado correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "La fecha de inicio debe de ser anterior a la final y estas no pueden ser fechas pasadas", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
					}
				} catch (ParseException | NullPointerException | NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Revise si ha escrito mal el precio o el formato de las fechas introducidas no es el adecuado e inténtelo de nuevo", "La oferta no se ha podido crear", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				// formatting
				System.out.println(format.format(new Date()));
			}
		});
		btnNewButton_1.setBounds(368, 135, 131, 23);
		add(btnNewButton_1);

		JLabel lblMisCasas = new JLabel("Mis casas");
		lblMisCasas.setBounds(25, 11, 95, 31);

		comboBox = new JComboBox();
		comboBox.setModel(ruralHousesOfOwner);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rh = (RuralHouse) comboBox.getSelectedItem();
				textField.setText(rh.getName()); //Get the name of the rural house selected
				editorPane.setText(rh.getDescription()); //Get the description of the rural house selected
				textField_2.setText(rh.getAddress());	////Get the address of the rural house selected
			}
		});
		comboBox.setBounds(25, 44, 260, 52);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(25, 222, 72, 20);

		textField = new JTextField();
		textField.setBorder(border);
		textField.setEnabled(false);
		textField.setBounds(25, 253, 260, 20);
		textField.setColumns(10);

		JLabel lblDescripcin = new JLabel("Descripci\u00F3n");
		lblDescripcin.setBounds(25, 412, 95, 20);

		JLabel lblDireccin = new JLabel("Direcci\u00F3n");
		lblDireccin.setBounds(25, 363, 72, 20);

		textField_2 = new JTextField();
		textField_2.setBorder(border);
		textField_2.setEnabled(false);
		textField_2.setBounds(25, 386, 260, 20);
		textField_2.setColumns(10);
		setLayout(null);
		add(lblMisCasas);
		add(comboBox);
		add(lblNombre);
		add(textField);
		add(lblDescripcin);
		add(lblDireccin);
		add(textField_2);

		JLabel lblCiudad = new JLabel("Ciudad");
		lblCiudad.setBounds(25, 283, 72, 20);
		add(lblCiudad);

		comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				city = (City) comboBox_1.getSelectedItem();
			}
		});
		comboBox_1.setModel(someCities);
		comboBox_1.setEnabled(false);
		comboBox_1.setBounds(25, 314, 260, 38);
		add(comboBox_1);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Editar casa");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNewCheckBox.isSelected()) {
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
		editorPane.setBorder(border);
		editorPane.setEnabled(false);
		editorPane.setBounds(25, 434, 260, 127);
		add(editorPane);

		textField_1 = new JTextField();
		textField_1.setBounds(130, 135, 95, 22);
		textField_1.setBorder(border);
		add(textField_1);
		textField_1.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBounds(25, 135, 95, 22);
		textField_3.setBorder(border);
		add(textField_3);
		textField_3.setColumns(10);

		JLabel lblFechaDeInicio = new JLabel("Fecha inicio");
		lblFechaDeInicio.setBounds(25, 114, 131, 23);
		add(lblFechaDeInicio);

		JLabel lblFechaFinal = new JLabel("Fecha final");
		lblFechaFinal.setBounds(130, 120, 95, 10);
		add(lblFechaFinal);

		JLabel lblformatoAUtilizar = new JLabel("*Formato a utilizar DD/MM/YYYY");
		lblformatoAUtilizar.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblformatoAUtilizar.setForeground(Color.RED);
		lblformatoAUtilizar.setBounds(25, 168, 196, 14);
		add(lblformatoAUtilizar);

		JLabel lblPrecioPorNoche = new JLabel("Precio por noche");
		lblPrecioPorNoche.setBounds(235, 118, 102, 14);
		add(lblPrecioPorNoche);

		textField_4 = new JTextField();
		textField_4.setBounds(235, 135, 102, 23);
		textField_4.setBorder(border);
		add(textField_4);
		textField_4.setColumns(10);

		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//initializeRuralHousesComboBox(ow);
			}
		});
		btnNewButton_2.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/updaterhs0.gif")));
		btnNewButton_2.setBounds(295, 44, 52, 52);
		add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/editrh.gif")));
		lblNewLabel.setBounds(265, 192, 19, 19);
		add(lblNewLabel);
		
		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog newRuralHouse = new NewRuralHouseWindow();
				newRuralHouse.setVisible(true);
			}
		});
		button.setIcon(new ImageIcon(OwnerRuralHousesPanel.class.getResource("/img/newrhs0.gif")));
		button.setBounds(357, 44, 52, 52);
		add(button);

	}

	private void initializeRuralHousesComboBox(Owner ow) {
		Vector<RuralHouse> rhs = MainWindow.getBusinessLogic().getRuralHousesOfOwner(ow);

		ruralHousesOfOwner.addElement(null); //For that the owner have selected a RuralHouse

		for(RuralHouse rh : rhs) {
			ruralHousesOfOwner.addElement(rh);
		}	
	}

	private void initializeCitiesComboBox() {
		Vector<City> cities = MainWindow.getBusinessLogic().getCities();

		someCities.addElement(null); //For that the owner have selected a city

		for(City city : cities) {
			someCities.addElement(city);
		}	
	}
}
