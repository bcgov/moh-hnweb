package ca.bc.gov.hlth.hnweb.model;

/**
 *Wrapper class for GetPersonDetailsResponse
 */
public class PersonDetailsResponse extends BaseResponse {
	GetPersonDetailsResponse person;

	public GetPersonDetailsResponse getPerson() {
		return person;
	}

	public void setPerson(GetPersonDetailsResponse person) {
		this.person = person;
	}

	

}