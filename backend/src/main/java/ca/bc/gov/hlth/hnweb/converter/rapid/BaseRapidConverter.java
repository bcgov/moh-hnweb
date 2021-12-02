package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rapid.RapidDefaults;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;

public abstract class BaseRapidConverter {
	protected static final Logger logger = LoggerFactory.getLogger(BaseRapidConverter.class);

	protected static final String RAPID_DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String STATUS_CODE_SUCCESS = "RPBS9014";
	
	protected static final String STATUS_TEXT_SUCCESS = "TRANSACTION SUCCESSFUL";
	
	protected RapidDefaults rapidDefaults;

	public BaseRapidConverter(RapidDefaults rapidDefaults) {
		super();
		this.rapidDefaults = rapidDefaults;
	}
	
	protected void handleStatus(RPBSHeader header, BaseResponse response) {
		String statusCode = header.getStatusCode();
		String statusText = StringUtils.trimToEmpty(header.getStatusText());
		if (StringUtils.equals(header.getIdentifier(), RPBSHeader.IDENTIFER_ERRORMSG)) {
			response.setStatus(StatusEnum.ERROR);
			response.setMessage(statusCode + " " + statusText);
		} else if (StringUtils.equals(header.getIdentifier(), RPBSHeader.IDENTIFER_RESPONSE)) {
			if (StringUtils.equals(statusCode, STATUS_CODE_SUCCESS)) {
				response.setStatus(StatusEnum.SUCCESS);
				response.setMessage(statusText);
			} else {
				response.setStatus(StatusEnum.WARNING);
				response.setMessage(statusCode + " " + statusText);
			}			
		} else {
			logger.warn("Unrecognized identifier {}", header.getIdentifier());
			response.setStatus(StatusEnum.WARNING);
			response.setMessage(statusCode + " " + statusText);
		}
	}
	
	protected String convertBirthDate(String birthDate) {
		// Convert the response Date from yyyy-MM-dd to yyyyMMdd
		return StringUtils.remove(birthDate, "-");
	}

	public abstract String getTranCode();
}
