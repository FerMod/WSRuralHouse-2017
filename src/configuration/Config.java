package configuration;

import configuration.util.CurrencyLocale;

public interface Config {

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

	String getDbFilename();

	boolean initValues();

	boolean overwriteFile();

	String getDatabaseNode();

}
