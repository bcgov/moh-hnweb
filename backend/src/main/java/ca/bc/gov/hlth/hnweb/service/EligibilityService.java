package ca.bc.gov.hlth.hnweb.service;

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
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
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
public class EligibilityService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(EligibilityService.class);

	@Value("${hibc.e45.path}")
	private String e45Path;

	@Value("${hibc.e45.username}")
	private String e45Username;
	
	@Value("${hibc.e45.password}")
	private String e45Password;

	@Value("${hibc.r15.path}")
	private String r15Path;
	
	@Value("${hibc.r15.username}")
	private String r15Username;
	
	@Value("${hibc.r15.password}")
	private String r15Password;
	
	@Value("${rapid.r41Path}")
	private String r41Path;
	
	@Value("${rapid.r42Path}")
	private String r42Path;
	
	@Autowired
	private Parser parser;

	@Autowired
	private WebClient hibcWebClient;

	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Checks for subject eligibility based on the R15 request.
	 * 
	 * @param r15
	 * @return The R15 response.
	 * @throws HNWebException
	 * @throws HL7Exception
	 */
	public Message checkEligibility(R15 r15, Transaction transaction) throws HNWebException, HL7Exception {
		String r15v2 = parser.encode(r15);

		messageSent(transaction, V2MessageUtil.getMessageID(r15));
		ResponseEntity<String> response = postHibcRequest(r15Path, r15Username, r15Password, r15v2);
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		Message v2Response = parseResponse(response.getBody(), "R15");
    	messageReceived(transaction, V2MessageUtil.getMessageID(v2Response));
		
		return v2Response;
	}
	
	/**
	 * Checks for MSP Coverage Status based on the E45 request.
	 * 
	 * @param e45
	 * @return The E45 response.
	 * @throws HNWebException
	 * @throws HL7Exception
	 */
	public Message checkMspCoverageStatus(E45 e45, Transaction transaction) throws HNWebException, HL7Exception {
		String e45v2 = parser.encode(e45);
		
		messageSent(transaction, V2MessageUtil.getMessageID(e45));
		ResponseEntity<String> response = postHibcRequest(e45Path, e45Username, e45Password, e45v2);
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		Message v2Response = parseResponse(response.getBody(), "E45");
    	messageReceived(transaction, V2MessageUtil.getMessageID(v2Response));
		
		return v2Response;
	}

	/**
	 * Inquire on the PHN based on the R41/RPBSPPE0 request.
	 * 
	 * @param rpbsppe0
	 * @return The RPBSPPE0 response.
	 * @throws HNWebException
	 */
	public RPBSPPE0 inquirePhn(RPBSPPE0 rpbsppe0, Transaction transaction) throws HNWebException {
		String rpbsppe0Str = rpbsppe0.serialize();

		logger.info("Request {}", rpbsppe0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r41Path, rpbsppe0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		messageReceived(transaction);
		return new RPBSPPE0(response.getBody());
	}
	
	/**
	 * Lookup the PHN based on the R42/RPBSPPL0 request.
	 * 
	 * @param rpbsppl0
	 * @return The RPBSPPL0 response.
	 * @throws HNWebException
	 */
	public RPBSPPL0 lookupPhn(RPBSPPL0 rpbsppl0, Transaction transaction) throws HNWebException {
		String rpbsppl0Str = rpbsppl0.serialize();

		logger.info("Request {}", rpbsppl0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r42Path, rpbsppl0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		messageReceived(transaction);
		return new RPBSPPL0(response.getBody());
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
