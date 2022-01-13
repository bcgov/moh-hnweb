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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPED0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;

/**
 * Service for:
 *  Update Group Member's Number and/or Department. (R34)
 */
@Service
public class GroupMemberService {

	private static final Logger logger = LoggerFactory.getLogger(GroupMemberService.class);

	public static final String TRANSACTION_ID = "TransactionID";
	
	@Value("${rapid.r34Path}")
	private String r34Path;

	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Update the group member's department number based on the R34/RPBSPPE0 request.
	 * 
	 * @param rpbsped0
	 * @return The RPBSPED0 response.
	 * @throws HNWebException
	 */
	public RPBSPED0 updateGroupMemberDepartmentNumber(RPBSPED0 rpbsped0) throws HNWebException {
		String rpbsped0Str = rpbsped0.serialize();

		logger.info("Request {}", rpbsped0Str);
		
		ResponseEntity<String> response = postRapidRequest(r34Path, rpbsped0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPED0 rpbsped0Response = new RPBSPED0(response.getBody());

		return rpbsped0Response;
	}
	
	/**
	 * Update the group member's department number based on the R34/RPBSPPE0 request.
	 * 
	 * @param rpbspee0
	 * @return The RPBSPED0 response.
	 * @throws HNWebException
	 */
	public RPBSPEE0 updateGroupMemberEmployeeNumber(RPBSPEE0 rpbspee0) throws HNWebException {
		String rpbseed0Str = rpbspee0.serialize();

		logger.info("Request {}", rpbseed0Str);
		
		ResponseEntity<String> response = postRapidRequest(r34Path, rpbseed0Str);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPEE0 rpbspee0Response = new RPBSPEE0(response.getBody());

		return rpbspee0Response;
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