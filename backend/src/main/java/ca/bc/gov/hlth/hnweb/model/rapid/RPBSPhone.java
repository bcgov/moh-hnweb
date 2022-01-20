package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPhone {
	public static final int SEGMENT_LENGTH = 30;

	/** 1 PhoneType String No 0...10 */
	private String phoneType;
	/** 2 PhonePurpose String No 0...10 */
	private String phonePurpose;
	/** 3 PhoneAreaCode String No 0...3 */
	private String phoneAreaCode;
	/** 4 PhoneNumber String Yes 0...7 */
	private String phoneNumber;

	public RPBSPhone() {
		super();
	}
	
	public RPBSPhone(String message) {
		phoneType = StringUtils.substring(message, 0, 10);
		phonePurpose = StringUtils.substring(message, 10, 20);
		phoneAreaCode = StringUtils.substring(message, 20, 23);
		phoneNumber = StringUtils.substring(message, 23, SEGMENT_LENGTH);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phoneType, 10));
		sb.append(StringUtils.rightPad(phonePurpose, 10));
		sb.append(StringUtils.rightPad(phoneAreaCode, 3));
		sb.append(StringUtils.rightPad(phoneNumber, 7));
		return sb.toString();
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhonePurpose() {
		return phonePurpose;
	}

	public void setPhonePurpose(String phonePurpose) {
		this.phonePurpose = phonePurpose;
	}

	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
