package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class PL0Beneficary {
	public static final int SEGMENT_LENGTH = 101;
	
	/** 1	PHN	String	No	0...10	.. */
	private String phn;
	/**	2	FamilyName	String	No	0...35	.. */
	private String familyName;
	/** 3	FirstName	String	No	0...15	.. */
	private String firstName;
	/** 4	SecondName	String	No	0...15	.. */
	private String secondName;
	/** 5	ThirdName	String	No	0...15	.. */
	private String thirdName;
	/** 6	BirthDate	RPBSDate	No	0...10	.. */
	private String birthDate;
	/** 7	Gender	String	No	0...1 */
	private String gender;

	public PL0Beneficary(String message) {
		phn = StringUtils.substring(message, 0, 10);
		familyName = StringUtils.substring(message, 10, 45);
		firstName = StringUtils.substring(message, 45, 60);
		secondName = StringUtils.substring(message, 60, 75);
		thirdName = StringUtils.substring(message, 75, 90);
		birthDate = StringUtils.substring(message, 90, 100);
		gender = StringUtils.substring(message, 100, 101);
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
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

}
