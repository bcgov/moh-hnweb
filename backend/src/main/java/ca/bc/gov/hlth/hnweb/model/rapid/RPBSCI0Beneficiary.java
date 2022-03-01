package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSCI0Beneficiary {
	public static final int SEGMENT_LENGTH = 124;

	/** 1 PHN String No 0...10 .. */
	private String phn;
	/** 2 FamilyName String No 0...35 */
	private String familyName;
	/** 3 FirstName String No 0...15 */
	private String firstName;
	/** 4 SecondName String No 0...15 */
	private String secondName;
	/** 5 ThirdName String No 0...15 */
	private String thirdName;
	/** 6 Birth Date No 0...10 .. */
	private String birthDate;
	/** 7 Gender 0...1 .. */
	private String gender;
	/** 8 EffectiveDate RPBSDate No 0...10 */
	private String effectiveDate;
	/** 9 CancelDate RPBSDate No 0...10 */
	private String cancelDate;
	/** 10 CancelReason String No 0...1 */
	private String cancelReason;
	/** 11 StudentStatus String No 0...1 */
	private String studentStatus;
	/** 12 RelationshipCode String No 0...1 */
	private String relationshipCode;

	public RPBSCI0Beneficiary(String message) {
		this.phn = StringUtils.substring(message, 0, 10);
		this.familyName = StringUtils.substring(message, 10, 45);
		this.firstName = StringUtils.substring(message, 45, 60);
		this.secondName = StringUtils.substring(message, 60, 75);
		this.thirdName = StringUtils.substring(message, 75, 90);
		this.birthDate = StringUtils.substring(message, 90, 100);
		this.gender = StringUtils.substring(message, 100, 101);
		this.effectiveDate = StringUtils.substring(message, 101, 111);
		this.cancelDate = StringUtils.substring(message, 111, 121);
		this.cancelReason = StringUtils.substring(message, 121, 122);
		this.studentStatus = StringUtils.substring(message, 122, 123);
		this.relationshipCode = StringUtils.substring(message, 123, 124);
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

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}
}
