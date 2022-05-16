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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPCI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMA0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

/**
 * Service for: Get Contract Periods. (R32), ContractInquiry(R40), Update Group
 * Memeber's Contract Address(R38)
 */
@Service
public class MspContractsService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(MspContractsService.class);

	@Value("${rapid.r32Path}")
	private String r32Path;

	@Value("${rapid.r40Path}")
	private String r40Path;

	@Value("${rapid.r38Path}")
	private String r38Path;

	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Get Contracts Periods for a Personal Health Number (PHN) Inquiry based on the
	 * R32/RPBSPMC0 request.
	 * 
	 * @param rpbspmc0
	 * @return The {@link RPBSPMC0} response.
	 * @throws HNWebException
	 */
	public RPBSPMC0 getContractPeriods(RPBSPMC0 rpbspmc0, Transaction transaction) throws HNWebException {
		String rpbspmc0Str = rpbspmc0.serialize();

		logger.info("Request:\n{}", rpbspmc0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r32Path, rpbspmc0Str, transaction.getTransactionId().toString());
		messageReceived(transaction);

		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPMC0 rpbsmc0Response = new RPBSPMC0(response.getBody());

		return rpbsmc0Response;
	}

	/**
	 * Get MSP Coverage info for a Personal Health Number (PHN) of a
	 * group(GroupNumber) Inquiry based on the R40/RPBSPCI0 request.
	 * 
	 * @param rpbspci0
	 * @return
	 * @throws HNWebException
	 */
	public RPBSPCI0 inquireContract(RPBSPCI0 rpbspci0, Transaction transaction) throws HNWebException {
		String rpbspci0Str = rpbspci0.serialize();

		logger.info("Request:\n{}", rpbspci0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r40Path, rpbspci0Str, transaction.getTransactionId().toString());
		messageReceived(transaction);
		
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPCI0 rpbspci0Response = new RPBSPCI0(response.getBody());

		return rpbspci0Response;
	}

	/**
	 * Update Group Member's Contract Address for a Personal Health Number(PHN) of a
	 * group based on the R38/RPBSPMA0 request
	 * 
	 * @param rpbspma0
	 * @return
	 * @throws HNWebException
	 */
	public RPBSPMA0 updateAddress(RPBSPMA0 rpbspma0, Transaction transaction) throws HNWebException {
		String rpbspma0Str = rpbspma0.serialize();

		logger.info("Request:\n{}", rpbspma0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r38Path, rpbspma0Str, transaction.getTransactionId().toString());
		messageReceived(transaction);
		
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPMA0 rpbspma0Response = new RPBSPMA0(response.getBody());

		return rpbspma0Response;
	}

	/**
	 * Update Group Member's Contract Address Phone Number for a Personal Health
	 * Number(PHN) of a group based on the R38/RPBSPEP0 request
	 * 
	 * @param rpbspep0
	 * @return
	 * @throws HNWebException
	 */
	public RPBSPEP0 updatePhone(RPBSPEP0 rpbspep0, Transaction transaction) throws HNWebException {
		String rpbspep0Str = rpbspep0.serialize();

		logger.info("Request:\n{}", rpbspep0Str);

		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r38Path, rpbspep0Str, transaction.getTransactionId().toString());
		messageReceived(transaction);
		
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());

		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}

		RPBSPEP0 rpbspep0Response = new RPBSPEP0(response.getBody());

		return rpbspep0Response;
	}

	private ResponseEntity<String> postRapidRequest(String path, String body, String transactionId) {
		return rapidWebClient.post().uri(path).contentType(MediaType.TEXT_PLAIN)
				.header(TRANSACTION_ID, transactionId).bodyValue(body).retrieve().toEntity(String.class)
				.block();
	}

}
