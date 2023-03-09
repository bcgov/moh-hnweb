package ca.bc.gov.hlth.hnweb.exception;

/**
 * enum for possible types of {@link UserPayeeMappingException}
 *
 */
public enum UserPayeeMappingExceptionType {

	ENTITY_ALREADY_EXISTS("Entity already exists."),
	ENTITY_NOT_FOUND("Entity not found.");
	
	private final String message;

	private UserPayeeMappingExceptionType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
