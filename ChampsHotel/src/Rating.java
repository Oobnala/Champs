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
	
	public void review(String name, int uID) {
		System.out.println("You have selected to leave a rating and comment");
		System.out.println("Please rating our hotel in a scale of 1 to 5(1 is lowest and 5 is highest");
		
		int score = 0;
		String comment = "";
		try {
			while (sc.hasNext()) {
				score = Integer.parseInt(sc.nextLine());
				if (score < 1 || score >5) {
					System.out.println("Invalid score, please try again");
				}
				else {
					System.out.println("Please leave your comment: ");
					comment += sc.nextLine();
					break;
				}
			}
			
			String rate = "INSERT INTO Rating(score, review, uID)" + " VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(rate);
			ps.setInt(1, score);
			ps.setString(2, comment);
			ps.setInt(3, uID);
			
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
