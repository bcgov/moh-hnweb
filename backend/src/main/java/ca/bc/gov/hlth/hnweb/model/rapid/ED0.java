package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class ED0 {

	/** 1 PHN String No 0...10 .. */
	private String phn;
	/** 2 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 3	DepartmentNumber	String	No	0...6 */
	private String departmentNumber;

	public ED0() {
		super();
	}

	public ED0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		departmentNumber = StringUtils.substring(message, 17, 23);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first two fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(departmentNumber, 6));

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

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

}
