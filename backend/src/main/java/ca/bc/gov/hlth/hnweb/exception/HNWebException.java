package ca.bc.gov.hlth.hnweb.exception;

public class HNWebException extends Exception {
	private ExceptionType type;

	private static final long serialVersionUID = 1L;

	public HNWebException() {
		super();
	}

	public HNWebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HNWebException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public HNWebException(ExceptionType type, Throwable cause) {
		super(type.getMessage(), cause);
		this.type = type;
	}
	
	public HNWebException(ExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}

	public HNWebException(String message) {
		super(message);
	}

	public HNWebException(Throwable cause) {
		super(cause);
	}

	public ExceptionType getType() {
		return type;
	}

	public void setType(ExceptionType type) {
		this.type = type;
	}

}
