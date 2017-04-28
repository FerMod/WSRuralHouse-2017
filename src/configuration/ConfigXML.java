package configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigXML {
	
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

	private String locale;
	
	private static ConfigXML theInstance = new ConfigXML();

	private ConfigXML(){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("resources/config.xml"));
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

			locale = getTagValue("locale", config);



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
			System.out.println("Error in ConfigXML.java: problems with config.xml");
			e.printStackTrace();
		}		
		
	}

	private static String getTagValue(String sTag, Element eElement) {
		
		NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}

	public boolean enableConsole() {
		return showConsole;
	}

	public String getLocale() {
		return locale;
	}

	public int getDatabasePort() {
		return databasePort;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isLocalDatabase() {
		return databaseLocal;
	}

	public boolean isBusinessLogicLocal() {
		return businessLogicLocal;
	}

	public static ConfigXML getInstance() {
		return theInstance;
	}

	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	public String getBusinessLogicName() {
		return businessLogicName;
	}

	public String getDbFilename(){
		return databaseFileName;
	}

	public boolean initValues(){
		return databaseInitValues;
	}
	
	public boolean overwriteFile(){
		return databaseOverwriteFile;
	}

	public String getDatabaseNode() {
		return databaseNode;
	}

}
