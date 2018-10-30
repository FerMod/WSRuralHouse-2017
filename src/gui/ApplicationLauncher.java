package gui;

import java.net.URL;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.ApplicationFacadeFactory;
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

			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

			ApplicationFacadeInterface aplicationFacade = ApplicationFacadeFactory.createApplicationFacade(config);

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
