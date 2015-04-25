package nfc.bits.com.model;

import java.util.List;

/**
 * @author unnati_khanorkar
 *
 */
public class Vendor {

	private long vendorId;
	private String vendorName;
	private long contactNumber;
	private String email;
	private List<VendorLocation> vendorLocations;
	/**
	 * @return the vendorId
	 */
	public long getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the contactNumber
	 */
	public long getContactNumber() {
		return contactNumber;
	}
	/**
	 * @param contactNumber the contactNumber to set
	 */
	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the vendorLocations
	 */
	public List<VendorLocation> getVendorLocations() {
		return vendorLocations;
	}
	/**
	 * @param vendorLocations the vendorLocations to set
	 */
	public void setVendorLocations(List<VendorLocation> vendorLocations) {
		this.vendorLocations = vendorLocations;
	}

}
