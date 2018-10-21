package gui;

import java.net.URL;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;
import businessLogic.util.LogFile;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import gui.debug.ConsoleKeyEventDispatcher;
import gui.user.MainWindow;
import gui.user.SharedFrame;

public class ApplicationLauncher {

	public static void main(String[] args) {

		try {

			LogFile.FILE_NAME = "error.log";

			ConfigXML config = ConfigXML.getInstance();

			System.out.println(config.getLocale());

			Locale.setDefault(new Locale(config.getLocale()));

			System.out.println("Locale: " + Locale.getDefault());
			
			ConsoleKeyEventDispatcher consoleKeyEventDispatcher = new ConsoleKeyEventDispatcher();
			if(config.enableConsole()) {			
				consoleKeyEventDispatcher.showConsole();
			}

			SharedFrame sharedFrame = new SharedFrame();
			sharedFrame.setVisible(true);

			ApplicationFacadeInterface aplicationFacade;
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			if (config.isBusinessLogicLocal()) {
				aplicationFacade = new ApplicationFacadeImpl();
				DataAccess dataAccess = new DataAccess();
				aplicationFacade.setDataAccess(dataAccess);
			} else { //Si es remoto

				//String serviceName = "http://localhost:9999/ws/ruralHouses?wsdl";
				String serviceName= "http://"+config.getBusinessLogicNode() +":"+ config.getBusinessLogicPort()+"/ws/"+config.getBusinessLogicName()+"?wsdl";

				//URL url = new URL("http://localhost:9999/ws/ruralHouses?wsdl");
				URL url = new URL(serviceName);


				//1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");

				Service service = Service.create(url, qname);

				aplicationFacade = service.getPort(ApplicationFacadeInterface.class);
			} 

			//if (c.getDataBaseOpenMode().equals("initialize")) {
			//    appFacadeInterface.initializeBD();
			//}

			MainWindow.setBussinessLogic(aplicationFacade);

		} catch (Exception e) {

			System.err.println("An error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"\nTo show the console output press \"F12\"");
			LogFile.log(e, true);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,	"An error has occurred.\nTo see more detailed information, go to \"" + LogFile.getAbsolutePath() + "\"\nTo show the console output press \"F12\"", "Error!", JOptionPane.ERROR_MESSAGE);
			
		}

		//a.pack();

	}

}
