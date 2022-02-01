package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class WB0 {

	/** 1	SubscriberPHN	String	No	0...10	.. */
	private String phn;
	/** 2	GroupNumber	String	No	0...7	.. */
	private String groupNumber;
	/** 3 BenficiaryPHN	String	No	0...10	.. */
	private String benficiaryPHN;
	/** 4	CoverageEffectiveDate	RPBSDate	No	0...10	.. */
	private String coverageEffectiveDate;
	/** 5	RelationshipCode	String	No	0...1 .. */
	private String relationshipCode;
	//Modifications to this messaging protocol added the following values 
	/** 6 StudentEndDate **/
	private String studentEndDate;	
	/** 7 StudentFlag Valid values are 'Y', 'N' **/
	private String studentFlag;
	
	public WB0() {
		super();
	}
	
	public WB0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		benficiaryPHN = StringUtils.substring(message, 17, 27);
		coverageEffectiveDate = StringUtils.substring(message, 27, 37);
		// S or D
		relationshipCode = StringUtils.substring(message, 37, 38);
		// Y or N
		studentFlag = StringUtils.substring(message, 38, 39);
		studentEndDate = StringUtils.substring(message, 39, 49);
	}

	public String serialize() {
		// Serialize is only used when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(benficiaryPHN, 10));
		sb.append(StringUtils.rightPad(coverageEffectiveDate, 10));
		sb.append(StringUtils.rightPad(relationshipCode, 1));
		sb.append(StringUtils.rightPad(studentFlag, 1));
		sb.append(StringUtils.rightPad(studentEndDate, 7));

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

	public String getBenficiaryPHN() {
		return benficiaryPHN;
	}

	public void setBenficiaryPHN(String benficiaryPHN) {
		this.benficiaryPHN = benficiaryPHN;
	}

	public String getCoverageEffectiveDate() {
		return coverageEffectiveDate;
	}

	public void setCoverageEffectiveDate(String coverageEffectiveDate) {
		this.coverageEffectiveDate = coverageEffectiveDate;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public String getStudentEndDate() {
		return studentEndDate;
	}

	public void setStudentEndDate(String studentEndDate) {
		this.studentEndDate = studentEndDate;
	}

	public String getStudentFlag() {
		return studentFlag;
	}

	public void setStudentFlag(String studentFlag) {
		this.studentFlag = studentFlag;
	}

}
