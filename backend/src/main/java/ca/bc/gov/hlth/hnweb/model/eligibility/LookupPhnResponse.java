package ca.bc.gov.hlth.hnweb.model.eligibility;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.BaseResponse;

public class LookupPhnResponse extends BaseResponse {
	private List<LookupPhnBeneficiary> beneficiaries = new ArrayList<LookupPhnBeneficiary>();

	public List<LookupPhnBeneficiary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<LookupPhnBeneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

	@Override
	public String toString() {
		return "LookupPhnResponse [beneficiaries=" + beneficiaries + "]";
	}

}