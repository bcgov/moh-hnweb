package ca.bc.gov.hlth.hnweb.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.hlth.hnweb.exception.ExceptionType;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPL0;
import ca.bc.gov.hlth.hnweb.model.v2.message.E45;
import ca.bc.gov.hlth.hnweb.model.v2.message.R15;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Service for:
 *  Eligibility request (R15)
 *  PHN requests (R41, R42)
 *  MSP Coverage request (E45)
 *
 */
@Service
public class EligibilityService {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityService.class);

	public static final String TRANSACTION_ID = "TransactionID";
	
	@Value("${hibc.e45.path}")
	private String e45Path;
	
	@Value("${hibc.e45.username}")
	private String e45Username;
	
	@Value("${hibc.e45.password}")
	private String e45Password;
	
	@Value("${rapid.r41Path}")
	private String r41Path;
	
	@Value("${rapid.r42Path}")
	private String r42Path;

	@Autowired
	private Parser parser;

	private static final String R15_RESPONSE_ELIGIBLE = "MSH|^~\\&|RAICHK-BNF-CVST|BC00001013|HNWeb|moh_hnclient_dev|20211118181051|train96|R15|20210513182941|D|2.4||\r\n"
			+ "MSA|AA|20210513182941|HJMB001ISUCCESSFULLY COMPLETED\r\n" + "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n" + "ZTL|1^RD\r\n"
			+ "IN1|1||||||||||||||||||||||||Y\r\n" + "ZIH||||||||||||||||||1";

	private static final String R15_RESPONSE_INELIGIBLE = "MSH|^~\\&|RAICHK-BNF-CVST|BC00001013|HNWeb|moh_hnclient_dev|20211118181051|train96|R15|20210513182941|D|2.4||\r\n"
			+ "MSA|AA|20210513182941|HJMB001ISUCCESSFULLY COMPLETED\r\n" + "ERR|^^^HJMB001I&SUCCESSFULLY COMPLETED\r\n" + "ZTL|1^RD\r\n"
			+ "IN1|1||||||||||||20221231||||||||||||N\r\n" + "ZIH|||||||||||||||DECEASED|20221201||1";

	@Autowired
	private WebClient hibcWebClient;

	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Checks for subject eligibility based on the R15 request.
	 * 
	 * @return The R15 response.
	 * @throws HL7Exception
	 */
	public Message checkEligibility(R15 r15) throws HL7Exception {
		String r15v2 = parser.encode(r15);

		// TODO (weskubo-hni) Send to actual endpoint when it's available, currently
		// response is stubbed.
//		ResponseEntity<String> response = postCheckEligibilityRequest(r15v2, UUID.randomUUID().toString());
//		String responseBody = response.getBody();
		String responseBody = generateCannedResponse(r15);

		return parseResponse(responseBody, "R15");
	}

	public RPBSPPE0 inquirePhn(RPBSPPE0 rpbsppe0) throws HNWebException {
		String rpbsppe0Str = rpbsppe0.serialize();

		logger.info("Request {}", rpbsppe0Str);
		
		ResponseEntity<String> response = postRapidRequest(r41Path, rpbsppe0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPPE0 rpbsppe0Response = new RPBSPPE0(response.getBody());

		return rpbsppe0Response;
	}
	
	public RPBSPPL0 lookupPhn(RPBSPPL0 rpbsppl0) throws HNWebException {
		String rpbsppl0Str = rpbsppl0.serialize();

		logger.info("Request {}", rpbsppl0Str);
		
		ResponseEntity<String> response = postRapidRequest(r42Path, rpbsppl0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPPL0 rpbsppl0Response = new RPBSPPL0(response.getBody());

		return rpbsppl0Response;
	}
	

	private ResponseEntity<String> postRapidRequest(String path, String body) {
		return rapidWebClient
				.post()
				.uri(path)
				.contentType(MediaType.TEXT_PLAIN)
				.header(TRANSACTION_ID, UUID.randomUUID().toString())
				.bodyValue(body)
				.retrieve()
				.toEntity(String.class)
				.block();
	}
	
	private ResponseEntity<String> postHibcRequest(String path, String username, String password, String data) {
        return hibcWebClient
                .post()
                .uri(path)
                .contentType(MediaType.TEXT_PLAIN)
                .header(TRANSACTION_ID, UUID.randomUUID().toString())
                .headers(header -> header.setBasicAuth(username, password))
                .bodyValue(data)
                .retrieve()
                .toEntity(String.class)
                .block();
    }	

	public Message checkMspCoverageStatus(E45 e45) throws HNWebException, HL7Exception {

		String e45v2 = parser.encode(e45);
		ResponseEntity<String> response = postHibcRequest(e45Path, e45Username, e45Password, e45v2);
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		return parseResponse(response.getBody(), "E45");
	}

	private String generateCannedResponse(R15 r15) {
		Calendar serviceDate = Calendar.getInstance();
		try {
			serviceDate.setTime(new SimpleDateFormat("yyyyMMdd").parse(r15.getIN1().getPlanEffectiveDate().toString()));
		} catch (ParseException e) {
			// Ignore
		}

		Calendar today = Calendar.getInstance();

		int yearsDiff = serviceDate.get(Calendar.YEAR) - today.get(Calendar.YEAR);

		String cannedResponse = null;
		if (yearsDiff > 1) {
			cannedResponse = R15_RESPONSE_INELIGIBLE;
		} else {
			cannedResponse = R15_RESPONSE_ELIGIBLE;
		}
		return cannedResponse;
	}

	/**
	 * Parse the V2 String and build a message from it that gets returned.
	 * 
	 * @param response    The raw V2 response
	 * @param messageType The target message type
	 * @return The Message
	 * @throws HL7Exception
	 */
	private Message parseResponse(String response, String messageType) throws HL7Exception {
		Message message = parser.parse(V2MessageUtil.correctMSH9(response, messageType));
		logger.debug("V2 message parsed to {} object", message.getName());

		return message;
	}

}
