package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RH0 {

	/** 1 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 2 PHN String No 0...10 .. */
	private String phn;
	/** 3 CancelDateToBeRemoved RPBSDate No 0...10 .. */
	private String cancelDateToBeRemoved;

	public RH0() {
		super();
	}

	public RH0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);		
		cancelDateToBeRemoved = StringUtils.substring(message, 17, 27);

	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first three fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(cancelDateToBeRemoved, 10));
	
		return sb.toString();
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getCancelDateToBeRemoved() {
		return cancelDateToBeRemoved;
	}

	public void setCancelDateToBeRemoved(String newCoverageEffectiveDate) {
		this.cancelDateToBeRemoved = newCoverageEffectiveDate;
	}

}
