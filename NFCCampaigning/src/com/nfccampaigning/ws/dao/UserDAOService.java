/**
 * 
 */
package com.nfccampaigning.ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.nfccampaigning.model.User;
import com.nfccampaigning.util.DatabaseConnectionVendor;

/**
 * @author unnati_khanorkar
 *
 */
@Path ("/userdaoservice")
public class UserDAOService {
	
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private Connection connection;
	private final String INSERT_USER_INFO = "INSERT INTO user(device_id,contact_number,email_id,registration_number) VALUES(?,?,?,?);";
	Logger logger = Logger.getLogger("VendorDAOService");
	
	@POST
	@Consumes("application/json")
	public void registerUser(User user){
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(INSERT_USER_INFO);
			preparedStatement.setString(1, user.getDeviceId());
			preparedStatement.setLong(2, user.getContactNumber());
			preparedStatement.setString(3,  user.getEmail());
			preparedStatement.setString(4,  user.getRegistrationNumber());
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}
