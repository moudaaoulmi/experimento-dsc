package healthwatcher;


/**
 * This constants define system specific configurations.
 */
public class Constants {

	// Database Configuration
	public static final String DB_URL = "jdbc:mysql://localhost/test"; // "jdbc:odbc:test";
	public static final String DB_LOGIN = "root"; // "orbi2";
	public static final String DB_PASS = "asdffdsa"; //"orbi2";
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver"; //"sun.jdbc.odbc.JdbcOdbcDriver";

	// RMI Configuration
	public static final String SERVER_NAME = "localhost";
	public static final String SYSTEM_NAME = "HealthWatcher";

	// SERVLETS Configuration
	
	// You should point this path to the base of the forms in your system
	public static final String FORM_PATH = "/opt/tomcat/webapps/servlet/evolution/";
	//public static final String FORM_PATH = "c:\\eclipse\\workspace\\HealthWatcherOO_1_Base\\web\\healthwatcher\\formularios\\";	
	
	//public static final String SERVLET_SERVER_PATH = "localhost:8080/healthwatcher";
	public static final String SERVLET_SERVER_PATH = "localhost:8080/servlet/healthwatcher.view.servlets.";

	//public static final String SYSTEM_ROOT = "http://localhost:8080/healthwatcher/";
	public static final String SYSTEM_ROOT = "http://" + SERVLET_SERVER_PATH + "ServletWebServer?file=";

	//public static final String SYSTEM_ROOT = "http://localhost:8080/healthwatcher/";
	public static final String SYSTEM_ACTION = "http://" + SERVLET_SERVER_PATH + "HWServlet";

	//public static final String SYSTEM_INDEX = "http://"+SERVLET_SERVER_PATH + "ServletWebServer?file=index.html";
	public static final String SYSTEM_INDEX = SYSTEM_ROOT + "index.html";

	//public static final String SYSTEM_INDEX_ADMINISTRATOR = "http://"+SERVLET_SERVER_PATH+"ServletLogin";
	public static final String SYSTEM_INDEX_ADMINISTRATOR = SYSTEM_ACTION + "?operation=LoginMenu";

	public static final String SYSTEM_LOGIN = SYSTEM_ROOT + "Login.html";
	public static final String SYSTEM_QUERIES = SYSTEM_ROOT + "QueriesMenu.html";
	
	/**
	 * Defines if the system should be persistent or not (use DB or not)
	 * 
	 * @return true, if the system should use DB, false otherwise
	 */
	public static boolean isPersistent() {
		return true;
	}
}
