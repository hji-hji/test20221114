package dbconn;

import java.sql.Connection;
import java.sql.DriverManager;

public class Dbconn {
	
	private String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private String user="system";
	private String password="1234";

	public Connection getConnect() { 
		Connection conn= null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {			
			e.printStackTrace();
		}	
		return conn;
	}
	
	
	
	
	
}
