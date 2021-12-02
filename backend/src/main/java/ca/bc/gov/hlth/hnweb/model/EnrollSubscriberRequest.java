package ca.bc.gov.hlth.hnweb.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Request message for API call to enroll a subscriber 
 *
 */
public class EnrollSubscriberRequest {
	
	private String phn;
	
	private String surname;
	
	private String firstGivenName;
	
	private String secondGivenName;
		
	private String gender;
	
	private String groupnumber;
	
	private String immigrationCode;
	
	private String groupMemberNumber;
	
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate visaIssueDate;
	
	private String departmentNumber;
	
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate visaExpiryDate;
	
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate residenceDate;
	
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate coverageEffectiveDate;
	
	private String telephone;
	
	@JsonFormat(pattern="yyyyMMdd")
	private LocalDate coverageCancellationDate;
	
	private String address1;
	
	private String address2;
	
	private String address3;
	
	private String city;
	
	private String province;
	
	private String country;
	
	private String postalCode;
	
	private String mailingAddress1;
	
	private String mailingAddress2;
	
	private String mailingAddress3;
	
	private String mailingAddressCity;
	
	private String mailingAddressProvince;
	
	private String mailingAddressCountry;
	
	private String mailingAddressPostalCode;
	
	private String priorResidenceCode;
	
	private String otherProvinceHealthcareNumber;
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGroupnumber() {
		return groupnumber;
	}

	public void setGroupnumber(String groupnumber) {
		this.groupnumber = groupnumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String familyName) {
		this.surname = familyName;
	}

	public String getFirstGivenName() {
		return firstGivenName;
	}

	public void setFirstGivenName(String firstGivenName) {
		this.firstGivenName = firstGivenName;
	}

	public String getSecondGivenName() {
		return secondGivenName;
	}

	public void setSecondGivenName(String secondGivenName) {
		this.secondGivenName = secondGivenName;
	}

	public String getImmigrationCode() {
		return immigrationCode;
	}

	public void setImmigrationCode(String immigrationCode) {
		this.immigrationCode = immigrationCode;
	}

	public String getGroupMemberNumber() {
		return groupMemberNumber;
	}

	public void setGroupMemberNumber(String groupMemberNumber) {
		this.groupMemberNumber = groupMemberNumber;
	}

	public LocalDate getVisaIssueDate() {
		return visaIssueDate;
	}

	public void setVisaIssueDate(LocalDate visaIssueDate) {
		this.visaIssueDate = visaIssueDate;
	}

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public LocalDate getVisaExpiryDate() {
		return visaExpiryDate;
	}

	public void setVisaExpiryDate(LocalDate visaExpiryDate) {
		this.visaExpiryDate = visaExpiryDate;
	}

	public LocalDate getResidenceDate() {
		return residenceDate;
	}

	public void setResidenceDate(LocalDate residenceDate) {
		this.residenceDate = residenceDate;
	}

	public LocalDate getCoverageEffectiveDate() {
		return coverageEffectiveDate;
	}

	public void setCoverageEffectiveDate(LocalDate coverageEffectiveDate) {
		this.coverageEffectiveDate = coverageEffectiveDate;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public LocalDate getCoverageCancellationDate() {
		return coverageCancellationDate;
	}

	public void setCoverageCancellationDate(LocalDate coverageCancellationDate) {
		this.coverageCancellationDate = coverageCancellationDate;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address) {
		this.address1 = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getMailingAddress2() {
		return mailingAddress2;
	}

	public void setMailingAddress2(String mailingAddress2) {
		this.mailingAddress2 = mailingAddress2;
	}

	public String getMailingAddress3() {
		return mailingAddress3;
	}

	public void setMailingAddress3(String mailingAddress3) {
		this.mailingAddress3 = mailingAddress3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getMailingAddress1() {
		return mailingAddress1;
	}

	public void setMailingAddress1(String mailingAddress) {
		this.mailingAddress1 = mailingAddress;
	}

	public String getMailingAddressCity() {
		return mailingAddressCity;
	}

	public void setMailingAddressCity(String mailingAddressCity) {
		this.mailingAddressCity = mailingAddressCity;
	}

	public String getMailingAddressProvince() {
		return mailingAddressProvince;
	}

	public void setMailingAddressProvince(String mailingAddressProvince) {
		this.mailingAddressProvince = mailingAddressProvince;
	}

	public String getMailingAddressCountry() {
		return mailingAddressCountry;
	}

	public void setMailingAddressCountry(String mailingAddressCountry) {
		this.mailingAddressCountry = mailingAddressCountry;
	}

	public String getMailingAddressPostalCode() {
		return mailingAddressPostalCode;
	}

	public void setMailingAddressPostalCode(String mailingAddressPostalCode) {
		this.mailingAddressPostalCode = mailingAddressPostalCode;
	}

	public String getPriorResidenceCode() {
		return priorResidenceCode;
	}

	public void setPriorResidenceCode(String priorResidenceCode) {
		this.priorResidenceCode = priorResidenceCode;
	}

	public String getOtherProvinceHealthcareNumber() {
		return otherProvinceHealthcareNumber;
	}

	public void setOtherProvinceHealthcareNumber(String otherProvinceHealthcareNumber) {
		this.otherProvinceHealthcareNumber = otherProvinceHealthcareNumber;
	}

}
