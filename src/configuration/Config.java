package configuration;

import configuration.util.CurrencyLocale;

public interface Config {
	
	/**
	 * Obtain the current configuration file path
	 * 
	 * @return the configuration file path
	 */
	String getFilePath();

	boolean enableConsole();

	CurrencyLocale getLocale();

	int getDatabasePort();

	String getUser();

	String getPassword();

	boolean isLocalDatabase();

	boolean isBusinessLogicLocal();

	String getBusinessLogicNode();

	String getBusinessLogicPort();

	String getBusinessLogicName();

	String getDBFilename();

	boolean initValues();

	boolean overwriteFile();

	String getDatabaseNode();

}
