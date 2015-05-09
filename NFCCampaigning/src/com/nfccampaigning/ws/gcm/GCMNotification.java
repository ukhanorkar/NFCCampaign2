package com.nfccampaigning.ws.gcm;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Path;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.nfccampaigning.model.Offer;
import com.nfccampaigning.model.User;
import com.nfccampaigning.util.DatabaseConnectionVendor;

@Path("/GCMNotification")
public class GCMNotification{
	private static final long serialVersionUID = 1L;

	// Put your Google API Server Key here
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDA5dlLInMWVsJEUTIHV0u7maB82MCsZbU";
	static final String MESSAGE_KEY = "message";	
	private final String GET_OFFER_DETAILS_QUERY = "select u.user_id, u.registration_number, off.* " +
													" from user u, offer off, user_vendor_history uvh" +
													" where off.vendor_location_id = uvh.vendor_location_id" +
													" and uvh.user_id = u.user_id" +
													" and off.offer_date = ? order by u.user_id DESC";
	private PreparedStatement preparedStatement;
	private Connection connection;
	
	/**
	 * @return
	 */
	public HashMap<User, ArrayList<Offer>> getOfferDetails(){

		HashMap<User, ArrayList<Offer>> offerMap = new HashMap<User, ArrayList<Offer>>();
		try {
			connection = DatabaseConnectionVendor.getConnection();
			preparedStatement = connection.prepareStatement(GET_OFFER_DETAILS_QUERY);
			Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
			preparedStatement.setDate(1, currentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.first();
			User user = new User();
			ArrayList<Offer> offers = new ArrayList<Offer>();
			Offer offer = getOfferBean(resultSet);
			user.setUserId(resultSet.getLong("user_id"));
			user.setRegistrationNumber(resultSet.getString("registration_number"));			
			Long currentUserId = resultSet.getLong("user_id");
			offerMap.put(user, offers);
			Long newUserId;
			while (resultSet.next()) {
				newUserId = resultSet.getLong("user_id");
				if(newUserId == currentUserId){
					offer = getOfferBean(resultSet);
					offers.add(offer);
				}
				else{
					currentUserId = newUserId;
					user = new User();
					user.setUserId(resultSet.getLong("user_id"));
					user.setRegistrationNumber(resultSet.getString("registration_number"));	
					offers = new ArrayList<Offer>();
					offer = getOfferBean(resultSet);
					offers.add(offer);
					offerMap.put(user, offers);
				}
			}
			return offerMap;
		} catch (SQLException e) {
			e.printStackTrace();
			return offerMap;
		}		
	}
	
	/**
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private Offer getOfferBean(ResultSet resultSet) throws SQLException{
		Offer offer = new Offer();
		offer.setOfferId(resultSet.getLong("offer_Id"));
		offer.setOfferDetails(resultSet.getString("offer_details"));
		offer.setOfferDate(resultSet.getDate("offer_date"));
		offer.setVendorLocationId(resultSet.getLong("vendor_location_id"));
		return offer;
		
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendNotification(){
		/*HashMap<User,ArrayList<Offer>> offerDetails = getOfferDetails();
		Iterator it = offerDetails.entrySet().iterator();
		while (it.hasNext()) {
			 Map.Entry data = (Map.Entry)it.next();
			 User user = (User) data.getKey();
			 ArrayList<Offer> offers = (ArrayList<Offer>) data.getValue();
			 Sender sender = new Sender(GOOGLE_SERVER_KEY);
			 for (Offer offer : offers) {
				 String offerMessage = offer.getOfferDetails();
				 String regId = user.getRegistrationNumber();
				 Message message = new Message.Builder().timeToLive(30)
						.delayWhileIdle(true).addData(MESSAGE_KEY, offerMessage).build();
				 try {
					sender.send(message, regId, 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}*/
		System.out.println("Test DATA");
	}
}