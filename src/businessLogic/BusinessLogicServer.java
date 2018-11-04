package businessLogic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import configuration.Config;
import configuration.ConfigXML;

import javax.swing.JTextArea;
import javax.xml.ws.Endpoint;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BusinessLogicServer extends JDialog {

	private static final long serialVersionUID = 7811026526400972222L;

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	@SuppressWarnings("unused")
	private ApplicationFacadeInterface server;
	private String service;

	/**
	 * Launch the application.
	 * @param args arguments
	 */
	public static void main(String[] args) {
		try {
			BusinessLogicServer dialog = new BusinessLogicServer();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BusinessLogicServer() {
		setTitle("BusinessLogicServer: running the business logic");
		setBounds(100, 100, 486, 209);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		contentPanel.add(textArea);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.append("\n\n\nClosing the server... ");

				//server.close();

				System.exit(1);
			}
		});

		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		Config c = ConfigXML.getInstance();


		if (c.isBusinessLogicLocal()) {
			textArea.append("\nERROR, the business logic is configured as local");
		} else {

			try {

				if (!c.isLocalDatabase()) {
					textArea.append("\nWARNING: Please be sure ObjectdbManagerServer is launched\n           in machine: "+c.getDatabaseNode()+" port: "+c.getDatabasePort()+"\n");
					//textArea.append("\n java -cp objectdb.jar com.objectdb.Server -port "+c.getDatabasePort()+" start");
					this.setVisible(true);			
				}

				service= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName();

				Endpoint.publish(service, new ApplicationFacadeImpl());

				textArea.append("\n\nRunning service at:\n\t" + service);
				textArea.append("\n\n\nPress button to exit this server... ");

			} catch (Exception e) {
				textArea.append(e.toString());
			}

		}

	}

}
