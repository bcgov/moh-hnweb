package ca.bc.gov.hlth.hnweb.model;

import java.util.List;

public class InquirePhnResponse extends BaseResponse {
	private List<InquirePhnMatch> matches;

	public List<InquirePhnMatch> getMatches() {
		return matches;
	}

	public void setMatches(List<InquirePhnMatch> matches) {
		this.matches = matches;
	}

	@Override
	public String toString() {
		return "InquirePhnResponse [matches=" + matches + "]";
	}

}