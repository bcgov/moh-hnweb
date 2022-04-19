package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

/**
 * @author anumeha.srivastava
 *
 */
public class EP0 {
	/** 1 PHN String Yes 0...10 1..1 */
	private String phn;
	/** 2 GroupNumber String Yes 0...7	1..1 */
	private String groupNumber;
	/** 3 Phone0 RPBSPhone No 0...30 .. */
	private RPBSPhone phone0;
	/** 4 Phone1 RPBSPhone No 0...30 .. */
	private RPBSPhone phone1;

	public EP0() {
		super();
	}

	public EP0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		phone0 = new RPBSPhone(StringUtils.substring(message, 17, 47));
		phone1 = new RPBSPhone(StringUtils.substring(message, 47, 77));
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(phone0.serialize());
		sb.append(phone1.serialize());

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

	public RPBSPhone getPhone0() {
		return phone0;
	}

	public void setPhone0(RPBSPhone phone0) {
		this.phone0 = phone0;
	}

	public RPBSPhone getPhone1() {
		return phone1;
	}

	public void setPhone1(RPBSPhone phone1) {
		this.phone1 = phone1;
	}

}
