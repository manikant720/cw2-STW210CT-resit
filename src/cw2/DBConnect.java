package cw2;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

	public Connection connection() {
		Connection conn = null;
		
		try {
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/lottery", "root", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
}
