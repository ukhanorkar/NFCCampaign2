/**
 * 
 */
package com.nfccampaigning.util;

/**
 * @author unnati_khanorkar
 *
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionVendor {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost/nfccampaigndb";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	private static Connection connection;

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static Connection getConnection() throws SQLException{
		if(connection == null){
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			return connection;
		}
		return connection;
	}
}