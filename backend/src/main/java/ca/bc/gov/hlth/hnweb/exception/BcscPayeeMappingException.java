package ca.bc.gov.hlth.hnweb.exception;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.BcscPayeeMapping;

/**
 * Exception class for errors related to the {@link BcscPayeeMapping} entity.
 * 
 */
public class BcscPayeeMappingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private BcscPayeeMappingExceptionType type;

	public BcscPayeeMappingException(BcscPayeeMappingExceptionType type) {
		super(type.getMessage());
		this.type = type;
	}

	public BcscPayeeMappingExceptionType getType() {
		return type;
	}

	public void setType(BcscPayeeMappingExceptionType type) {
		this.type = type;
	}

}
