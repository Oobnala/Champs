import java.sql.SQLException;
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
		System.out.println("Welcome, " + name +"!");
		System.out.println("Would you like to: ");
		System.out.println("[1]: Make a reservation [2]: Delete a Reservation [3]: View your reservation(s) [4]: Exit");
		
		while (sc.hasNext()) {
			String userInput = sc.nextLine();
			if (userInput.equals("1")) {
				action.makeRes(name, uID);
			} 
			else if(userInput.equals("2")) {
				action.deleteRes(uID);
			}
			else if(userInput.equals("3")) {
				action.viewRes(uID);
			}
			else if(userInput.equals("4")) {
				System.out.println("Exiting Application");
				System.exit(0);
			}
		}
	}
	
}
