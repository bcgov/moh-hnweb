package ca.bc.gov.hlth.hnweb.model;
public class GetPersonDetailsResponse {

  private Person person;
  private Message message;
  private int resultCount;

  public GetPersonDetailsResponse() {}

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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
  
  @Override
	public String toString() {
		return "GetPersonDetails [phn=" + person.getPhn() + ", surname=" + person.getDocumentedName().getSurname()
				+ "givenName=" + person.getDocumentedName().getFirstGivenName()
				+ "dateOfBirth=" + person.getBirthDate() +  "]";
	}
}