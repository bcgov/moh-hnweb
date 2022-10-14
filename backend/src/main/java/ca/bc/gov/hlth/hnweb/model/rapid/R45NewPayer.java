package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class R45NewPayer {
	/** 0	SubscriberPHN	String	No	0...10	.. */
	private String phn;
	/** 1	SpousePHN	String	No	0...10	.. */
	private String spousePhn;	
	/** 2	DependentPHN1	String	No	0...10	.. */	 
	private String dependentPhn1;
	/** 3	DependentPHN2	String	No	0...10	.. */	 	 
	private String dependentPhn2;
	/** 4	DependentPHN3	String	No	0...10	.. */	 
	private String dependentPhn3;
	/** 5	DependentPHN4	String	No	0...10	.. */	 
	private String dependentPhn4;
	/** 6	DependentPHN5	String	No	0...10	.. */	 
	private String dependentPhn5;
	/** 7	DependentPHN6	String	No	0...10	.. */
	private String dependentPhn6;
	/** 8	DependentPHN7	String	No	0...10	.. */
	private String dependentPhn7;
	/** 9 EmployeePHN String No 0...10 .. */
	private String employeePhn;
	/** 10 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/**	11 EmployeeNumber	String	No	0...9	.. */
	private String employeeNumber;
	/** 12	DepartmentNumber	String	No	0...6 */
	private String departmentNumber;
	/** 13	HomeAddress	RPBSAddress	No	0...106	.. */
	private RPBSAddress homeAddress;
	/** 14	MailAddress	RPBSAddress	No	0...106	.. */
	private RPBSAddress mailAddress;
	/** 15	Phone0	RPBSPhone	No	0...30	.. */
	private RPBSPhone phone0;
	/** 16	Phone1	RPBSPhone	No	0...30	.. */
	private RPBSPhone phone1;
		
	public R45NewPayer() {
		super();
	}

	public R45NewPayer(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		spousePhn = StringUtils.substring(message, 10, 20);
		dependentPhn1 = StringUtils.substring(message, 20, 30);
		dependentPhn2 = StringUtils.substring(message, 30, 40);
		dependentPhn3 = StringUtils.substring(message, 40, 50);
		dependentPhn4 = StringUtils.substring(message, 50, 60);
		dependentPhn5 = StringUtils.substring(message, 60, 70);
		dependentPhn6 = StringUtils.substring(message, 70, 80);
		dependentPhn7 = StringUtils.substring(message, 80, 90);
		employeePhn = StringUtils.substring(message, 90, 100);
		groupNumber = StringUtils.substring(message, 100, 107);
		employeeNumber = StringUtils.substring(message, 107, 116);
		departmentNumber = StringUtils.substring(message, 116, 122);
		homeAddress = new RPBSAddress(StringUtils.substring(message, 122, 228));
		mailAddress = new RPBSAddress(StringUtils.substring(message, 228, 334));
		phone0 = new RPBSPhone(StringUtils.substring(message, 334, 364));
		phone1 = new RPBSPhone(StringUtils.substring(message, 364, 394));			

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
	public String getEmployeePhn() {
		return employeePhn;
	}

	public void setEmployeePhn(String employeePhn) {
		this.employeePhn = employeePhn;
	}

	public String getDependentPhn7() {
		return dependentPhn7;
	}
	public void setDependentPhn7(String dependentPhn7) {
		this.dependentPhn7 = dependentPhn7;
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
