package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;

import businessLogic.ApplicationFacadeInterfaceWS;
import businessLogic.FacadeImplementationWS;

public class ApplicationLauncher {



	public static void main(String[] args) {

		try {
			ConfigXML c = ConfigXML.getInstance();

			System.out.println(c.getLocale());

			Locale.setDefault(new Locale(c.getLocale()));



			System.out.println("Locale: "+Locale.getDefault());

			SharedFrame sharedFrame = new SharedFrame();
			sharedFrame.setVisible(true);



			ApplicationFacadeInterfaceWS appFacadeInterface;
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			if (c.isBusinessLogicLocal()) {

				appFacadeInterface = new FacadeImplementationWS();

			} else { //Si es remoto

				//String serviceName = "http://localhost:9999/ws/ruralHouses?wsdl";
				String serviceName= "http://"+c.getBusinessLogicNode() +":"+ c.getBusinessLogicPort()+"/ws/"+c.getBusinessLogicName()+"?wsdl";

				//URL url = new URL("http://localhost:9999/ws/ruralHouses?wsdl");
				URL url = new URL(serviceName);


				//1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");

				Service service = Service.create(url, qname);

				appFacadeInterface = service.getPort(ApplicationFacadeInterfaceWS.class);
			} 

			//if (c.getDataBaseOpenMode().equals("initialize")) {
			//    appFacadeInterface.initializeBD();
			//}

			MainGUI.setBussinessLogic(appFacadeInterface);

		} catch (Exception e) {	
			try {
				File file = new File("error.log");
				FileOutputStream fos = new FileOutputStream(file, true);
				PrintStream ps = new PrintStream(fos);
				System.setErr(ps);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			}
			JOptionPane.showMessageDialog(null,	"An error has occurred:\n " + e.getStackTrace(), "ERROR!", JOptionPane.ERROR_MESSAGE);
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}

		//a.pack();

	}

}
