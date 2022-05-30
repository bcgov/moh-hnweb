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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAJ0;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

/**
 * Service for: Change Effective Date. (R46)
 */
@Service
public class MaintenanceService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(MaintenanceService.class);

	@Value("${rapid.r46Path}")
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

	private ResponseEntity<String> postRapidRequest(String path, String body, String transactionId) {
		return rapidWebClient.post().uri(path).contentType(MediaType.TEXT_PLAIN).header(TRANSACTION_ID, transactionId)
				.bodyValue(body).retrieve().toEntity(String.class).block();
	}

}
