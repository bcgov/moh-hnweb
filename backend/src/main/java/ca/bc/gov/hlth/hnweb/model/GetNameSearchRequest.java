package ca.bc.gov.hlth.hnweb.model;

public class GetNameSearchRequest extends BaseResponse {

	private String givenName;

	private String secondName;

	private String surname;

	private String dateOfBirth;
	
	private String gender;

	
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

	@Override
	public String toString() {
		return "NameSearch Response: [givenName=" + givenName + ", secondName=" + secondName
				+ ", surname=" + surname + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + "]";
	}
	
}