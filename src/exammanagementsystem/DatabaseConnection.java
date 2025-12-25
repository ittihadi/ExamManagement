package exammanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ittihadi
 */

public class DatabaseConnection {
	private static final String url = "jdbc:mysql://localhost/exam_management";
	private static final String user = "root";
	private static final String pass = "";

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
