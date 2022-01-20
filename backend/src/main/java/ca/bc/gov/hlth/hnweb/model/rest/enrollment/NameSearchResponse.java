package ca.bc.gov.hlth.hnweb.model.rest.enrollment;

import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class NameSearchResponse extends BaseResponse {

	private List<NameSearchResult> candidates;

	public List<NameSearchResult> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<NameSearchResult> results) {
		this.candidates = results;
	}
	
}