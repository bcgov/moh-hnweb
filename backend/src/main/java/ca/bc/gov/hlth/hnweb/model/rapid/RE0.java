package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RE0 {

	/** 1 SubscriberPHN String No 0...10 .. */
	private String subscriberPHN;
	/** 2 GroupNumber String No 0...7 .. */
	private String groupNumber;
	/** 3 DependentPHN String No 0...10 .. */
	private String dependentPHN;
	/** 4 DependentDOB	RPBSDate	No	0...10	.. */
	private String dependentDOB;	
	/** 5 CanadianStudent String No 0...1 .. (Valid values are 'Y', 'N') **/
	private String canadianStudent;
	/** 6 StudentEndDate String No 0...10 .. */
	private String studentEndDate;

	public RE0() {
		super();
	}

	public RE0(String message) {
		super();
		subscriberPHN = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		dependentPHN = StringUtils.substring(message, 17, 27);
		dependentDOB = StringUtils.substring(message, 27, 34);
		canadianStudent = StringUtils.substring(message, 34, 35);
		studentEndDate = StringUtils.substring(message, 35, 42);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(subscriberPHN, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(dependentPHN, 10));
		sb.append(StringUtils.rightPad(dependentDOB, 10));
		sb.append(StringUtils.rightPad(canadianStudent, 1));
		sb.append(StringUtils.rightPad(studentEndDate, 7));

		return sb.toString();
	}

	public String getSubscriberPHN() {
		return subscriberPHN;
	}

	public void setSubscriberPHN(String subscriberPHN) {
		this.subscriberPHN = subscriberPHN;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getDependentPHN() {
		return dependentPHN;
	}

	public void setDependentPHN(String dependentPHN) {
		this.dependentPHN = dependentPHN;
	}

	public String getDependentDOB() {
		return dependentDOB;
	}

	public void setDependentDOB(String dependentDOB) {
		this.dependentDOB = dependentDOB;
	}

	public String getCanadianStudent() {
		return canadianStudent;
	}

	public void setCanadianStudent(String canadianStudent) {
		this.canadianStudent = canadianStudent;
	}

	public String getStudentEndDate() {
		return studentEndDate;
	}

	public void setStudentEndDate(String studentEndDate) {
		this.studentEndDate = studentEndDate;
	}
	
	

}
