package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;

public class LocalDateConverter extends AbstractSingleValueConverter {
	private static final Logger logger = LoggerFactory.getLogger(LocalDateConverter.class);

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return (type != null) && LocalDate.class.getPackage().equals(type.getPackage());
	}

	public String toString(Object source) {
		return source.toString();
	}

	public Object fromString(String str) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(V2MessageUtil.DATE_FORMAT_DATE_ONLY);
			return LocalDate.parse(str, formatter);
		} catch (DateTimeParseException e) {
			final ConversionException exception = new ConversionException("Cannot parse value as local date", e);
			exception.add("value", str);
			logger.error(e.getMessage());
		}
		return str;
	}

}
