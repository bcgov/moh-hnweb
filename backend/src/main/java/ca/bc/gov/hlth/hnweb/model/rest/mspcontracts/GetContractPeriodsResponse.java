package ca.bc.gov.hlth.hnweb.model.rest.mspcontracts;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class GetContractPeriodsResponse extends BaseResponse {
	private String phn;
	
	private List<ContractPeriod> contractPeriods = new ArrayList<>();
	
	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public List<ContractPeriod> getContractPeriods() {
		return contractPeriods;
	}

	public void setContractPeriods(List<ContractPeriod> contractPeriods) {
		this.contractPeriods = contractPeriods;
	}

	@Override
	public String toString() {
		return "GetContractPeriodsRequest [phn=" + phn + "]";
	}

}
