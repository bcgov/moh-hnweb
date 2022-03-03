package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class GetContractPeriodsResponse extends BaseResponse {
	private String phn;
	
	private List<BeneficiaryContractPeriod> beneficiaryContractPeriods = new ArrayList<>();
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public List<BeneficiaryContractPeriod> getBeneficiaryContractPeriods() {
		return beneficiaryContractPeriods;
	}

	public void setBeneficiaryContractPeriods(List<BeneficiaryContractPeriod> beneficiaryContractPeriods) {
		this.beneficiaryContractPeriods = beneficiaryContractPeriods;
	}

	@Override
	public String toString() {
		return "GetContractPeriodsRequest [phn=" + phn + "]";
	}

}
