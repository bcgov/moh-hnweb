package ca.bc.gov.hlth.hnweb.controller;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingException;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.UserPayeeMappingRequest;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.UserPayeeMappingResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.UserPayeeMapping;
import ca.bc.gov.hlth.hnweb.service.UserPayeeMappingService;
import ca.bc.gov.hlth.hnweb.service.PBFClinicPayeeService;

/**
 * Controller to handle CRUD requests for maintaining Keycloak Users to their PBF MSP Payee Number mappings.
 *   
 */
@RequestMapping("/payee-mapping")
@RestController
public class UserPayeeMappingController {

	private static final Logger logger = LoggerFactory.getLogger(UserPayeeMappingController.class);

	@Autowired
	private UserPayeeMappingService userPayeeMappingService;
		
	@Autowired
    private PBFClinicPayeeService pbfClinicPayeeService;
	
	/** 
	 * Create a User to MSP Payee Number mapping
	 *  
	 * @param userPayeeMappingRequest
	 * @return the response containing the newly created mapping if successful otherwise an error status 
	 */
	@PostMapping("")
	public ResponseEntity<UserPayeeMappingResponse> addUserPayeeMapping(@RequestBody UserPayeeMappingRequest userPayeeMappingRequest) {
		logger.info("Adding a new User to Payee Mapping:\n{}", userPayeeMappingRequest.toString());
		
		if (StringUtils.isBlank(userPayeeMappingRequest.getUserGuid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field userGuid.");
		}
		if (StringUtils.isBlank(userPayeeMappingRequest.getPayeeNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field payeeNumber.");
		}
		
		try {
			UserPayeeMapping userPayeeMapping = mapRequestToEntity(userPayeeMappingRequest);
			UserPayeeMapping newUserPayeeMapping = userPayeeMappingService.add(userPayeeMapping);		
			UserPayeeMappingResponse userPayeeMappingResponse = mapEntityToRepsonse(newUserPayeeMapping);			
			return ResponseEntity.ok(userPayeeMappingResponse);
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserPayeeMappingResponse> updateUserPayeeMapping(@RequestBody UserPayeeMappingRequest userPayeeMappingRequest, @PathVariable String id) {
		logger.info("Updating a User to Payee Mapping for ID: {}; Updated entity: \n{}", id, userPayeeMappingRequest.toString());
		
		if (StringUtils.isBlank(userPayeeMappingRequest.getPayeeNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field payeeNumber");
		}

		try {
			UserPayeeMapping userPayeeMapping = mapRequestToEntity(userPayeeMappingRequest);		
			UserPayeeMapping updatedUserPayeeMapping = userPayeeMappingService.update(userPayeeMapping, id);
			UserPayeeMappingResponse userPayeeMappingResponse = mapEntityToRepsonse(updatedUserPayeeMapping);		
			return ResponseEntity.ok(userPayeeMappingResponse);
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserPayeeMappingResponse> getUserPayeeMapping(@PathVariable String id) {
		logger.info("Getting a User to Payee Mapping for ID: {}", id);
		
		Optional<UserPayeeMapping> userPayeeMappingOptional = userPayeeMappingService.find(id);
		if (userPayeeMappingOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Entity not found for ID %s", id));
		}

		UserPayeeMapping userPayeeMapping = userPayeeMappingOptional.get();
		UserPayeeMappingResponse userPayeeMappingResponse = mapEntityToRepsonse(userPayeeMapping);
		
		userPayeeMappingResponse.setPayeeIsActive(pbfClinicPayeeService.getPayeeActiveStatus(userPayeeMapping.getPayeeNumber()));
		
		return ResponseEntity.ok(userPayeeMappingResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserPayeeMapping(@PathVariable String id) {
		logger.info("Deleting a User to Payee Mapping for ID: {}", id);

		try {
			userPayeeMappingService.delete(id);		
			return ResponseEntity.noContent().build();
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}
	
	private UserPayeeMappingResponse mapEntityToRepsonse(UserPayeeMapping newUserPayeeMapping) {
		UserPayeeMappingResponse userPayeeMappingResponse = new UserPayeeMappingResponse();
		userPayeeMappingResponse.setUserGuid(newUserPayeeMapping.getUserGuid());
		userPayeeMappingResponse.setPayeeNumber(newUserPayeeMapping.getPayeeNumber());
		return userPayeeMappingResponse;
	}

	private UserPayeeMapping mapRequestToEntity(UserPayeeMappingRequest userPayeeMappingRequest) {
		UserPayeeMapping userPayeeMapping = new UserPayeeMapping();
		userPayeeMapping.setUserGuid(userPayeeMappingRequest.getUserGuid());
		userPayeeMapping.setPayeeNumber(userPayeeMappingRequest.getPayeeNumber());
		return userPayeeMapping;
	}	

	private HttpStatus handleException(Exception exception) {
		HttpStatus status;
		
		if (exception instanceof UserPayeeMappingException) {
			UserPayeeMappingException bpme = (UserPayeeMappingException)exception;
			switch (bpme.getType()) {
			case ENTITY_ALREADY_EXISTS:
				status = HttpStatus.CONFLICT;
				break;
			case ENTITY_NOT_FOUND:
				status = HttpStatus.NOT_FOUND;
				break;
			default:
				status = HttpStatus.BAD_REQUEST;		
			}
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return status;
	}

}