package ca.bc.gov.hlth.hnweb.model;

/**
 * Request message for API call to enroll a subscriber 
 *
 */
public class EnrollSubscriberRequest {
	
	private String phn;
	
	private String surname;
		
	private String gender;
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
