package ca.bc.gov.hlth.hnweb.model;

public class R41Response {
	private String status;
	private String message;
	
	public R41Response(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "R41Response [status=" + status + ", message=" + message + "]";
	}
	
}
