package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class AI0 {

	/** 1 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 2 PHN String No 0...10 .. */
	private String phn;
	/** 3 NewCoverageEffectiveDate RPBSDate No 0...10 .. */
	private String newCoverageEffectiveDate;
	/** 4 NewPayerData String No 0...394 .. */
	private R45NewPayer newPayerData;

	public AI0() {
		super();
	}

	public AI0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);		
		newCoverageEffectiveDate = StringUtils.substring(message, 17, 27);
		newPayerData = new R45NewPayer(StringUtils.substring(message, 27,421));

	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first three fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(newCoverageEffectiveDate, 10));
	
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

	public String getNewCoverageEffectiveDate() {
		return newCoverageEffectiveDate;
	}

	public void setNewCoverageEffectiveDate(String newCoverageEffectiveDate) {
		this.newCoverageEffectiveDate = newCoverageEffectiveDate;
	}

	public R45NewPayer getNewPayerData() {
		return newPayerData;
	}

	public void setNewPayerData(R45NewPayer ai0payeeData) {
		this.newPayerData = ai0payeeData;
	}

}
