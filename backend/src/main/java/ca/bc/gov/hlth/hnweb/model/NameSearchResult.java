package ca.bc.gov.hlth.hnweb.model;

import ca.bc.gov.hlth.hnweb.model.v3.Person;

public class NameSearchResult {

	private Person person;
	private double score;

	public NameSearchResult() {
    }

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getPersonNameDisplay() {

		if (this.person.isDeclaredNameMasked() || this.person.isDocumentedNameMasked()) {
			return "Confidential";
		}

		StringBuilder nameDisplay;

		// 1. verify if documented/declared name exists and display the
		// surname/firstGivenName for the documentedName
		// 2. verify if only documentedName exists and display the corresponding
		// surname/firstGivenName
		// 3. verify if only declaredName exists and display the corresponding
		// surname/firstGivenName
		if (((this.person.getDocumentedName().getSurname() != null
				&& this.person.getDocumentedName().getFirstGivenName() != null)
				&& ((this.person.getDeclaredName().getSurname() != null
						&& this.person.getDeclaredName().getFirstGivenName() != null)))
				|| ((this.person.getDocumentedName().getSurname() != null
						&& this.person.getDocumentedName().getFirstGivenName() != null)
						&& (this.person.getDeclaredName().getSurname() == null
								&& this.person.getDeclaredName().getFirstGivenName() == null))) {
			nameDisplay = new StringBuilder(this.person.getDocumentedName().getDisplayText());
		} else if ((this.person.getDocumentedName().getSurname() == null
				&& this.person.getDocumentedName().getFirstGivenName() == null)
				&& (this.person.getDeclaredName().getSurname() != null
						&& this.person.getDeclaredName().getFirstGivenName() != null)) {
			nameDisplay = new StringBuilder(this.person.getDeclaredName().getDisplayText());
		} else {
			nameDisplay = new StringBuilder("Name is empty");
		}

		return nameDisplay.toString();
	}
}
