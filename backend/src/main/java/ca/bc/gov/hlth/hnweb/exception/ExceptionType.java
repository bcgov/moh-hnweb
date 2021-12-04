package ca.bc.gov.hlth.hnweb.exception;

public enum ExceptionType {
	DOWNSTREAM_FAILURE("Could not connect to downstream service"),
	GENERAL("An error has occured"),
	SSL_FAILURE("SSL Context could not be built, application will not start");

	ExceptionType(String message) {
		this.message = message;
	}

	private final String message;

	public String getMessage() {
		return message;
	}

}
