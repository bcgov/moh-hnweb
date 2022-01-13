package ca.bc.gov.hlth.hnweb.model.v3;

import java.math.BigDecimal;

public class FindCandidatesResult {

	private Person person;
	private BigDecimal score;

	public FindCandidatesResult() {
    }

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}	
}
