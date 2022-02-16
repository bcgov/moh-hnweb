package ca.bc.gov.hlth.hnweb.model.v3;

public class GetDemographicsResponse {

  private String messageIdExtension;
  private Person person;
  private Message message;
  private int resultCount;

  public GetDemographicsResponse() {}

  public String getMessageIdExtension() {
	return messageIdExtension;
  }
	
  public void setMessageIdExtension(String messageIdExtension) {
	this.messageIdExtension = messageIdExtension;
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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}