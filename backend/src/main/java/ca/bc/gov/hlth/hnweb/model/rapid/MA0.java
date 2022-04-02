package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

/**
 * @author anumeha.srivastava
 *
 */
public class MA0 {
	/** 1 PHN String Yes 0...10 1..1 */
	private String phn;
	/** 2 GroupNumber String Yes 0...7	1..1 */
	private String groupNumber;
	/** 3 HomeAddress RPBSAddress No 0...106 .. */
	private RPBSAddress homeAddress;
	/** 4 MailAddress RPBSAddress No 0...106 .. */
	private RPBSAddress mailAddress;

	public MA0() {
		super();
	}

	public MA0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		homeAddress = new RPBSAddress(StringUtils.substring(message, 17, 123));
		mailAddress = new RPBSAddress(StringUtils.substring(message, 123, 229));
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(homeAddress.serialize());
		sb.append(mailAddress.serialize());

		return sb.toString();
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public RPBSAddress getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(RPBSAddress homeAddress) {
		this.homeAddress = homeAddress;
	}

	public RPBSAddress getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(RPBSAddress mailAddress) {
		this.mailAddress = mailAddress;
	}

}
