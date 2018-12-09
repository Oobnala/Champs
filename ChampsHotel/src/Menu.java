import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class Menu {
	
	private Scanner sc;
	
	public Menu() {
		sc = new Scanner(System.in);
	}

	public void mainMenu() {
		MenuAction action = new MenuAction();
		System.out.println("Select an option:");
		System.out.println();
		System.out.println("[1]: Login [2]: Register [3]: Exit");
		
		while (sc.hasNext()) {
			String userInput = sc.nextLine();
			if (userInput.equals("1")) {
				System.out.println("----Login----");
				System.out.println();
				action.Login();
			} 
			else if(userInput.equals("2")) {
				System.out.println("----Register----");
				System.out.println();
				action.Register();
			}
			else if(userInput.equals("3")) {
				System.out.println("Exiting Application");
				System.exit(0);
			}
		}
	}
	
	public void userMenu(String name, int uID) {
		MenuAction action = new MenuAction();
		Rating rating = new Rating();
		System.out.println("Welcome, " + name +"!");
		System.out.println("Would you like to: ");
		System.out.println("[1]: Make a reservation");
		System.out.println("[2]: Delete a Reservation");
		System.out.println("[3]: View your reservation(s)");
		System.out.println("[4]: Search");
		System.out.println("[5]: View most positive rating(s)");
		System.out.println("[6]: Leave a rating and comment");
		System.out.println("[7]: Exit");
		
		while (sc.hasNext()) {
			String userInput = sc.nextLine();
			if (userInput.equals("1")) {
				action.makeRes(name, uID);
			} 
			else if(userInput.equals("2")) {
				action.deleteRes(uID);
			}
			else if(userInput.equals("3")) {
				action.viewRes(name,uID);
			}
			else if(userInput.equals("4")) {
				action.search(name, uID);
			}
			else if(userInput.equals("5")) {
				rating.viewHighest();
			}
			else if(userInput.equals("6")) {
				rating.review(name, uID);
			}
			else if(userInput.equals("7")) {
				System.out.println("Exiting Application");
				System.exit(0);
			}
		}
	}

	public void adminMenu(String fullName, int uID) {
		System.out.println("Welcome, " + fullName +" - ADMIN!!");
		System.out.println("Would you like to: ");
		do { 
		System.out.println("[1]: View all reservation");
		System.out.println("[2]: View all made reservations by users to view all room info and amenities ");
		System.out.println("[3]: Modify Reservation");
		System.out.println("[4]: Cancel Reservation");
		System.out.println("[5]: Change Password of a user");
		System.out.println("[6]: View regulars");
		System.out.println("[7]: Archive reservations");
		System.out.println("[8]: Exit");
		
		Administrator admin = new Administrator();

		String userInput = sc.nextLine();	
		if(userInput.equals("1")){
			admin.viewAllRes();
		} else if(userInput.equals("2")) {
			admin.viewAllInfo();
		} else if(userInput.equals("3")) {
			admin.modifyRes();
		} else if(userInput.equals("4")) {
			admin.deleteRes();
		} else if(userInput.equals("5")) {
			admin.changeUserPwd();
		} else if(userInput.equals("6")) {
			admin.viewRegulars();
		} else if(userInput.equals("7")) {
			archiveMenu(fullName, uID);
		} else if(userInput.equals("8")) {
			System.out.println("Exiting Application");
			System.exit(0);
		}
			
		}while(sc.hasNext());
	}
	
	public void sortMenu(String searchQuery, String name, int uID) throws SQLException {
		MenuAction action = new MenuAction();
		System.out.println();
		System.out.println("Select an option");
		System.out.println("[1]: Sort by price high - low [2]: Sort by price low - high [3]: Return to search");
	
		String sortQuery = "";
		while (sc.hasNext()) {
			String userInput = sc.nextLine();
			if (userInput.equals("1")) {
				sortQuery = searchQuery + " ORDER BY price DESC";
				break;
			} 
			else if(userInput.equals("2")) {
				sortQuery = searchQuery + " ORDER BY price ASC";
				break;
				
			}
			else if(userInput.equals("3")) {
				action.search(name, uID);
			}
		}
		action.sort(searchQuery, sortQuery, name, uID);
	}
	
	public void archiveMenu(String name, int uID) {
		Administrator admin = new Administrator();
		System.out.println("Would you like to: ");
		System.out.println("[1] Archive reservations [2] View Archive [3] Return to Admin menu");
		String option = sc.nextLine();
		
		if (option.equals("1")) {
			admin.archiveRes(name, uID);
		} else if (option.equals("2")) {
			admin.viewArchive(name, uID);
		} else if (option.equals("3")) {
			adminMenu(name, uID);
		}
	}	
}
