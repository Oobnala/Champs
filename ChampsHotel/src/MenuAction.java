import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class MenuAction {
	
	private HotelApp app;
	private Menu menu;
	private Connection conn;
	private Scanner sc;
	
	public MenuAction() {
		app = new HotelApp();
		menu = new Menu();
		sc = new Scanner(System.in);
		conn = app.getConnection();
	}
	
	/**
	 * LOGIN for users or admin.
	 * Users or admin login with their email or password.
	 * Functional Requirement 1
	 */
	public void Login() {
			
		try {
			String fullName = "";
			String role = "";
			int uID = 0;
			System.out.println("Enter your email");
			String email = sc.nextLine();
			
			System.out.println("Enter your password");
			String password = sc.nextLine();
			
			// Requirement 1
			String credentials = "SELECT count(*) FROM Users " + 
								 "WHERE email = ? AND password = ?";
			
			PreparedStatement stmt = conn.prepareStatement(credentials);
			stmt.setString(1, email);
			stmt.setString(2, password);
			
			ResultSet result = stmt.executeQuery();
			result.next();
			
			// check if inputted email is in Users
			String findName = "SELECT * FROM Users " +
							  "WHERE email = ?";
			
			PreparedStatement stmt2 = conn.prepareStatement(findName);
			stmt2.setString(1, email);
			
			ResultSet result2 = stmt2.executeQuery();
			
			// Get uID, name, and role from ResultSet after executing query
			while(result2.next()) {
				uID = result2.getInt("uID");
				fullName = result2.getString("fullName");
				role = result2.getString("role");
			}

			// Check if user or admin is logging in
			if(result.getInt(1) == 1 && role.equals("USER")) {
				System.out.println();
				menu.userMenu(fullName, uID);
			} else if (result.getInt(1) == 1 && role.equals("ADMIN")) {
				System.out.println();
				menu.adminMenu(fullName, uID);
			} else {
				System.out.println("Incorrect information.");
				System.out.println();
				menu.mainMenu();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * Register a user
	 * Users enter full name, email, phone number, and pass
	 * Functional Requirement 16
	 */
	public void Register() {
		
		try {
			boolean passwordCheck = true;
			
			System.out.println("Enter your full name");		
			String fullName = sc.nextLine();
			
			System.out.println("Enter your email");		
			String email = sc.nextLine();
			
			System.out.println("Enter your phone number");		
			String phoneNumber = sc.nextLine();
			
			System.out.println("Enter a password");	
			String password1 = sc.nextLine();
			
			String role = "USER";
			
			while(passwordCheck) {

				
				System.out.println("Re-enter your password");
				String password2 = sc.nextLine();
				
				// check if re-entered password is equal
				if (password1.equals(password2)) {
					passwordCheck = false;
					break;
				} else {
					System.out.println("Please try again");
				}
				
			}
			// Requirement 16
			String userDetails = "INSERT INTO Users(fullName, email, phone, role, password) " + 
						  		 "VALUES ('"+fullName+"', '"+email+"', '"+phoneNumber+"', '"+role+"', '"+password1+"' )";
			
			Statement st = conn.createStatement();
			st.executeUpdate(userDetails);
			System.out.println("Registration Complete! Select an option");
			System.out.println();
			this.menu.mainMenu();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Make a reservation based capacity.
	 * Displays all reservation information based on the capacity entered.
	 * Users enter a roomID they want to reserve.
	 * Calculates the total price based on number of nights.
	 * Presents all information to the user after a completed reservation.
	 * 
	 * @param name of users
	 * @param uID of users
	 */
	public void makeRes(String name, int uID) {
		System.out.println("You have selected to make a reservation!");
		System.out.println("Select an option to select room type: ");
		System.out.println("[1]: SINGLE [2]: DOUBLE [3]: Return to User Menu");
		
		String roomType = "";
		int numOfBeds = 1;
		int priceOfRoom = 0;
		
		
		try {
			while (sc.hasNext()) {
				String userInput = sc.nextLine();
				if (userInput.equals("1")) {
					roomType = "SINGLE";
					break;
				} 
				else if(userInput.equals("2")) {
					roomType = "DOUBLE";
					System.out.println("How many beds would you like: ");
					System.out.println("[1]: One bed [2]: 2 beds");
					String userInput2 = sc.nextLine();
					if (userInput2.equals("1")) {
						numOfBeds = 1;				
					}
					else if (userInput2.equals("2")){
						numOfBeds = 2;
					}
					break;
				}
				else if(userInput.equals("3")) {
					System.out.println("Returning to User Menu.");
					menu.userMenu(name, uID);
				}
			}
			
			String chooseRoom = "SELECT * FROM Rooms JOIN Amenities using(roomID) " + 
								 "WHERE roomType = ? AND numOfBeds = ?";
			
			PreparedStatement stmt1 = conn.prepareStatement(chooseRoom);
			stmt1.setString(1, roomType);
			stmt1.setInt(2, numOfBeds);
			
			ResultSet rs1 = stmt1.executeQuery();
			
			
			while(rs1.next()) {
				System.out.println("----------------");
				System.out.println("Room ID: " + rs1.getString("roomID"));
				System.out.println("Room type: " + rs1.getString("roomType"));
				System.out.println("Room number: " + rs1.getString("roomNumber"));
				System.out.println("Number of beds: " + rs1.getString("numOfBeds"));
				System.out.println("Guest capacity: " + rs1.getString("capacity"));
				System.out.println("Room price per night: " + rs1.getString("price"));
				System.out.println("Internet: "  + rs1.getBoolean("internet"));
				System.out.println("Room service: "  + rs1.getBoolean("roomService"));
				System.out.println("Television: "  + rs1.getBoolean("television"));
				System.out.println("----------------");
			}
			
			System.out.println("Please enter the roomID you would like to reserve.");
			int roomID = Integer.parseInt(sc.nextLine());
			
			String getPrice = "SELECT price FROM Rooms " + 
								 "WHERE roomID = ?";
			
			PreparedStatement stmt2 = conn.prepareStatement(getPrice);
			stmt2.setInt(1, roomID);
			
			ResultSet rs2 = stmt2.executeQuery();
			
			while(rs2.next()) {
				priceOfRoom = rs2.getInt("price");
			}
			
			System.out.println("Please enter a checkin date (YYYY-MM-DD): ");
			String checkin = sc.nextLine();
			System.out.println("Please enter a checkout date (YYYY-MM-DD): ");
			String checkout = sc.nextLine();
			
			
			System.out.println("Would you like to confirm this reservation?");
			System.out.println("[1]: Yes [2]: No");
			String confirmation = sc.nextLine();
			if(confirmation.equals("1")) {
				int numberOfNights = numberOfNights(checkin, checkout);
				int totalPrice = numberOfNights * priceOfRoom;
				
				String reservation = "INSERT INTO Reservations(roomID, uID, totalPrice, checkin, checkout) " + 
				  		 "VALUES ('"+roomID+"', '"+uID+"', '"+totalPrice+"', '"+checkin+"', '"+checkout+"' )";
				Statement st = conn.createStatement();
				st.executeUpdate(reservation);
				System.out.println("Reservation Complete! Here is your reservation info: ");
				System.out.println("Number of nights: " + numberOfNights);
				System.out.println("Total Price: $" + totalPrice);
			}
			else if (confirmation.equals("2")) {
				System.out.println("Returning to User Menu.");
				menu.userMenu(name, uID);
			}
			
		} catch (Exception e) {
			if (e instanceof SQLException) {
				System.out.println("Error: " + e.getMessage());
			} else {
				e.printStackTrace();
			}
		} finally {
			System.out.println("Returning to User Menu.");
			menu.userMenu(name, uID);
		}
		
		
	}
	
	/**
	 * Calculates the number of nights a user will stay at the hotel.
	 * 
	 * @param checkin date
	 * @param checkout date
	 * @return
	 */
	public int numberOfNights(String checkin, String checkout) {
		String[] checkinDate = checkin.split("-");
		String[] checkoutDate = checkout.split("-");
		
		int numberOfNights = 0;
		
		int checkinMonth = Integer.parseInt(checkinDate[1]);
		int checkinDay = Integer.parseInt(checkinDate[2]);
		int checkinYear = Integer.parseInt(checkinDate[0]);
		
		int checkoutMonth = Integer.parseInt(checkoutDate[1]);
		int checkoutDay = Integer.parseInt(checkoutDate[2]);
		int checkoutYear = Integer.parseInt(checkoutDate[0]);
		
		Calendar date1 = new GregorianCalendar();
		Calendar date2 = new GregorianCalendar();
		
		date1.set(checkinYear, checkinMonth, checkinDay);
		date2.set(checkoutYear, checkoutMonth, checkoutDay);
		
		Date inDate = date1.getTime();
		Date outDate = date2.getTime();
		
		numberOfNights = (int) ((outDate.getTime() - inDate.getTime())/ (1000 * 60 * 60 * 24));
		return numberOfNights;
	}
	
	/**
	 * A user can delete their own reservation.
	 * 
	 * @param uID of User
	 */
	public void deleteRes(int uID) {
		System.out.println("You have selected to cancel a reservation!");
		System.out.println("Enter the reservation ID to cancel your reservation:");
		
		try {
			while (sc.hasNext()) {
				int resID = sc.nextInt();
				
				String cancelRes = "DELETE FROM reservations " + "WHERE resID = ? AND uID = ?";
				PreparedStatement ps = conn.prepareStatement(cancelRes);
				ps.setInt(1, resID);
				ps.setInt(2, uID);
				
				int delete = ps.executeUpdate();
				if (delete == 0) {
					System.out.println("Invalid reservation ID, please enter again.");
				} else {
					System.out.println("Successfully cancel the reservation!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A function where users can view all their reservations
	 * Executes a query to access all reservations
	 * 
	 * @param name
	 * @param uID
	 */
	public void viewRes(String name, int uID) {
		System.out.println("You reservations are:");
		
		try {
			String view = "SELECT * FROM reservations " + "WHERE uID = " + uID;
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery(view);
			while (rs.next()) {
				System.out.println("-----------------------------");
				System.out.println("Reservation ID: " + rs.getInt("resID"));
				System.out.println("Room ID: " + rs.getInt("roomID"));
				System.out.println("Total Price: " + rs.getInt("totalPrice"));
				System.out.println("Check in Date: " + rs.getString("checkin"));
				System.out.println("Check out Date: " + rs.getString("checkout"));
			}
			System.out.println();
			
			System.out.println("Would you like to:");
			System.out.println("[1] View total price of all reservations [2] Return to User Menu");
			
			while (sc.hasNext()) {
				String userInput = sc.nextLine();
				if (userInput.equals("1")) {
					calculateTotal(name, uID);
					break;
				} 
				else if(userInput.equals("2")) {
					System.out.println("Returning to User Menu.");
					menu.userMenu(name, uID);
				}
			}
			
			menu.userMenu(name, uID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the total price of all reservations a user has.
	 * 
	 * @param name
	 * @param uID
	 */
	public void calculateTotal(String name, int uID) {

		try {
			String sum = "SELECT SUM(totalPrice) sum FROM Reservations " +
					"WHERE uID = " + uID;

			Statement st;
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sum);
			
			System.out.println("The total amount you owe for your reservation(s) is:");
			while(rs.next()) {
				System.out.println("$" + rs.getInt("sum"));
			}
			System.out.println();
			viewRes(name, uID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	/**
	 * Search by capacity function.
	 * User selects a capacity they want
	 * which will display the correlated room.
	 * 
	 * @param name 
	 * @param uID
	 */
	public void search(String name, int uID) {
		System.out.println("-------");
		System.out.println("SEARCH");
		System.out.println("-------");
		System.out.println();
		System.out.println("Please select a room capacity to search all rooms available");
		System.out.println("[1]: One person [2]: Two people [3]: All [4]: Return to User Menu");
		
		int capacity = 0;
	
		try {
			while (sc.hasNext()) {
				String userInput = sc.nextLine();
				if (userInput.equals("1")) {
					capacity = 1;
					break;
				} 
				else if(userInput.equals("2")) {
					capacity = 2;
					break;
				}
				else if(userInput.equals("3")) {
					searchAll(name, uID);
					break;
				}
				else if(userInput.equals("4")) {
					System.out.println("Returning to User Menu.");
					menu.userMenu(name, uID);
				}
			}
			
			String search = "SELECT * FROM Rooms JOIN Amenities using (roomID) " +
							"WHERE capacity = " + capacity;
			
			Statement st = conn.createStatement();		
			ResultSet rs = st.executeQuery(search);
			
			viewRooms(rs);
			menu.sortMenu(search, name, uID);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Search all rooms that has amenities.
	 * 
	 * @param name of user
	 * @param uID of user
	 */
	public void searchAll(String name, int uID) {
		try {
			String search = "SELECT * FROM Rooms JOIN Amenities using (roomID)";
			Statement stat = conn.createStatement();
			
			ResultSet rs = stat.executeQuery(search);
			
			viewRooms(rs);
			
			menu.sortMenu(search, name, uID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Display all room information
	 * when executed.
	 * 
	 * @param rs from query
	 * @throws SQLException
	 */
	public void viewRooms(ResultSet rs) throws SQLException {
		while(rs.next()) {
			System.out.println("----------------");
			System.out.println("Room ID: " + rs.getString("roomID"));
			System.out.println("Room type: " + rs.getString("roomType"));
			System.out.println("Room number: " + rs.getString("roomNumber"));
			System.out.println("Number of beds: " + rs.getString("numOfBeds"));
			System.out.println("Guest capacity: " + rs.getString("capacity"));
			System.out.println("Room price per night: " + rs.getString("price"));
			System.out.println("Internet: "  + rs.getBoolean("internet"));
			System.out.println("Room service: "  + rs.getBoolean("roomService"));
			System.out.println("Television: "  + rs.getBoolean("television"));
		}
	}
	
	/**
	 * Sort by price function that executes query either ASC or DESC
	 * 
	 * @param searchQuery original search query
	 * @param sortQuery original search query with ORDER BY string
	 * @param name of user
	 * @param uID of user
	 * @throws SQLException
	 */
	public void sort(String searchQuery, String sortQuery, String name, int uID) throws SQLException {

		Statement stat = conn.createStatement();
		
		ResultSet rs = stat.executeQuery(sortQuery);
		viewRooms(rs);
		menu.sortMenu(searchQuery, name, uID);
	}
}
