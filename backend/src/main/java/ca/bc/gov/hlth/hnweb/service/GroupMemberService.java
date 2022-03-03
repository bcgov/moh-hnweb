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
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWB0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWC0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPXP0;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

/**
 * Service for:
 *  Add a dependent for the Group Member. (R31)
 *  Update Group Member's Number and/or Department. (R34)
 */
@Service
public class GroupMemberService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(GroupMemberService.class);

	@Value("${rapid.r30Path}")
	private String r30Path;
	
	@Value("${rapid.r31Path}")
	private String r31Path;

	@Value("${rapid.r34Path}")
	private String r34Path;

	@Value("${rapid.r35Path}")
	private String r35Path;
	
	@Value("${rapid.r36Path}")
	private String r36Path;
	
	@Autowired
	private WebClient rapidWebClient;

	/**
	 * Add a dependent for the group member based on the R31/RPBSPWB0 request.
	 * 
	 * @param rpbspwb0
	 * @param transaction 
	 * @return The RPBSPWB0 response.
	 * @throws HNWebException
	 */
	public RPBSPWB0 addDependent(RPBSPWB0 rpbspwb0, Transaction transaction) throws HNWebException {
		String rpbspwb0Str = rpbspwb0.serialize();

		logger.info("Request {}", rpbspwb0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r31Path, rpbspwb0Str);
		messageReceived(transaction);
		logger.info("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPWB0 rpbspwb0Response = new RPBSPWB0(response.getBody());

		return rpbspwb0Response;
	}
	
	/**
	 * Update the group member's department number based on the R34/RPBSPPE0 request.
	 * 
	 * @param rpbsped0
	 * @return The RPBSPED0 response.
	 * @throws HNWebException
	 */
	public RPBSPED0 updateGroupMemberDepartmentNumber(RPBSPED0 rpbsped0, Transaction transaction) throws HNWebException {
		String rpbsped0Str = rpbsped0.serialize();

		logger.info("Request {}", rpbsped0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r34Path, rpbsped0Str);
		messageReceived(transaction);
		
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
	public RPBSPEE0 updateGroupMemberEmployeeNumber(RPBSPEE0 rpbspee0, Transaction transaction) throws HNWebException {
		String rpbseed0Str = rpbspee0.serialize();

		logger.info("Request {}", rpbseed0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r34Path, rpbseed0Str);
		messageReceived(transaction);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPEE0 rpbspee0Response = new RPBSPEE0(response.getBody());

		return rpbspee0Response;
	}
	
	/**
	 * Adds the group member and spouse/dependents based on the R30/RPBSPXP0.
	 * 
	 * @param rpbspxp0
	 * @return The RPBSPXP0 response.
	 * @throws HNWebException
	 */
	public RPBSPXP0 addGroupMember(RPBSPXP0 rpbspxp0, Transaction transaction) throws HNWebException {
		String rpbspxp0Str = rpbspxp0.serialize();

		logger.info("Request {}", rpbspxp0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r30Path, rpbspxp0Str);
		messageReceived(transaction);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPXP0 rpbspxp0Response = new RPBSPXP0(response.getBody());

		return rpbspxp0Response;
	}
	
	/**
	 * Cancels the group member's coverage based on the R35/RPBSPWC0.
	 * 
	 * @param rpbspwc0
	 * @return The RPBSPWC0 response.
	 * @throws HNWebException
	 */
	public RPBSPWC0 cancelGroupMember(RPBSPWC0 rpbspwc0, Transaction transaction) throws HNWebException {
		String rpbspwc0Str = rpbspwc0.serialize();

		logger.info("Request {}", rpbspwc0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r35Path, rpbspwc0Str);
		messageReceived(transaction);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPWC0 rpbspwc0Response = new RPBSPWC0(response.getBody());

		return rpbspwc0Response;
	}
	
	/**
	 * Cancels the group member's dependent coverage based on the R36/RPBSPWP0.
	 * 
	 * @param rpbspwp0
	 * @return The RPBSPWP0 response.
	 * @throws HNWebException
	 */
	public RPBSPWP0 cancelDependent(RPBSPWP0 rpbspwp0, Transaction transaction) throws HNWebException {
		String rpbspwp0Str = rpbspwp0.serialize();

		logger.info("Request {}", rpbspwp0Str);
		
		messageSent(transaction);
		ResponseEntity<String> response = postRapidRequest(r36Path, rpbspwp0Str);
		messageReceived(transaction);
		
		logger.debug("Response Status: {} ; Message:\n{}", response.getStatusCode(), response.getBody());
		
		if (response.getStatusCode() != HttpStatus.OK) {
			logger.error("Could not connect to downstream service. Service returned {}", response.getStatusCode());
			throw new HNWebException(ExceptionType.DOWNSTREAM_FAILURE);
		}
		
		RPBSPWP0 rpbspwp0Response = new RPBSPWP0(response.getBody());

		return rpbspwp0Response;
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
