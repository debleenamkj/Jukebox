import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {

	@SuppressWarnings("resource")
	public void menuDisplay() throws UserInvalidException {
		try {
			Scanner scanObj = new Scanner(System.in);
			Connection conn = DBConn.getConnection();
			System.out.println();
			System.out.print("\t\tWelcome to Jukebox");
			System.out.println();

			do {
				System.out.println("\n\t\tDo you want to?\n\t\t\t1.Sign Up\n\t\t\t2.Sign In");
				int choice = scanObj.nextInt();
				if (choice == 1) {
					createUser(conn);
					continue;
				} else if (choice == 2) {
					System.out.print("\n\t\tEnter your username: ");
					String username = scanObj.next();
					System.out.print("\n\t\tEnter your password: ");
					String password = scanObj.next();
					try{
						if (checkUsernamePassword(conn, username, password)) {
						System.out.print("\n\t\t*****************************************");
						System.out.print("\n\t\t*\tWelcome to Jukebox... " + username + "\t*");
						System.out.print("\n\t\t*\tEntering the world of music\t*");
						System.out.print("\n\t\t*****************************************");

						Song song = new Song();

						song.songMenu(username);
						break;
						
						} else {
							throw new UserInvalidException("\n\t\tIncorrect username or password.");
						}
					}catch (UserInvalidException ue) {
						System.out.print("\n\t\tIncorrect username or password.\n");
						continue;
					}
					
				} else {
					System.out.print("\n\t\tInvalid choice. Please select from 1 or 2.");
					continue;
				}
			} while (true);

		}  catch (InputMismatchException ie) {
			System.out.print("\n\t\tYour username and password do not match");
		} catch (NoSuchElementException ne) {
			System.out.print("\n\t\tJukeBox abruptly stopped...");
		} catch (SQLException sq) {
			System.out.print("\n\t\tCannot connect to Jukebox");
		} catch (Exception e) {
			System.out.print("\n\t\tJukeBox abruptly stopped working");
		}
	}

	public void callPlaylist(Connection conn, String username) {
		Scanner scanObj = new Scanner(System.in);
		Song song=new Song();
		Playlist playlist = new Playlist();
		System.out.println("\n\t\t\t1. Create Playlist\n\t\t\t2. View Playlist");
		int choice1 = scanObj.nextInt();
		switch (choice1) {
		case 1:
			playlist.createUserPlaylist(conn, username);
			break;
		case 2:
			playlist.viewSongsInPlaylist(conn, username);
			song.songMenu(username);
			break;
		default:
			System.out.print("\n\t\tInvalid choice");
		}
		scanObj.close();
	}

	public void createUser(Connection conn) {
		Scanner scanObj = new Scanner(System.in);
		try {
			PreparedStatement psmt = conn.prepareStatement("insert into users values(?,?,?,?,?)");

			System.out.print("\n\t\tEnter Username: ");
			String username = scanObj.next();
			psmt.setString(1, username);
			System.out.print("\n\t\tEnter Password: ");
			String password = scanObj.next();
			psmt.setString(2, password);
			System.out.print("\n\t\tEnter Age: ");
			int age = scanObj.nextInt();
			psmt.setInt(3, age);
			System.out.print("\n\t\tEnter Gender: ");
			String gender = scanObj.next();
			psmt.setString(4, gender);
			System.out.print("\n\t\tEnter Mobile Number: ");
			String mobileno = scanObj.next();
			if (Pattern.matches("[0-9]{10}", mobileno)) {
				psmt.setString(5, mobileno);
				int rowEffected = psmt.executeUpdate();
				psmt.close();
				if (rowEffected > 0) {
					System.out.print(
							"\n\n\t\tUser created. Please remember your username and password to login when you come back again. Thank You.\n");
				} else {
					System.out.print("\n\n\t\tError occured while creating user");
				}
			} else {
				System.out.println("\n\t\tIncorrect way of entering mobile number");
			}
			scanObj.close();
		} catch (NoSuchElementException ne) {
			System.out.print("\n\t\tJukeBox abruptly stopped...");
		} catch (Exception e) {
			System.out.print("\n\n\t\tUsername already exists. Please try again\n");
		}
	}

	public boolean checkUsernamePassword(Connection conn, String username, String password) throws SQLException {
		boolean checking = true;
		Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = statement.executeQuery("select * from users");

		while (rs.next()) {
			if (rs.getString(1).equals(username)) {
				if (rs.getString(2).equals(password)) {
					return checking = true;
				}
			} else {
				checking = false;
			}
		}
		return checking;
	}
}
