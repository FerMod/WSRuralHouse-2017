package businessLogic;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import businessLogic.ApplicationFacadeImpl;
import businessLogic.ApplicationFacadeInterface;
import configuration.Config;
import configuration.ConfigXML;

public class AppFacade implements Serializable {	

	private volatile static ApplicationFacadeInterface instance;

	public static String CONFIG_FILEPATH = "resources/config.xml";
	private static Config config;

	private AppFacade(){		
		this(config);
	}

	private AppFacade(Config config){
		if (instance != null){
			throw new RuntimeException("Use getInstance() method to get the single instance of the facade class.");
		}
	}

	/**
	 * Ensure that nobody can create another instance by serializing and deserializing the singleton.
	 * @return the current singleton instance
	 * @see Serializable
	 */
	protected ApplicationFacadeInterface readResolve() {
		return getInstance();
	}

	/**
	 * Return the current instance of the object. <br>
	 * Creates a new instance if none exists.
	 * 
	 * @return the current or new instance of this object
	 */
	public synchronized static ApplicationFacadeInterface getInstance() {
		if(instance == null) {
			synchronized (ApplicationFacadeInterface.class) {
				if(instance == null) instance = createApplicationFacade(config);
			}
		}
		return instance;
	}

	/**
	 * Loads the configuration file and return a new instance of this object with the configuration loaded.
	 * 
	 * @param config the configuration instance
	 * @return the new instance with the loaded configuration
	 */
	public synchronized static ApplicationFacadeInterface loadConfig(Config config) {
		AppFacade.config = config;
		return getInstance();
	}

	public static ApplicationFacadeInterface createApplicationFacade() {
		return createApplicationFacade(config);
	}

	public static ApplicationFacadeInterface createApplicationFacade(Config config) {

		AppFacade.config = config != null ? config : ConfigXML.loadConfig(AppFacade.class.getResource(CONFIG_FILEPATH).getFile());

		if (config.isBusinessLogicLocal()) {
			instance = new ApplicationFacadeImpl();
		} else {
			//Es remoto
			//String serviceName = "http://localhost:9999/ws/ruralHouses?wsdl";
			String serviceName = "http://" + config.getBusinessLogicNode() + ":" + config.getBusinessLogicPort() + "/ws/" + config.getBusinessLogicName() + "?wsdl";

			//URL url = new URL("http://localhost:9999/ws/ruralHouses?wsdl");
			URL url = null;
			try {
				url = new URL(serviceName);


				//1st argument refers to wsdl document above
				//2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "FacadeImplementationWSService");

				Service service = Service.create(url, qname);

				instance = service.getPort(ApplicationFacadeInterface.class);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = -2628567254888914298L;

}
