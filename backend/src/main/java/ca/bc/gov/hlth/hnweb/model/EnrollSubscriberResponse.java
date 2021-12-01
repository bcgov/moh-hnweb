package ca.bc.gov.hlth.hnweb.model;

import java.time.LocalDate;
import java.util.Date;

public class EnrollSubscriberResponse extends BaseResponse {

	private String phn;

	private String givenName;

	private String secondName;

	private String surname;

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

	
}