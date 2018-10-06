package gui.user.owner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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

import businessLogic.ApplicationFacadeInterface;
import domain.City;
import domain.Owner;
import domain.Review.ReviewState;
import domain.RuralHouse;
import exceptions.DuplicatedEntityException;
import gui.components.TextPrompt;
import gui.user.MainWindow;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JCheckBox;
import java.awt.SystemColor;

public class NewRuralHouseWindow extends JDialog {

	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 6770177109582209536L;

	private JPanel contentPane;
	private JEditorPane editorPaneDescription;
	private JComboBox<City> comboBoxCity;
	private JLabel lblNewRuralHouse;
	private JLabel lblCity;
	private JButton btnAdd;
	private JButton btnCancel;
	private JEditorPane editorPaneTags;
	private JTextField textField;
	private JFileChooser fileChooser = new JFileChooser();
	private String imagePath;

	private JCheckBox chckbxNuevaCiudad;


	protected City city;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblDebenIrSeparados;
	private JLabel lblseleccionada;

	private Owner owner;

	/**
	 * Create the frame.
	 * @param owner the rural house owner
	 */
	public NewRuralHouseWindow(Owner owner) {
		this.owner = owner;
		setModalityType(ModalityType.TOOLKIT_MODAL);
		setResizable(false);
		setTitle("Nueva Casa Rural");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 529, 454);
		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/rural_house.png")));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Get screen dimension
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); //Set the screen location to the center of the screen

		initialize();

		getRootPane().setDefaultButton(btnAdd);

	}

	private void initialize() {

		setContentPane(getJContentPane());
		
		fileChooser.setDialogTitle("Elija una imagen preview para la casa rural");

		contentPane.add(getLblNewRuralHouse());
		contentPane.add(getEditorPaneDescription());
		contentPane.add(getComboBoxCity());
		contentPane.add(getLblCity());
		contentPane.add(getBtnCreate());
		contentPane.add(getBtnCancel());

	}

	private JPanel getJContentPane() {
		if(contentPane == null) {
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPane.setLayout(null);

			JLabel lblImages = new JLabel("Imagen preview de la casa:");
			lblImages.setBounds(264, 224, 183, 14);
			contentPane.add(lblImages);

			JButton btnNewButton = new JButton("Seleccionar imagen");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					imagePath = getImage();
				}
			});
			
			
			btnNewButton.setBounds(264, 248, 163, 23);
			contentPane.add(btnNewButton);

			editorPaneTags = new JEditorPane();
			applyStyle("Tags", editorPaneTags);
			editorPaneTags.setBounds(264, 300, 251, 73);
			contentPane.add(editorPaneTags);

			chckbxNuevaCiudad = new JCheckBox("Nueva ciudad");
			chckbxNuevaCiudad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(chckbxNuevaCiudad.isSelected()) {
						comboBoxCity.setEnabled(false);
						textField.setEnabled(true);
					} else {
						comboBoxCity.setEnabled(true);
						textField.setEnabled(false);
					}
				}
			});
			chckbxNuevaCiudad.setBounds(10, 276, 163, 23);
			contentPane.add(chckbxNuevaCiudad);

			textField = new JTextField();
			applyStyle("Nombre de la ciudad", textField);
			textField.setEnabled(false);
			textField.setBounds(20, 306, 203, 20);
			contentPane.add(textField);
			textField.setColumns(10);
			contentPane.add(getTextField_1());
			contentPane.add(getTextField_2());
			contentPane.add(getLblDebenIrSeparados());
			contentPane.add(getLblseleccionada());
		}
		return contentPane;
	}

	private JButton getBtnCancel() {		
		if(btnCancel == null) {
			btnCancel = new JButton("Cancelar");
			btnCancel.setBounds(260, 384, 251, 28);
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnCancel;
	}

	private JButton getBtnCreate() {
		if(btnAdd == null) {
			btnAdd = new JButton("Añadir");
			btnAdd.setBounds(10, 384, 240, 28);
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(fieldsFilled()) {
						try{
							ApplicationFacadeInterface facade = MainWindow.getBusinessLogic();
							String name = textField_1.getText();
							String description = editorPaneDescription.getText();
							String address = textField_2.getText();
							String[] tags = prepareTags(editorPaneTags.getText());
							
							if(!chckbxNuevaCiudad.isSelected()){
								city = (City) comboBoxCity.getSelectedItem();
							} else {
								city = facade.createCity(textField.getText());
							}

							RuralHouse rh = facade.createRuralHouse(owner, name, description, city, address);
							
							rh.setTags(tags);
							rh.addImage(new File(imagePath).toURI());
							rh.setOwner(owner);
							
							rh.getReview().setState(ReviewState.AWAITING_REVIEW);
							
							facade.update(rh);

							dispose();
							JOptionPane.showMessageDialog(null,	"Casa rural añadida exitosamente", "Info", JOptionPane.INFORMATION_MESSAGE);
						} catch(NullPointerException | IOException err) {
							JOptionPane.showMessageDialog(null,	"Compruebe que no se ha dejado ningún campo vacio, debe añadir también una foto", "No se ha podido crear la casa", JOptionPane.ERROR_MESSAGE);
						} catch(DuplicatedEntityException error) {
							JOptionPane.showMessageDialog(null,	"Tiene un campo repetido con otra casa ya existente, compruebelos de nuevo", "No se ha podido crear la casa", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return btnAdd;
	}

	private boolean fieldsFilled() {
		if(editorPaneDescription.getText().trim().equals("")){
			JOptionPane.showMessageDialog(this,	"El campo \"descripcion\", no puede estar vacio.", "Campo vacio", JOptionPane.WARNING_MESSAGE);
			return false;
		} else if(comboBoxCity.getSelectedIndex() == -1 && !chckbxNuevaCiudad.isSelected()) {
			JOptionPane.showMessageDialog(this,	"El campo \"ciudad\", no puede estar vacio.", "Campo vacio", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private JLabel getLblCity() {
		if(lblCity == null) {
			lblCity = new JLabel("Ciudad: ");
			lblCity.setBounds(14, 224, 117, 14);
		}
		return lblCity;
	}

	private JComboBox<City> getComboBoxCity() {
		if(comboBoxCity == null) {	
			comboBoxCity = new JComboBox<City>(new DefaultComboBoxModel<City>(MainWindow.getBusinessLogic().getCities()));
			comboBoxCity.setBounds(14, 249, 210, 20);
		}
		return comboBoxCity;
	}

	private JEditorPane getEditorPaneDescription() {
		if(editorPaneDescription == null) {	
			editorPaneDescription = new JEditorPane();
			applyStyle("Descripción", editorPaneDescription);
			editorPaneDescription.setBounds(10, 112, 501, 83);
		}
		return editorPaneDescription;
	}

	private JLabel getLblNewRuralHouse() {
		if(lblNewRuralHouse == null) {	
			lblNewRuralHouse = new JLabel("Casa Rural");
			lblNewRuralHouse.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewRuralHouse.setBounds(10, 11, 137, 28);
		}
		return lblNewRuralHouse;
	}

	private JTextField getTextField_1() {
		if (textField_1 == null) {
			textField_1 = new JTextField();
			textField_1.setBounds(10, 50, 163, 20);
			applyStyle("Nombre", textField_1);
			textField_1.setColumns(10);
		}
		return textField_1;
	}

	private JTextField getTextField_2() {
		if (textField_2 == null) {
			textField_2 = new JTextField();
			applyStyle("Dirección", textField_2);
			textField_2.setBounds(10, 81, 163, 20);
			textField_2.setColumns(10);
		}
		return textField_2;
	}

	private String[] prepareTags(String tags) {
		return (String[]) Arrays.asList(tags.split(";")).toArray();
	}

	private JLabel getLblDebenIrSeparados() {
		if (lblDebenIrSeparados == null) {
			lblDebenIrSeparados = new JLabel("(deben ir separados por ;)");
			lblDebenIrSeparados.setFont(new Font("Tahoma", Font.PLAIN, 9));
			lblDebenIrSeparados.setBounds(264, 280, 163, 14);
		}
		return lblDebenIrSeparados;
	}
	
	private String getImage() {
		int selectFile = fileChooser.showOpenDialog(contentPane);
		if(selectFile == JFileChooser.APPROVE_OPTION) {
			lblseleccionada.setVisible(true);
			return fileChooser.getSelectedFile().getAbsolutePath();
		} else {
			lblseleccionada.setVisible(false);
			return null;
		}
	}
	
	
	
	private JLabel getLblseleccionada() {
		if (lblseleccionada == null) {
			lblseleccionada = new JLabel("Seleccionada");
			lblseleccionada.setVisible(false);
			lblseleccionada.setForeground(SystemColor.textHighlight);
			lblseleccionada.setBounds(437, 252, 83, 14);
		}
		return lblseleccionada;
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
}
