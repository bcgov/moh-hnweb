package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class AG0 {

	/** 1 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 2 PHN String No 0...10 .. */
	private String phn;
	/** 3 ExistingCancellationDate RPBSDate No 0...10 .. */
	private String existingCancellationDate;
	/** 4 NewCancellationDate RPBSDate No 0...10 .. */
	private String newCancellationDate;

	public AG0() {
		super();
	}

	public AG0(String message) {
		super();
		groupNumber = StringUtils.substring(message, 0, 7);
		phn = StringUtils.substring(message, 7, 17);
		existingCancellationDate = StringUtils.substring(message, 17, 27);
		newCancellationDate = StringUtils.substring(message, 27, 37);

	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first two fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(existingCancellationDate, 10));
		sb.append(StringUtils.rightPad(newCancellationDate, 10));

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

	public String getExistingCancellationDate() {
		return existingCancellationDate;
	}

	public void setExistingCancellationDate(String existingCancellationDate) {
		this.existingCancellationDate = existingCancellationDate;
	}

	public String getNewCancellationDate() {
		return newCancellationDate;
	}

	public void setNewCancellationDate(String newCancellationDate) {
		this.newCancellationDate = newCancellationDate;
	}

}
