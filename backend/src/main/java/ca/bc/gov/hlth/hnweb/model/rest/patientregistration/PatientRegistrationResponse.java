package ca.bc.gov.hlth.hnweb.model.rest.patientregistration;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;

public class PatientRegistrationResponse extends BaseResponse {
 
	private GetPersonDetailsResponse personDetail;
	
	private List<PatientRegisterModel> patientRegistrationHistory = new ArrayList<>();
	
	private String additionalInfoMessage;
	
	public GetPersonDetailsResponse getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(GetPersonDetailsResponse personDetail) {
		this.personDetail = personDetail;
	}	
	
	public List<PatientRegisterModel> getPatientRegistrationHistory() {
		return patientRegistrationHistory;
	}

	public void setPatientRegistrationHistory(List<PatientRegisterModel> patientRegistrationHistory) {
		this.patientRegistrationHistory = patientRegistrationHistory;
	}

	public String getAdditionalInfoMessage() {
		return additionalInfoMessage;
	}

	public void setAdditionalInfoMessage(String additionalInfoMessage) {
		this.additionalInfoMessage = additionalInfoMessage;
	}
	
}