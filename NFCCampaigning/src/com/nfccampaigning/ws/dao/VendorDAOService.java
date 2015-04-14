/**
 * 
 */
package com.nfccampaigning.ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.nfccampaigning.model.Vendor;
import com.nfccampaigning.model.VendorLocation;
import com.nfccampaigning.util.DatabaseConnectionVendor;

/**
 * @author unnati_khanorkar
 *
 */
@Path("/vendorservice")
public class VendorDAOService {
	
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private Connection connection;
	private final String SAVE_USER_VENDOR_HISTORY_QUERY = "INSERT INTO user_vendor_history (user_id,vendor_location_id,isSubscribed) VALUES(?,?,1)";
	private final String GET_USER_VENDOR_HISTORY_QUERY = "select v.name, vl.* " +
			"from vendor v, vendor_location vl, user_vendor_history uvh " +
			"where uvh.user_id = ? " +
			"and uvh.vendor_location_id = vl.vendor_location_id " +
			"and vl.vendor_id = v.vendor_id and uvh.isSubscribed = 1 order by vendor_id;";
	private final String UNSUBSCRIBE_VENDOR_QUERY = "UPDATE user_vendor_history uvh" +
			"SET uvh.isSubscribed = 0" +
			"WHERE uvh.user_id = ?" +
			"and (select 1 from vendor_location vl " +
			"where uvh.vendor_location_id = vl.vendor_location_id" +
			"and vl.vendor_id = ?)";
	private final String SUBSCRIBE_VENDOR_QUERY = "UPDATE user_vendor_history uvh" +
			"SET uvh.isSubscribed = 1" +
			"WHERE uvh.user_id = ?" +
			"and (select 1 from vendor_location vl " +
			"where uvh.vendor_location_id = vl.vendor_location_id" +
			"and vl.vendor_id = ?)";
	Logger logger = Logger.getLogger("VendorDAOService");
	
	@POST
	@Path("/saveUserVendorHistory/{userId}/{vendorLocationId}")
	public void saveUserVendorHistory(@PathParam("userId") Long userId, @PathParam("vendorLocationId") Long vendorLocationId ){
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(SAVE_USER_VENDOR_HISTORY_QUERY);
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, vendorLocationId);
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@GET
	@Path("/getUserVendorHistory/{userId}")
	@Produces("application/json")
	public List<Vendor> getUserVendorHistory(@PathParam("userId") Long userId){
		List<Vendor> vendors = new ArrayList<Vendor>();
		List<VendorLocation> vendorLocations = new ArrayList<VendorLocation>();
		Long currentVendorId = null;
		Long newVendorId = null;
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(GET_USER_VENDOR_HISTORY_QUERY);
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();	
			resultSet.first();
			Vendor vendor = new Vendor();
			vendor.setVendorName(resultSet.getString("name"));
			vendor.setVendorId(resultSet.getLong("vendor_id"));
			VendorLocation vendorLocation =  getVendorLocation(resultSet);
			currentVendorId = vendor.getVendorId();
			vendorLocations.add(vendorLocation);		
			vendor.setVendorLocations(vendorLocations);
			vendors.add(vendor);
			while (resultSet.next()) {				
				newVendorId = resultSet.getLong("vendor_id");
				if(currentVendorId == newVendorId){
					vendorLocation =  getVendorLocation(resultSet);
					vendorLocations.add(vendorLocation);
				}
				else{
					currentVendorId = newVendorId;
					vendor = new Vendor();
					vendors.add(vendor);
					vendor.setVendorName(resultSet.getString("name"));
					vendor.setVendorId(resultSet.getLong("vendor_id"));
					vendorLocations = new ArrayList<VendorLocation>();
					vendor.setVendorLocations(vendorLocations);
					vendorLocations.add(getVendorLocation(resultSet));
				}				
			}
			return vendors;
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return vendors;
	}
	
	private VendorLocation getVendorLocation(ResultSet resultSet){
		VendorLocation vendorLocation =  new VendorLocation();
		try {
			vendorLocation.setCity(resultSet.getString("city"));
			vendorLocation.setStreet(resultSet.getString("street"));
			vendorLocation.setState(resultSet.getString("state"));
			vendorLocation.setPincode(resultSet.getInt("pincode"));
			vendorLocation.setVendorLocationId(resultSet.getInt("vendor_location_id"));
			vendorLocation.setCountry(resultSet.getString("country"));
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		return vendorLocation;
	}

	@Path("/unsubscribevendor/{userId}/{vendorId}")
	public void unSubscribeVendor(@PathParam("userId") Long userId, @PathParam("vendorId") Long vendorId){
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(UNSUBSCRIBE_VENDOR_QUERY);
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, vendorId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Path("/subscribevendor/{userId}/{vendorId}")
	public void subscribeVendor(@PathParam("userId") Long userId, @PathParam("vendorId") Long vendorId){
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(SUBSCRIBE_VENDOR_QUERY);
			preparedStatement.setLong(1, userId);
			preparedStatement.setLong(2, vendorId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
