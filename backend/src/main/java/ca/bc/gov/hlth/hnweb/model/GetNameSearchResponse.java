package ca.bc.gov.hlth.hnweb.model;

import java.util.List;

public class GetNameSearchResponse extends BaseResponse {

	private List<NameSearchResult> results;

	public List<NameSearchResult> getResults() {
		return results;
	}

	public void setResults(List<NameSearchResult> results) {
		this.results = results;
	}
	
}