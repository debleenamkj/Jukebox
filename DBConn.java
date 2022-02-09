import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
	public static Connection connection;
	
	public static Connection getConnection() throws Exception
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/jukebox","root","Password@123");
		return connection;
	}
}
