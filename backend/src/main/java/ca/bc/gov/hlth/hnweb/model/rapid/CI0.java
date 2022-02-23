package ca.bc.gov.hlth.hnweb.model.rapid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CI0 {

	/** 1 PHN String No 0...10 .. */
	private String phn;
	/** 2 GroupNumber String No 0...7 */
	private String groupNumber;
	/** 3 EmployeeNumber String No 0...9 */
	private String employeeNumber;
	/** 4 DepartmentNumber String No 0...6 .. */
	private String departmentNumber;
	/** 5 HomeAddress RPBSAddress No 0...106 */
	private RPBSAddress homeAddress;
	/** 6 MailAddress RPBSAddress No 0...106 */
	private RPBSAddress mailAddress;
	/** 7 Phone RPBSPhone No 0...30 0..2 */
	private RPBSPhone phone;
	/** 8 BeneficiaryList RPBSBeneficiary No 0...124 0..20 */
	private List<RPBSBeneficiary> beneficiary = new ArrayList<RPBSBeneficiary>();
	/** 9 Filler String No 0...Infinite */
	private String filler;

	public CI0() {
		super();
	}

	public CI0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		employeeNumber = StringUtils.substring(message, 17, 26);
		departmentNumber = StringUtils.substring(message, 26, 32);
		homeAddress = new RPBSAddress(StringUtils.substring(message, 32, 138));
		mailAddress = new RPBSAddress(StringUtils.substring(message, 138, 244));
		phone = new RPBSPhone(StringUtils.substring(message, 244, 304));
		String beneficiariesStr = StringUtils.substring(message, 304);
		if (StringUtils.isNotBlank(beneficiariesStr)) {
			int count = 0;
			String ciBeneficiary = StringUtils.substring(beneficiariesStr, 0, RPBSBeneficiary.SEGMENT_LENGTH);
			while (StringUtils.isNotBlank(ciBeneficiary)) {
				RPBSBeneficiary rPBSBeneficiary = new RPBSBeneficiary(ciBeneficiary);
				beneficiary.add(rPBSBeneficiary);
				count++;
				ciBeneficiary = StringUtils.substring(beneficiariesStr, RPBSBeneficiary.SEGMENT_LENGTH * count,
						RPBSBeneficiary.SEGMENT_LENGTH * (count + 1));
			}
		}
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first two fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
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

	public RPBSPhone getPhone() {
		return phone;
	}

	public void setPhone(RPBSPhone phone) {
		this.phone = phone;
	}

	public List<RPBSBeneficiary> getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(List<RPBSBeneficiary> beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

}
