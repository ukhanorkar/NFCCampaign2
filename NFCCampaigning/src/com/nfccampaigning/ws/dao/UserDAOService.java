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
import javax.ws.rs.Produces;

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
	private final String GET_USER  = "select * from user where device_id = ?";
	Logger logger = Logger.getLogger("UserDAOService");
	
	@POST
	@Path("/registerUser")
	@Consumes("application/json")
	@Produces("application/json")
	public String registerUser(User user){
		long userId = 0;
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(INSERT_USER_INFO);
			preparedStatement.setString(1, user.getDeviceId());
			preparedStatement.setLong(2, user.getContactNumber());
			preparedStatement.setString(3,  user.getEmail());
			preparedStatement.setString(4,  user.getRegistrationNumber());
			preparedStatement.executeUpdate();	
			userId = getUser(user.getDeviceId());
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return Long.toString(userId);
	}
	
	@POST
	@Path("/isUserRegistered")
	@Consumes("application/json")
	public boolean isUaerRegistered(User user){
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(GET_USER);
			preparedStatement.setString(1, user.getDeviceId());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return true;
			}
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	private long getUser(String deviceId){
		long userId = 0;
		try {
		connection = DatabaseConnectionVendor.getConnection();
		preparedStatement = connection.prepareStatement(GET_USER);
		preparedStatement.setString(1,deviceId);
		resultSet = preparedStatement.executeQuery();
		while(resultSet.next()){
			userId = resultSet.getLong("user_id");
		}
		
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return userId;
	}
}
