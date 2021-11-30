package ca.bc.gov.hlth.hnweb.model;

public class BaseResponse {
	protected StatusEnum status;
	protected String message;

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
