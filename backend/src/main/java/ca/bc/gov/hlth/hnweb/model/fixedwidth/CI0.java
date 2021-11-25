package ca.bc.gov.hlth.hnweb.model.fixedwidth;

import org.apache.commons.lang3.StringUtils;

public class CI0 {
	/** 1	PHN	String	No	0...10	.. */
	private String phn = "";
	/** 2	GroupNumber	String	No	0...7	..*/
	private String groupNumber = "";
	/** 3	EmployeeNumber	String	No	0...9	..*/
	private String employeeNumber = "";
	/** 4	DepartmentNumber	String	No	0...6	..	 	 */
	private String departmentNumber = "";
	/** 5	HomeAddress	RPBSAddress	No	0...106	..	 	 */
	private String homeAddress = "";
	/** 6	MailAddress	RPBSAddress	No	0...106	..	 	 */
	private String mailAddress = "";
	/** 7	Phone	RPBSPhone	No	0...30	0..2	 	 */
	private String phone = "";
	/** 8	BeneficiaryList	RPBSBeneficiary	No	0...124	0..20*/
	private String beneficiaryList = "";
	/** 9	Filler	String	No	0...Infinite	..	 */
	
	public CI0() {
		super();
	}
	
	public CI0(String message) {
		super();
		this.phn = StringUtils.substring(message, 0, 10);
		this.groupNumber = StringUtils.substring(message, 10, 17);
		this.employeeNumber = StringUtils.substring(message, 17, 26);
		this.departmentNumber = StringUtils.substring(message, 26, 32);
		this.homeAddress = StringUtils.substring(message, 32, 138);
		this.mailAddress = StringUtils.substring(message, 138, 244);
		// XXX (weskubo-cgi) This code does not handle repeating groups
		this.phone = StringUtils.substring(message, 244, 304);
		// XXX (weskubo-cgi) This code does not handle repeating groups
		this.beneficiaryList = StringUtils.substring(message, 72, 80);
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 8));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		
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

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBeneficiaryList() {
		return beneficiaryList;
	}

	public void setBeneficiaryList(String beneficiaryList) {
		this.beneficiaryList = beneficiaryList;
	}
	
	
	
}
