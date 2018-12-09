import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Administrator {
	private HotelApp app;
	private Scanner sc;
	private Menu menu;
	private Connection conn;
	
	public Administrator() {
		app = new HotelApp();
		sc = new Scanner(System.in);
		conn = app.getConnection();
		menu = new Menu();
	}

	public void viewAllRes() {
		System.out.println("List of reservations:");
		
		try {
			String view = "SELECT * FROM reservations";
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
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void deleteRes() {
		System.out.println("Enter the reservation ID you wish to cancel:");
		
		try {
				int resID = sc.nextInt();
				
				String cancelRes = "DELETE FROM reservations " + "WHERE resID = ?";
				PreparedStatement ps = conn.prepareStatement(cancelRes);
				ps.setInt(1, resID);
				
				int delete = ps.executeUpdate();
				if (delete == 0) {
					System.out.println("Invalid reservation ID, please enter again.");
				} else {
					System.out.println("Successfully cancelled the reservation!");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void modifyRes() {
		System.out.println("Enter the reservation ID you wish to modify:");		
		String resId = sc.next();
		System.out.println("Select an option to select room type: ");
		System.out.println("[1]: SINGLE [2]: DOUBLE [3]: Return to Admin Menu");
		String userInput = sc.nextLine();
		String roomType = "";
		int numOfBeds = 1;
		int priceOfRoom = 0;
		try {
			if (userInput.equals("1")) {
				roomType = "SINGLE";
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
			}
			else if(userInput.equals("3")) {
				System.out.println("Returning to Admin Menu.");
				return;
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

			int numberOfNights = numberOfNights(checkin, checkout);
			int totalPrice = numberOfNights * priceOfRoom;
			System.out.println("Number of nights: " + numberOfNights);
			System.out.println("Total Price: $" + totalPrice);
			
			System.out.println("Would you like to confirm this reservation?");
			System.out.println("[1]: Yes [2]: No");
			String confirmation = sc.nextLine();
			if(confirmation.equals("1")) {
				String reservation = "UPDATE Reservations SET roomID="+roomID+" , totalPrice=" + totalPrice + " , checkin='"+ checkin+"', checkout='" + 
						checkout+"' WHERE resID = "+resId;
				Statement st = conn.createStatement();
				st.executeUpdate(reservation);
				System.out.println("Reservation Complete!");
			}
			else if (confirmation.equals("2")) {
				System.out.println("Returning to Admin Menu.");
				return;
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void changeUserPwd() {
		System.out.println("Enter the email ID of the user whose password you wish to change:");
		String email = sc.next();
		try {
				System.out.println("Enter the new password");
				String newPwd = sc.next();
				
				String cancelRes = "UPDATE Users SET password = ? WHERE email = ?";
				PreparedStatement ps = conn.prepareStatement(cancelRes);
				ps.setString(1, newPwd);
				ps.setString(2, email);
				
				int delete = ps.executeUpdate();
				if (delete == 0) {
					System.out.println("Invalid email id.");
				} else {
					System.out.println("Successfully updated the user password!");
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewAllInfo() {
		try {
			
			String view = "SELECT * FROM Reservations, Rooms, Amenities WHERE Reservations.roomID = Rooms.roomID AND Rooms.roomID = Amenities.roomID;";
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery(view);
			while (rs.next()) {
				System.out.println("-----------------------------");
				System.out.println("Reservation ID: " + rs.getInt("resID"));
				System.out.println("Room ID: " + rs.getInt("Reservations.roomID"));
				System.out.println("Room Type: " + rs.getString("roomType"));
				System.out.println("Room Number: " + rs.getInt("roomNumber"));
				System.out.println("Number of Beds: " + rs.getInt("numOfBeds"));
				System.out.println("Capacity: " + rs.getInt("capacity"));
				System.out.println("Number of Beds: " + rs.getInt("numOfBeds"));
				System.out.println("Price: " + rs.getInt("price"));
				System.out.println("Internet: " + rs.getBoolean("internet"));
				System.out.println("Room Service: " + rs.getBoolean("roomService"));
				System.out.println("Television: " + rs.getBoolean("television"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void archiveRes(String name, int uID) {
		try {
			Statement st = conn.createStatement();
			
			System.out.println("Please enter a cutoff date to archive reservations");
			String date = sc.next();
			
			String callProc = "CALL archiveRes(" + "'" + java.sql.Date.valueOf(date) + "'" + ")";
			st.executeQuery(callProc);	
			System.out.println("Reservation(s) archived!");
			menu.archiveMenu(name, uID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void viewArchive(String name, int uID) {
		Statement st;
		try {
			st = conn.createStatement();
			System.out.println("Here are all reservations that are archived");
			
			String archiveView = "SELECT * FROM Archive";
			ResultSet rs2 = st.executeQuery(archiveView);
			
			while(rs2.next()) {
				System.out.println("----------------------------");
				System.out.println("Reservation ID: " + rs2.getInt("resID"));
				System.out.println("Room ID: " + rs2.getInt("roomID"));
				System.out.println("User ID: " + rs2.getInt("uID"));
				System.out.println("Total Price: " + rs2.getInt("totalPrice"));
				System.out.println("Checkin: " + rs2.getDate("checkIn"));
				System.out.println("Checkout: " + rs2.getDate("checkOut"));
				System.out.println("Updated At: " + rs2.getDate("updatedAt"));
			}
			menu.archiveMenu(name, uID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public void viewRegulars() {
		try {
		Statement st = conn.createStatement();
		System.out.println("The regulars are: ");
		String listRegulars = "SELECT fullName, count(*) AS Reservations FROM Users NATURAL JOIN Reservations " + "GROUP BY uID HAVING COUNT(*) > 1";
		
		System.out.println("User Name" + "\t" + "Reservations");
		System.out.println("------------------------------------");
		ResultSet rs = st.executeQuery(listRegulars);
		while (rs.next()) {
			System.out.println(rs.getString("fullName") + "\t" + rs.getInt("Reservations"));
		}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
