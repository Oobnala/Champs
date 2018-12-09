import java.sql.*;
import java.util.Scanner;

public class Rating {
	private HotelApp app;
	private Scanner sc;
	private Connection conn;
	private Menu menu;
	
	public Rating() {
		app = new HotelApp();
		sc = new Scanner(System.in);
		conn = app.getConnection();
		menu = new Menu();
	}
	
	public void viewHighest() {
		System.out.println("The most positive rating(s) are: ");
		try {
			String showRating = "SELECT * FROM Rating " + "WHERE score >= ALL" + "(SELECT score from Rating)";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(showRating);
			
			while(rs.next()) {
				System.out.println("---------------");
				System.out.println("Score: " + rs.getInt("Score"));
				System.out.println("Review: " + rs.getString("review"));
				System.out.println("User ID: " + rs.getInt("uID"));
				System.out.println("Reservation ID: " + rs.getInt("resID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void review(String name, int uID) {
		System.out.println("You have selected to leave a rating and comment");
		
		try {
			int score = 0;
			String comment = "";
			System.out.println("Please enter your reservation ID to verify:");
			int resID = Integer.parseInt(sc.nextLine());
			System.out.println("Please rate our hotel in a scale of 1 to 5(1 is lowest and 5 is highest");
			while (sc.hasNext()) {
				score = Integer.parseInt(sc.nextLine());
				if (score < 1 || score >5) {
					System.out.println("Invalid score, please try again");
				}
				else {
					break;
				}
			}
			System.out.println("Please leave your comment: ");
			comment = sc.nextLine();
			
			
			String rate = "INSERT INTO Rating(score, review, uID, resID)" + " VALUES(?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(rate);
			ps.setInt(1, score);
			ps.setString(2, comment);
			ps.setInt(3, uID);
			ps.setInt(4, resID);
			
			int execute = ps.executeUpdate();
			if (execute == 0) {
				System.out.println("Failure updating the database, please try again");
			}
			else {
				System.out.println("Successfully submit your review! Returning to the main menu..");
			}
			menu.userMenu(name, uID);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
