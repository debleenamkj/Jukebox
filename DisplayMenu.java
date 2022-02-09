import java.util.NoSuchElementException;

public class DisplayMenu {
	
	public void display()  {
		
		try {	
			
		System.out.println("\t\t*****************************************************************");
		System.out.println("\t\t*\tWelcome to Jukebox: Surround your soul with Beats\t*");
		System.out.println("\t\t*****************************************************************");
		
		User user=new User();
		user.menuDisplay();
		
		System.out.print("\n\t\tWe hope you enjoyed your time here. Hope to see you soon.");
		
		}
		catch(NoSuchElementException ne) {
			System.out.print("\n\t\tJukeBox abruptly stopped");
		}
		catch (Exception e) {
			
			System.out.print("\n\t\tJukeBox abruptly stopped working");
		}
	}
}
