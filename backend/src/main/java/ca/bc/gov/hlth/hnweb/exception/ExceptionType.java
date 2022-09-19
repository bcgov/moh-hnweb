package ca.bc.gov.hlth.hnweb.exception;

public enum ExceptionType {
	DOWNSTREAM_FAILURE("Could not connect to downstream service"),
	GENERAL("An error has occured"),
	SSL_FAILURE("SSL Context could not be built, application will not start"),
	PAYEE_MAPPING_NOT_FOUND("No Payee Number mapping was found for the current user"),
	PAYEE_MAPPING_INCORRECT("Payee field value does not match the Payee Number mapped to this user");
	
	ExceptionType(String message) {
		this.message = message;
	}

	private final String message;

	public String getMessage() {
		return message;
	}

}
