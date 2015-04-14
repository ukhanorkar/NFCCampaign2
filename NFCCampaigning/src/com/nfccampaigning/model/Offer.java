package com.nfccampaigning.model;

import java.util.Date;

/**
 * @author unnati_khanorkar
 *
 */
public class Offer {

	private long offerId;
	private String offerDetails;
	private Date offerDate;
	private long vendorLocationId;
	/**
	 * @return the offerId
	 */
	public long getOfferId() {
		return offerId;
	}
	/**
	 * @param offerId the offerId to set
	 */
	public void setOfferId(long offerId) {
		this.offerId = offerId;
	}
	/**
	 * @return the offerDetails
	 */
	public String getOfferDetails() {
		return offerDetails;
	}
	/**
	 * @param offerDetails the offerDetails to set
	 */
	public void setOfferDetails(String offerDetails) {
		this.offerDetails = offerDetails;
	}
	/**
	 * @return the offerDate
	 */
	public Date getOfferDate() {
		return offerDate;
	}
	/**
	 * @param offerDate the offerDate to set
	 */
	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}
	/**
	 * @return
	 */
	public long getVendorLocationId() {
		return vendorLocationId;
	}
	/**
	 * @param vendorLocationId
	 */
	public void setVendorLocationId(long vendorLocationId) {
		this.vendorLocationId = vendorLocationId;
	}
	
	
	
}
