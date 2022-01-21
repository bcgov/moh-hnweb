package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSAddress {
	public static final int SEGMENT_LENGTH = 106;

	/** 1 AddressLine1 String No 0...25 */
	private String addressLine1;
	/** 2 AddressLine2 String No 0...25 */
	private String addressLine2;
	/** 3 AddressLine3 String No 0...25 */
	private String addressLine3;
	/** 4 AddressLine4 String No 0...25 */
	private String addressLine4;
	/** 5 PostalCode String No 0...6 */
	private String postalCode;

	public RPBSAddress() {
		super();
	}

	public RPBSAddress(String message) {
		addressLine1 = StringUtils.substring(message, 0, 25);
		addressLine2 = StringUtils.substring(message, 25, 50);
		addressLine3 = StringUtils.substring(message, 50, 75);
		addressLine4 = StringUtils.substring(message, 75, 100);
		postalCode = StringUtils.substring(message, 100, SEGMENT_LENGTH);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(StringUtils.trimToEmpty(addressLine1), 25));
		sb.append(StringUtils.rightPad(StringUtils.trimToEmpty(addressLine2), 25));
		sb.append(StringUtils.rightPad(StringUtils.trimToEmpty(addressLine3), 25));
		sb.append(StringUtils.rightPad(StringUtils.trimToEmpty(addressLine4), 25));
		sb.append(StringUtils.rightPad(StringUtils.trimToEmpty(postalCode), 6));
		return sb.toString();
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
