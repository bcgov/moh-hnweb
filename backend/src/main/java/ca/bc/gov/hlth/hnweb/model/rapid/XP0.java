package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class XP0 {

	/** 1	GroupNumber	String	No	0...7	.. */
	private String groupNumber;
	/** 2	EmployeeNumber	String	No	0...9	.. */
	private String employeeNumber;
	/** 3	DepartmentNumber	String	No	0...6	.. */
	private String departmentNumber;
	/** 4	EffectiveDate	RPBSDate	Yes	0...10	.. */
	private String effectiveDate;
	/** 5	HomeAddress	RPBSAddress	No	0...106	.. */
	private RPBSAddress homeAddress;
	/** 6	MailAddress	RPBSAddress	No	0...106	.. */
	private RPBSAddress mailAddress;
	/** 7	Phone0	RPBSPhone	No	0...30	.. */
	private RPBSPhone phone0;
	/** 8	Phone1	RPBSPhone	No	0...30	.. */
	private RPBSPhone phone1;
	/** 9	PHN	String	No	0...10	.. */
	private String phn;
	/** 10	SpousePHN	String	No	0...10	.. */
	private String spousePhn;
	/** 11	DependentPHN0	String	No	0...10	.. */	 	 
	private String dependentPhn0;
	/** 12	DependentPHN1	String	No	0...10	.. */	 
	private String dependentPhn1;
	/** 13	DependentPHN2	String	No	0...10	.. */	 	 
	private String dependentPhn2;
	/** 14	DependentPHN3	String	No	0...10	.. */	 
	private String dependentPhn3;
	/** 15	DependentPHN4	String	No	0...10	.. */	 
	private String dependentPhn4;
	/** 16	DependentPHN5	String	No	0...10	.. */	 
	private String dependentPhn5;
	/** 17	DependentPHN6	String	No	0...10	.. */
	private String dependentPhn6;
	/** 18	DependentPHN7	String	No	0...10	.. */
	private String dependentPhn7;
	/** 19	PHNInError	String	No	0...10 */
	private String phnInError;

	public XP0() {
		super();
	}

	public XP0(String message) {
		super();
		groupNumber = StringUtils.substring(message, 0, 7);
		employeeNumber = StringUtils.substring(message, 7, 16);
		departmentNumber = StringUtils.substring(message, 16, 22);
		effectiveDate = StringUtils.substring(message, 22, 32);
		homeAddress = new RPBSAddress(StringUtils.substring(message, 22, 128));
		mailAddress = new RPBSAddress(StringUtils.substring(message, 128, 134));
		phone0 = new RPBSPhone(StringUtils.substring(message, 134, 164));
		phone1 = new RPBSPhone(StringUtils.substring(message, 164, 194));
		phn = StringUtils.substring(message, 194, 204);
		spousePhn = StringUtils.substring(message, 194, 204);
		dependentPhn0 = StringUtils.substring(message, 194, 204);
		dependentPhn1 = StringUtils.substring(message, 204, 214);
		dependentPhn2 = StringUtils.substring(message, 214, 224);
		dependentPhn3 = StringUtils.substring(message, 224, 234);
		dependentPhn4 = StringUtils.substring(message, 234, 244);
		dependentPhn5 = StringUtils.substring(message, 244, 254);
		dependentPhn6 = StringUtils.substring(message, 254, 264);
		dependentPhn7 = StringUtils.substring(message, 264, 274);
		phnInError = StringUtils.substring(message, 274, 284);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(employeeNumber, 9));
		sb.append(StringUtils.rightPad(departmentNumber, 6));
		sb.append(StringUtils.rightPad(effectiveDate, 10));
		sb.append(homeAddress.serialize());
		sb.append(mailAddress.serialize());
		sb.append(phone0.serialize());
		sb.append(phone1.serialize());
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(spousePhn, 10));
		sb.append(StringUtils.rightPad(dependentPhn0, 10));
		sb.append(StringUtils.rightPad(dependentPhn1, 10));
		sb.append(StringUtils.rightPad(dependentPhn2, 10));
		sb.append(StringUtils.rightPad(dependentPhn3, 10));
		sb.append(StringUtils.rightPad(dependentPhn4, 10));
		sb.append(StringUtils.rightPad(dependentPhn5, 10));
		sb.append(StringUtils.rightPad(dependentPhn6, 10));
		sb.append(StringUtils.rightPad(dependentPhn7, 10));
		return sb.toString();
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getSpousePhn() {
		return spousePhn;
	}

	public void setSpousePhn(String spousePhn) {
		this.spousePhn = spousePhn;
	}

	public String getDependentPhn0() {
		return dependentPhn0;
	}

	public void setDependentPhn0(String dependentPhn0) {
		this.dependentPhn0 = dependentPhn0;
	}

	public String getDependentPhn1() {
		return dependentPhn1;
	}

	public void setDependentPhn1(String dependentPhn1) {
		this.dependentPhn1 = dependentPhn1;
	}

	public String getDependentPhn2() {
		return dependentPhn2;
	}

	public void setDependentPhn2(String dependentPhn2) {
		this.dependentPhn2 = dependentPhn2;
	}

	public String getDependentPhn3() {
		return dependentPhn3;
	}

	public void setDependentPhn3(String dependentPhn3) {
		this.dependentPhn3 = dependentPhn3;
	}

	public String getDependentPhn4() {
		return dependentPhn4;
	}

	public void setDependentPhn4(String dependentPhn4) {
		this.dependentPhn4 = dependentPhn4;
	}

	public String getDependentPhn5() {
		return dependentPhn5;
	}

	public void setDependentPhn5(String dependentPhn5) {
		this.dependentPhn5 = dependentPhn5;
	}

	public String getDependentPhn6() {
		return dependentPhn6;
	}

	public void setDependentPhn6(String dependentPhn6) {
		this.dependentPhn6 = dependentPhn6;
	}

	public String getDependentPhn7() {
		return dependentPhn7;
	}

	public void setDependentPhn7(String dependentPhn7) {
		this.dependentPhn7 = dependentPhn7;
	}

	public String getPhnInError() {
		return phnInError;
	}

	public void setPhnInError(String phnInError) {
		this.phnInError = phnInError;
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
