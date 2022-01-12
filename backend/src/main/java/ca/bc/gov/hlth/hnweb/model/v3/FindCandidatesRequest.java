package ca.bc.gov.hlth.hnweb.model.v3;

public class FindCandidatesRequest {
	private Name name;
	
	private String surname;
	
	private String firstGivenName;
	
	private String secondName;
	
	private String gender;
	
	private String birthDate;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstGivenName() {
		return firstGivenName;
	}

	public void setFirstGivenName(String firstName) {
		this.firstGivenName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String dateOfBirth) {
		this.birthDate = dateOfBirth;
	}
	
}
