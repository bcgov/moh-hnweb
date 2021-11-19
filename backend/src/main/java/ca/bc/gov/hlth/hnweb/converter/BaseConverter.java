package ca.bc.gov.hlth.hnweb.converter;

import java.time.format.DateTimeFormatter;

import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;

public abstract class BaseConverter {
	protected static DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
	
	protected static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmss");
}
