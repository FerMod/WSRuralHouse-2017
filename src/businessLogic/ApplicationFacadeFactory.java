package businessLogic;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.util.LogFile;
import configuration.ConfigXML;
import dataAccess.DataAccess;

public final class ApplicationFacadeFactory {

	private ApplicationFacadeFactory() {
	}

	public static ApplicationFacadeInterface createApplicationFacade(ConfigXML config) {
		
		ApplicationFacadeInterface aplicationFacade = null;
		
		if (config.isBusinessLogicLocal()) {
			
			aplicationFacade = new ApplicationFacadeImpl();
			DataAccess dataAccess = new DataAccess();
			aplicationFacade.setDataAccess(dataAccess);
			
		} else { //Si es remoto

			//String serviceName = "http://localhost:9999/ws/ruralHouses?wsdl";
			String serviceName= "http://"+config.getBusinessLogicNode() +":"+ config.getBusinessLogicPort()+"/ws/"+config.getBusinessLogicName()+"?wsdl";

			//URL url = new URL("http://localhost:9999/ws/ruralHouses?wsdl");
			URL url = null;
			try {
				url = new URL(serviceName);


				//1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");

				Service service = Service.create(url, qname);

				aplicationFacade = service.getPort(ApplicationFacadeInterface.class);

			} catch (MalformedURLException e) {
				LogFile.log(e, true);
				e.printStackTrace();
			}
			
		} 
		return aplicationFacade;
	}	

}
