package ca.bc.gov.hlth.hnweb.model.rest.eligibility;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class InquirePhnResponse extends BaseResponse {
	private List<InquirePhnBeneficiary> beneficiaries = new ArrayList<>();

	public List<InquirePhnBeneficiary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<InquirePhnBeneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	@Override
	public String toString() {
		return "InquirePhnResponse [beneficiaries=" + beneficiaries + "]";
	}



}