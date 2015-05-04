package nfc.bits.com.model;

import java.io.Serializable;

/**
 * @author unnati_khanorkar
 *
 */
public class VendorLocation implements Serializable{

	private long vendorLocationId;
	private String street;
	private String city;
	private String state;
	private long pincode;
	private String country;
	private double latitude;
	private double longitude;
	
	/**
	 * @return the vendorLocationId
	 */
	public long getVendorLocationId() {
		return vendorLocationId;
	}
	/**
	 * @param vendorLocationId the vendorLocationId to set
	 */
	public void setVendorLocationId(long vendorLocationId) {
		this.vendorLocationId = vendorLocationId;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the pincode
	 */
	public long getPincode() {
		return pincode;
	}
	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(long pincode) {
		this.pincode = pincode;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

    @Override
    public String toString() {
        return this.street + " " + this.city + " " + this.state + " " + this.pincode;
    }
}
