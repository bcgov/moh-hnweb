package ca.bc.gov.hlth.hnweb.model.v3;

import java.util.List;

public class FindCandidatesResponse {
	
	private List<FindCandidatesResult> results;
	private Message message;
	private int resultCount;
	
	public List<FindCandidatesResult> getResults() {
		return results;
	}
	public void setResults(List<FindCandidatesResult> results) {
		this.results = results;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

}