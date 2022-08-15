package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class AJ0 {

	/** 1 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 2 PHN String No 0...10 .. */
	private String phn;
	/** 3 ExistingEffectiveDate RPBSDate No 0...10 .. */
	private String existingEffectiveDate;
	/** 4 NewEffectiveDate RPBSDate No 0...10 .. */
	private String newEffectiveDate;

	public AJ0() {
		super();
	}

	public AJ0(String message) {
		super();
		groupNumber = StringUtils.substring(message, 0, 7);
		phn = StringUtils.substring(message, 7, 17);
		existingEffectiveDate = StringUtils.substring(message, 17, 27);
		newEffectiveDate = StringUtils.substring(message, 27, 37);

	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first two fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(existingEffectiveDate, 10));
		sb.append(StringUtils.rightPad(newEffectiveDate, 10));

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

	public String getExistingEffectiveDate() {
		return existingEffectiveDate;
	}

	public void setExistingEffectiveDate(String existingEffectiveDate) {
		this.existingEffectiveDate = existingEffectiveDate;
	}

	public String getNewEffectiveDate() {
		return newEffectiveDate;
	}

	public void setNewEffectiveDate(String newEffectiveDate) {
		this.newEffectiveDate = newEffectiveDate;
	}

}
