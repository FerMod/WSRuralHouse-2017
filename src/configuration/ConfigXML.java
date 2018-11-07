package configuration;

import java.io.File;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import configuration.util.CurrencyLocale;

public class ConfigXML implements Serializable, Config {

	private static final String FILE_PATH = "resources/config.xml";

	private boolean showConsole;

	private String businessLogicNode;

	private String businessLogicPort;

	private String businessLogicName;

	private static String databaseFileName;

	private boolean databaseInitValues;

	private boolean databaseOverwriteFile;

	//Two possible values: true (no instance of RemoteServer needs to be launched) or false (RemoteServer needs to be run first)
	private boolean businessLogicLocal;

	//Two possible values: true (if the database is in same node as business logic ) or false (in other case)
	private boolean databaseLocal;

	private String databaseNode;

	private int databasePort;

	private String user;

	private String password;

	private CurrencyLocale locale;

	private volatile static Config instance;

	private ConfigXML(){

		if (instance != null){
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}

		loadConfig(FILE_PATH);		

	}

	private void loadConfig(String filePath) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filePath));
			//System.out.println("Opening config.xml: "+getClass().getClassLoader().getResource("../resources/config.xml").getFile());
			//Document doc = dBuilder.parse(new File(getClass().getClassLoader().getResource("../resources/config.xml").getFile()));



			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("config");
			Element config = (Element) list.item(0); // list.item(0) is a Node that is an Element


			//Two possible values: true (no instance of RemoteServer needs to be launched) or false (RemoteServer needs to be run first)
			String value= ((Element)config.getElementsByTagName("businessLogic").item(0)).getAttribute("local");
			businessLogicLocal=value.equals("true");

			businessLogicNode = getTagValue("businessLogicNode", config);

			businessLogicPort = getTagValue("businessLogicPort", config);

			businessLogicName = getTagValue("businessLogicName", config);

			locale = CurrencyLocale.valueOf(getTagValue("locale", config).toUpperCase());

			//javaPolicyPath= getTagValue("javaPolicyPath", config);

			databaseFileName = getTagValue("databaseFileName", config);

			//Two possible values: true (no instance of RemoteServer needs to be launched) or false (RemoteServer needs to be run first)
			value= ((Element)config.getElementsByTagName("database").item(0)).getAttribute("local");
			databaseLocal = value.equals("true");

			databaseInitValues = getTagValue("databaseInitValues", config).equals("true");

			databaseOverwriteFile = getTagValue("databaseOverwriteFile", config).equals("true");

			databaseNode = getTagValue("databaseNode", config);


			databasePort=Integer.parseInt(getTagValue("databasePort", config));

			user=getTagValue("user", config);

			password=getTagValue("password", config);

			showConsole = getTagValue("showConsole", config).equals("true");

			System.out.print("Read from config.xml: ");
			System.out.print("\tbusinessLogicLocal = " + businessLogicLocal);
			System.out.print("\tdatabaseLocal = " + databaseLocal);
			System.out.println("\tdatabaseInitValues = "+ databaseInitValues); 

		} catch (Exception e) {
			System.out.println("Exception thrown when trying to load the xml configuration.");
			e.printStackTrace();
		}

	}

	private static String getTagValue(String sTag, Element eElement) {

		NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	/**
	 * Ensure that nobody can create another instance by serializing and deserializing the singleton.
	 * @return the current singleton instance
	 * @see Serializable
	 */
	protected Config readResolve() {
		return getInstance();
	}

	/* (non-Javadoc)
	 * @see configuration.Config#enableConsole()
	 */
	@Override
	public boolean enableConsole() {
		return showConsole;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getLocale()
	 */
	@Override
	public CurrencyLocale getLocale() {
		return locale;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getDatabasePort()
	 */
	@Override
	public int getDatabasePort() {
		return databasePort;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getUser()
	 */
	@Override
	public String getUser() {
		return user;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#isLocalDatabase()
	 */
	@Override
	public boolean isLocalDatabase() {
		return databaseLocal;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#isBusinessLogicLocal()
	 */
	@Override
	public boolean isBusinessLogicLocal() {
		return businessLogicLocal;
	}

	public synchronized static Config getInstance() {
		if(instance == null) {
			synchronized (ConfigXML.class) {
				if(instance == null) instance = new ConfigXML();
			}
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getBusinessLogicNode()
	 */
	@Override
	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getBusinessLogicPort()
	 */
	@Override
	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getBusinessLogicName()
	 */
	@Override
	public String getBusinessLogicName() {
		return businessLogicName;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getDbFilename()
	 */
	@Override
	public String getDBFilename(){
		return databaseFileName;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#initValues()
	 */
	@Override
	public boolean initValues(){
		return databaseInitValues;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#overwriteFile()
	 */
	@Override
	public boolean overwriteFile(){
		return databaseOverwriteFile;
	}

	/* (non-Javadoc)
	 * @see configuration.Config#getDatabaseNode()
	 */
	@Override
	public String getDatabaseNode() {
		return databaseNode;
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = 1972352637174060245L;

}
