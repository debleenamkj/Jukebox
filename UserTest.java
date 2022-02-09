
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

	User user;
	
	@BeforeEach
	void setUp() {
	user=new User();
	}
	
	@AfterEach
	void tearDown() {
	user=null;
	}
	
	
	@Test
	public void checkUserTestFalse() throws Exception {
		Connection conn = DBConn.getConnection();
		assertEquals(false,user.checkUsernamePassword(conn, "user1", "pass"));
		assertEquals(false,user.checkUsernamePassword(conn, "user", "aaa"));
		assertEquals(false,user.checkUsernamePassword(conn, "user2", "incorrrect"));
		assertEquals(false,user.checkUsernamePassword(conn, "Ashish", "notpresent"));
		assertEquals(false,user.checkUsernamePassword(conn, "Alokita", "nono"));
	}

	@Test
	public void checkUserTestTrue() throws Exception {
		Connection conn = DBConn.getConnection();
		assertEquals(true,user.checkUsernamePassword(conn, "user1", "pass1"));
		assertEquals(true,user.checkUsernamePassword(conn, "user", "pass"));
		assertEquals(true,user.checkUsernamePassword(conn, "j", "j"));
		assertEquals(true,user.checkUsernamePassword(conn, "M", "M"));
	}
	
	
}
