package ca.bc.gov.hlth.hnweb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Utility class for V3 messages
 *
 */
public class V3MessageUtil {

	public static final String SOAP_ENVELOPE_START = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:hl7-org:v3\">\r\n"
			+ "    <soapenv:Header/>\r\n" + "    <soapenv:Body>";
	
	public static final String SOAP_ENVELOPE_END = "</soapenv:Body></soapenv:Envelope>";
	
	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	/**
	 * Omits XML header and wraps the request in a standard soap envelope
	 * @param xmlRequest
	 * @return
	 */
	public static String wrap(String xmlRequest) {
		String formattedRequest = xmlRequest.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
		return SOAP_ENVELOPE_START + formattedRequest + SOAP_ENVELOPE_END;
	}
	
	public static String convertDateToString(Date date) {
		return new SimpleDateFormat(V2MessageUtil.DATE_FORMAT_DATE_ONLY).format(date);
	}
	
}
