/**
 * 
 */
package com.nfccampaigning.model;

/**
 * @author unnati_khanorkar
 *
 */
public class NFCTag {

	private long tagId;
	private String tagType;
	private long tabSize;
	private long vendorLocationId;
	/**
	 * @return the tagId
	 */
	public long getTagId() {
		return tagId;
	}
	/**
	 * @param tagId the tagId to set
	 */
	public void setTagId(long tagId) {
		this.tagId = tagId;
	}
	/**
	 * @return the tagType
	 */
	public String getTagType() {
		return tagType;
	}
	/**
	 * @param tagType the tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	/**
	 * @return the tabSize
	 */
	public long getTabSize() {
		return tabSize;
	}
	/**
	 * @param tabSize the tabSize to set
	 */
	public void setTabSize(long tabSize) {
		this.tabSize = tabSize;
	}
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
	
	
}
