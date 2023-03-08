package ca.bc.gov.hlth.hnweb.exception;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.UserPayeeMapping;

/**
 * Exception class for errors related to the {@link UserPayeeMapping} entity.
 * 
 */
public class UserPayeeMappingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private UserPayeeMappingExceptionType type;

	public UserPayeeMappingException(UserPayeeMappingExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}

	public UserPayeeMappingExceptionType getType() {
		return type;
	}

	public void setType(UserPayeeMappingExceptionType type) {
		this.type = type;
	}

}
