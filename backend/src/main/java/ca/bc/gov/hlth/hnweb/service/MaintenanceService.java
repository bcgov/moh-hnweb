package ca.bc.gov.hlth.hnweb.service;

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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAG0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAJ0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRE0;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

/**
 * Service for processing Coverage Maintenance requests. 
 *
 */
@Service
public class MaintenanceService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(MaintenanceService.class);

	@Value("${rapid.r43Path:}")
	private String r43Path;
	
	@Value("${rapid.r45Path:}")
	private String r45Path;
	
	@Value("${rapid.r46Path:}")
	private String r46Path;

	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Changes Coverage Effective Date for the group member based on the R46/RPBSPAJ0 request.
	 * 
	 * @param rpbspaj0
	 * @param transaction
	 * @return The RPBSPAJ0 response.
	 * @throws HNWebException
	 */
	public RPBSPAJ0 changeEffectiveDate(RPBSPAJ0 rpbspaj0, Transaction transaction) throws HNWebException {
		String rpbspaj0Str = rpbspaj0.serialize();

		logger.info("Request {}", rpbspaj0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r46Path, rpbspaj0Str,
				transaction.getTransactionId().toString());
		messageReceived(transaction);
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPAJ0 rpbspaj0Response = new RPBSPAJ0(response.getBody());

		return rpbspaj0Response;
	}
	
	/**
	 * Changes Coverage Cancellation Date for the group member based on the R46/RPBSPAG0 request.
	 * 
	 * @param rpbspag0
	 * @param transaction
	 * @return The RPBSPAG0 response.
	 * @throws HNWebException
	 */
	public RPBSPAG0 changeCancelDate(RPBSPAG0 rpbspag0, Transaction transaction) throws HNWebException {
		String rpbspag0Str = rpbspag0.serialize();

		logger.info("Request:\n{}", rpbspag0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r46Path, rpbspag0Str,
				transaction.getTransactionId().toString());
		messageReceived(transaction);
		logger.info("Response Status: {}; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPAG0 rpbspag0Response = new RPBSPAG0(response.getBody());

		return rpbspag0Response;
	}
	
	/**
	 * Reinstate an Over Age Dependent based on the R43/RPBSPRE0 request.
	 * 
	 * @param rpbspre0
	 * @return The RPBSPRE0 response.
	 * @throws HNWebException
	 */
	public RPBSPRE0 reinstateOverAgeDependent(RPBSPRE0 rpbspre0, Transaction transaction) throws HNWebException {
		String rpbspre0Str = rpbspre0.serialize();

		logger.info("Request {}", rpbspre0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r43Path, rpbspre0Str, transaction.getTransactionId().toString());
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		logger.info("Response {}", response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		messageReceived(transaction);
		return new RPBSPRE0(response.getBody());
	}
	
	public RPBSPAI0 renewCoverageEffectiveDate(RPBSPAI0 rpbspri0, Transaction transaction) throws HNWebException {
		String rpbspai0Str = rpbspri0.serialize();

		logger.info("Request {}", rpbspai0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r45Path, rpbspai0Str, transaction.getTransactionId().toString());
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		logger.info("Response {}", response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		messageReceived(transaction);
		return new RPBSPAI0(response.getBody());
	}
	
	private ResponseEntity<String> postRapidRequest(String path, String body, String transactionId) {
		return rapidWebClient
				.post()
				.uri(path)
				.contentType(MediaType.TEXT_PLAIN)
				.header(TRANSACTION_ID, transactionId)
				.bodyValue(body)
				.retrieve()
				.toEntity(String.class)
				.block();
	}
	
}
