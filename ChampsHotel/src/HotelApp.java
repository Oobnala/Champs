import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Connect to HOTEL database, MySQL
 * Menu object created in main to start console application
 * Make sure password is correct for local MySQL database.
 */
public class HotelApp {

	private final String userName = "root";
	// Use your password for mySQL
	private final String password = "";
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "HOTEL";
	
	
	public static void main(String[] args) throws SQLException {
		Menu  menu = new Menu();
		
		System.out.println("Welcome to Champs Hotels!");
		menu.mainMenu();
	}
	
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