package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rapid.FixedWidthDefaults;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;

public abstract class BaseRapidConverter {
	protected static final Logger logger = LoggerFactory.getLogger(BaseRapidConverter.class);

	protected static final String STATUS_CODE_SUCCESS = "RPBS9014";
	
	protected FixedWidthDefaults fwDefaults;

	public BaseRapidConverter(FixedWidthDefaults fwDefaults) {
		super();
		this.fwDefaults = fwDefaults;
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

	public abstract String getTranCode();
}
