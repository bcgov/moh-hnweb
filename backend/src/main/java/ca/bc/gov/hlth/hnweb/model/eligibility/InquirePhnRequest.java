package ca.bc.gov.hlth.hnweb.model.eligibility;

import java.util.ArrayList;
import java.util.List;

public class InquirePhnRequest {
	private List<String> phns = new ArrayList<>();

	public List<String> getPhns() {
		return phns;
	}

	public void setPhns(List<String> phns) {
		this.phns = phns;
	}

	@Override
	public String toString() {
		return "PhnEnquiryRequest [phns=" + phns + "]";
	}

}
