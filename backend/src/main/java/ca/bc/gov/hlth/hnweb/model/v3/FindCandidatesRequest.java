package ca.bc.gov.hlth.hnweb.model.v3;

public class FindCandidatesRequest {
	private Name name;
	
	private String gender;
	
	private String birthDate;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
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
