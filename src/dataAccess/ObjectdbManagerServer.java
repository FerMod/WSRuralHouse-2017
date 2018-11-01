package dataAccess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import configuration.Config;
import configuration.ConfigXML;

import javax.swing.JTextArea;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ObjectdbManagerServer extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JTextArea textArea;
	Config c;

	/**
	 * Launch the application. 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			ObjectdbManagerServer dialog = new ObjectdbManagerServer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ObjectdbManagerServer() {
		setTitle("ObjectdbManagerServer: running the database server");
		setBounds(100, 100, 486, 180);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			textArea = new JTextArea();
			contentPanel.add(textArea);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						textArea.append("\n\n\nClosing the database... ");
						try {
							System.out.println("Server close");
							try {
								Runtime.getRuntime().exec("java -cp lib\\objectdb.jar com.objectdb.Server -port "+ c.getDatabasePort()+" stop");
							} catch (Exception ioe) {
								System.out.println (ioe);
							}

							System.exit(1);

						} catch (Exception e1) {
						}
						System.exit(1);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		c=ConfigXML.getInstance();

		if (c.isLocalDatabase()) {
			textArea.append("\nERROR, the database is configured as local");
		}
		else {
			try{

				System.out.println("Lauching objectdb server");
				try {
					Runtime.getRuntime().exec("java -cp resources\\objectdb.jar com.objectdb.Server -port "+ c.getDatabasePort()+" start");
				} catch (Exception ioe) {
					System.out.println (ioe);
				}

				textArea.append("\nAccess granted to: "+c.getUser());

				textArea.append("\nPress button to exit this database server... ");

			} catch (Exception e) {
				textArea.append("Something has happened in ObjectdbManagerServer: "+e.toString());

			}

		}
	}
}

