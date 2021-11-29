package ca.bc.gov.hlth.hnweb.model;

import java.time.LocalDate;

/**
 * Request message for API call to enroll a subscriber 
 *
 */
public class EnrollSubscriberRequest {
	
	private String phn;
	
	private String fullName;
		
	private String gender;
	
	private String groupnumber;
	
	private String immigrationCode;
	
	private String groupMemberNumber;
	
	private LocalDate visaIssueDate;
	
	private String departmentNumber;
	
	private LocalDate visaExpiryDate;
	
	private LocalDate residenceDate;
	
	private LocalDate coverageEffectiveDate;
	
	private String areaCode;
	
	private String telephone;
	
	private LocalDate coverageCancellationDate;
	
	private String address;
	
	private String city;
	
	private String province;
	
	private String country;
	
	private String postalCode;
	
	private String mailingAddress;
	
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String surname) {
		this.fullName = surname;
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
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
