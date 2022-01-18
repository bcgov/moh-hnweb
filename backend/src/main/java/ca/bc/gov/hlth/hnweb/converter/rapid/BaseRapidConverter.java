package ca.bc.gov.hlth.hnweb.converter.rapid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;

public abstract class BaseRapidConverter {
	protected static final Logger logger = LoggerFactory.getLogger(BaseRapidConverter.class);

	protected static final String RAPID_DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String STATUS_CODE_SUCCESS = "RPBS9014";
	
	protected static final String STATUS_TEXT_SUCCESS = "TRANSACTION SUCCESSFUL";
	
	protected UserInfo userInfo;
	
	public BaseRapidConverter() {
		super();
		this.userInfo = SecurityUtil.loadUserInfo();
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
	
	protected String formatDate(LocalDate date) {
		if (date == null) {
			return null;
		}
		return date.format(DateTimeFormatter.ofPattern(RAPID_DATE_FORMAT));
	}

	public abstract String getTranCode();
}
