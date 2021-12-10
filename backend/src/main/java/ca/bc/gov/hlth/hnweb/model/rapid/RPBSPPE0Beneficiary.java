package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPPE0Beneficiary {
	public static final int SEGMENT_LENGTH = 273;

	/** 1 PHN String No 0...10 .. */
	private String phn;
	/** 2 Last Name No 0...35 .. */
	private String lastName;
	/** 3 First Name No 0...45 .. */
	private String firstName;
	/** 4 Birth Date No 0...10 .. */
	private String birthDate;
	/** 5 Gender 0...1 .. */
	private String gender;
	/** 6 String No 0...8 .. */
	private String statusCode = "";
	/** 7 String No 0...72 .. */
	private String statusText = "";//
	/** 8 Eligible No 0...1 .. */
	private String eligible;

	/** 9 Filler 3 No 0...96 .. */

	/** 10 Student No 0...11 .. */
	private String student;

	public RPBSPPE0Beneficiary(String message) {
		this.phn = StringUtils.substring(message, 0, 10);
		this.lastName = StringUtils.substring(message, 10, 45);
		this.firstName = StringUtils.substring(message, 45, 90);
		this.birthDate = StringUtils.substring(message, 90, 100);
		this.gender = StringUtils.substring(message, 100, 101);
		this.statusCode = StringUtils.substring(message, 101, 109);
		this.statusText = StringUtils.substring(message, 109, 181);
		this.eligible = StringUtils.substring(message, 181, 182);
		this.student = StringUtils.substring(message, 272, 273);
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getEligible() {
		return eligible;
	}

	public void setEligible(String eligible) {
		this.eligible = eligible;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

}
