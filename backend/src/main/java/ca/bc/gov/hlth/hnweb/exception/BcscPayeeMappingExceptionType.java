package ca.bc.gov.hlth.hnweb.exception;

/**
 * enum for possible types of {@link BcscPayeeMappingException}
 *
 */
public enum BcscPayeeMappingExceptionType {

	ENTITY_ALREADY_EXISTS("Entity already exists."),
	ENTITY_NOT_FOUND("Entity not found.");
	
	private final String message;

	private BcscPayeeMappingExceptionType(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
