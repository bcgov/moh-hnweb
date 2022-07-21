package ca.bc.gov.hlth.hnweb.model.rest.patientregistration;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;

public class ViewPatientRegisterResponse extends BaseResponse {
 
	private GetPersonDetailsResponse personDetail;
	
	public GetPersonDetailsResponse getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(GetPersonDetailsResponse personDetail) {
		this.personDetail = personDetail;
	}

	private List<PatientRegisterModel> patientRegistrationHistory = new ArrayList<>();
	
	
	public List<PatientRegisterModel> getPatientRegistrationHistory() {
		return patientRegistrationHistory;
	}

	public void setPatientRegistrationHistory(List<PatientRegisterModel> patientRegistrationHistory) {
		this.patientRegistrationHistory = patientRegistrationHistory;
	}
	
}