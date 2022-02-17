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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;

/**
 * Service for:
 *  Get Contract Periods. (R32)
 */
@Service
public class MspContractsService {

	private static final Logger logger = LoggerFactory.getLogger(MspContractsService.class);

	public static final String TRANSACTION_ID = "TransactionID";

	@Value("${rapid.r32Path}")
	private String r32Path;
	
	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Get Contracts Periods for a Personal Health Number (PHN) Inquiry based on the R32/RPBSPMC0 request.
	 * 
	 * @param rpbspmc0
	 * @return The {@link RPBSPMC0} response.
	 * @throws HNWebException
	 */
	public RPBSPMC0 getContractPeriods(RPBSPMC0 rpbspmc0) throws HNWebException {
		String rpbspmc0Str = rpbspmc0.serialize();

		logger.info("Request:\n{}", rpbspmc0Str);
		
		ResponseEntity<String> response = postRapidRequest(r32Path, rpbspmc0Str);
		
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPMC0 rpbsmc0Response = new RPBSPMC0(response.getBody());

		return rpbsmc0Response;
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

}
