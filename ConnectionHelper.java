package lms.leaveManagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionHelper {
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		ResourceBundle rb = ResourceBundle.getBundle("emp");
		System.out.println("bundel is :"+ rb);
		String driver = rb.getString("driver");
		String url = rb.getString("url");
		String user = rb.getString("user");
		String password = rb.getString("password");
		
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, user, password);
		
//		System.out.println("connection : "+connection);
		return connection;
	}
}
