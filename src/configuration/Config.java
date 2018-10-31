package configuration;

import java.util.Locale;

public interface Config {

	boolean enableConsole();

	Locale getLocale();

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