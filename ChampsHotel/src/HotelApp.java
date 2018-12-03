import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class HotelApp {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	// Use your password for mySQL
	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "Koko8764735!";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database we are testing with (this default is installed with MySQL) */
	private final String dbName = "HOTEL";
	
	
	public static void main(String[] args) throws SQLException {
		Menu  menu = new Menu();
		
		System.out.println("Welcome to Champs Hotels!");
		menu.mainMenu();
	}
	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() {
		
		Connection conn = null;

		try {

			Properties connectionProps = new Properties();
			connectionProps.put("user", this.userName);
			connectionProps.put("password", this.password);
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
					connectionProps);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

}