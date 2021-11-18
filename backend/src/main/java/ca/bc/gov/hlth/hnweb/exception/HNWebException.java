package ca.bc.gov.hlth.hnweb.exception;

public class HNWebException extends Exception {

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

	public HNWebException(String message) {
		super(message);
	}

	public HNWebException(Throwable cause) {
		super(cause);
	}

}
