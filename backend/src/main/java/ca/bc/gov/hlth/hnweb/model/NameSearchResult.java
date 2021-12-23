package ca.bc.gov.hlth.hnweb.model;

public class NameSearchResult {

	private String phn;
	
	private String givenName;

	private String secondName;

	private String surname;

	private String dateOfBirth;

	private String gender;

	private String address1;
	
	private String address2;
	
	private String address3;

	private String city;

	private String province;

	private String postalCode;
	
	private String assigningAuthority;
	
	private String identifierTypeCode;
	
	private String nameTypeCode;
	
	private String mailingAddress1;

	private String mailingAddress2;

	private String mailingAddress3;

	private String mailingAddressCity;

	private String mailingAddressProvince;

	private String mailingAddressCountry;

	private String mailingAddressPostalCode;
	
	private double score;

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address) {
		address1 = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String addressLine2) {
		this.address2 = addressLine2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String addressLine3) {
		this.address3 = addressLine3;
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public NameSearchResult() {
	}

	public String getNameTypeCode() {
		return nameTypeCode;
	}

	public void setNameTypeCode(String currentRecord) {
		this.nameTypeCode = currentRecord;
	}

	protected String getMailingAddress1() {
		return mailingAddress1;
	}

	protected void setMailingAddress1(String mailingAddress1) {
		this.mailingAddress1 = mailingAddress1;
	}

	protected String getMailingAddress2() {
		return mailingAddress2;
	}

	protected void setMailingAddress2(String mailingAddress2) {
		this.mailingAddress2 = mailingAddress2;
	}

	protected String getMailingAddress3() {
		return mailingAddress3;
	}

	protected void setMailingAddress3(String mailingAddress3) {
		this.mailingAddress3 = mailingAddress3;
	}

	protected String getMailingAddressCity() {
		return mailingAddressCity;
	}

	protected void setMailingAddressCity(String mailingAddressCity) {
		this.mailingAddressCity = mailingAddressCity;
	}

	protected String getMailingAddressProvince() {
		return mailingAddressProvince;
	}

	protected void setMailingAddressProvince(String mailingAddressProvince) {
		this.mailingAddressProvince = mailingAddressProvince;
	}

	protected String getMailingAddressCountry() {
		return mailingAddressCountry;
	}

	protected void setMailingAddressCountry(String mailingAddressCountry) {
		this.mailingAddressCountry = mailingAddressCountry;
	}

	protected String getMailingAddressPostalCode() {
		return mailingAddressPostalCode;
	}

	protected void setMailingAddressPostalCode(String mailingAddressPostalCode) {
		this.mailingAddressPostalCode = mailingAddressPostalCode;
	}

	public double getScore() {
		return score;
	}

	public String getAssigningAuthority() {
		return assigningAuthority;
	}

	public void setAssigningAuthority(String assigningAuthority) {
		this.assigningAuthority = assigningAuthority;
	}

	public String getIdentifierTypeCode() {
		return identifierTypeCode;
	}

	public void setIdentifierTypeCode(String identifierTypeCode) {
		this.identifierTypeCode = identifierTypeCode;
	}

	public void setScore(double score) {
		this.score = score;
	}

}