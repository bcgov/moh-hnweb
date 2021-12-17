package ca.bc.gov.hlth.hnweb.model;

import java.util.List;

public class GetNameSearchResponse extends BaseResponse {

	private List<NameSearchResult> candidates;

	public List<NameSearchResult> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<NameSearchResult> results) {
		this.candidates = results;
	}
	
}