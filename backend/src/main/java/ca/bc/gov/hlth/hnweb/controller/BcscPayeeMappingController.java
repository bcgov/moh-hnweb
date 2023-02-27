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

import ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingException;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.BcscPayeeMappingRequest;
import ca.bc.gov.hlth.hnweb.model.rest.pbf.BcscPayeeMappingResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.BcscPayeeMapping;
import ca.bc.gov.hlth.hnweb.service.BcscPayeeMappingService;
import ca.bc.gov.hlth.hnweb.service.PBFClinicPayeeService;

/**
 * Controller to handle CRUD requests for maintaining BC Services Card (BCSC) Users to their PBF MSP Payee Number mappings.
 *   
 */
@RequestMapping("/payee-mapping")
@RestController
public class BcscPayeeMappingController {

	private static final Logger logger = LoggerFactory.getLogger(BcscPayeeMappingController.class);

	@Autowired
	private BcscPayeeMappingService bcscPayeeMappingService;
		
	@Autowired
    private PBFClinicPayeeService pbfClinicPayeeService;
	
	/** 
	 * Create a BCSC User to MSP Payee Number mapping
	 *  
	 * @param bcscPayeeMappingRequest
	 * @return the response containing the newly created mapping if successful otherwise an error status 
	 */
	@PostMapping("")
	public ResponseEntity<BcscPayeeMappingResponse> addBcscPayeeMapping(@RequestBody BcscPayeeMappingRequest bcscPayeeMappingRequest) {
		logger.info("Adding a new BCSC User to Payee Mapping:\n{}", bcscPayeeMappingRequest.toString());
		
		if (StringUtils.isBlank(bcscPayeeMappingRequest.getBcscGuid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field bcscGuid.");
		}
		if (StringUtils.isBlank(bcscPayeeMappingRequest.getPayeeNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field payeeNumber.");
		}
		
		try {
			BcscPayeeMapping bcscPayeeMapping = mapRequestToEntity(bcscPayeeMappingRequest);
			BcscPayeeMapping newBcscPayeeMapping = bcscPayeeMappingService.add(bcscPayeeMapping);		
			BcscPayeeMappingResponse bcscPayeeMappingResponse = mapEntityToRepsonse(newBcscPayeeMapping);			
			return ResponseEntity.ok(bcscPayeeMappingResponse);
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<BcscPayeeMappingResponse> updateBcscPayeeMapping(@RequestBody BcscPayeeMappingRequest bcscPayeeMappingRequest, @PathVariable String id) {
		logger.info("Updating a BCSC User to Payee Mapping for ID: {}; Updated entity: \n{}", id, bcscPayeeMappingRequest.toString());
		
		if (StringUtils.isBlank(bcscPayeeMappingRequest.getPayeeNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing value in required request field payeeNumber");
		}

		try {
			BcscPayeeMapping bcscPayeeMapping = mapRequestToEntity(bcscPayeeMappingRequest);		
			BcscPayeeMapping updatedBcscPayeeMapping = bcscPayeeMappingService.update(bcscPayeeMapping, id);
			BcscPayeeMappingResponse bcscPayeeMappingResponse = mapEntityToRepsonse(updatedBcscPayeeMapping);		
			return ResponseEntity.ok(bcscPayeeMappingResponse);
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BcscPayeeMappingResponse> getBcscPayeeMapping(@PathVariable String id) {
		logger.info("Getting a BCSC User to Payee Mapping for ID: {}", id);
		
		Optional<BcscPayeeMapping> bcscPayeeMappingOptional = bcscPayeeMappingService.find(id);
		if (bcscPayeeMappingOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Entity not found for ID %s", id));
		}

		BcscPayeeMapping bcscPayeeMapping = bcscPayeeMappingOptional.get();
		BcscPayeeMappingResponse bcscPayeeMappingResponse = mapEntityToRepsonse(bcscPayeeMapping);
		
		bcscPayeeMappingResponse.setPayeeStatus(pbfClinicPayeeService.getPayeeStatus(bcscPayeeMapping.getPayeeNumber()));
		
		return ResponseEntity.ok(bcscPayeeMappingResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBcscPayeeMapping(@PathVariable String id) {
		logger.info("Deleting a BCSC User to Payee Mapping for ID: {}", id);

		try {
			bcscPayeeMappingService.delete(id);		
			return ResponseEntity.noContent().build();
		} catch(Exception exception) {
			HttpStatus status = handleException(exception);
			throw new ResponseStatusException(status, exception.getMessage(), exception);
		}
	}
	
	private BcscPayeeMappingResponse mapEntityToRepsonse(BcscPayeeMapping newBcscPayeeMapping) {
		BcscPayeeMappingResponse bcscPayeeMappingResponse = new BcscPayeeMappingResponse();
		bcscPayeeMappingResponse.setBcscGuid(newBcscPayeeMapping.getBcscGuid());
		bcscPayeeMappingResponse.setPayeeNumber(newBcscPayeeMapping.getPayeeNumber());
		return bcscPayeeMappingResponse;
	}

	private BcscPayeeMapping mapRequestToEntity(BcscPayeeMappingRequest bcscPayeeMappingRequest) {
		BcscPayeeMapping bcscPayeeMapping = new BcscPayeeMapping();
		bcscPayeeMapping.setBcscGuid(bcscPayeeMappingRequest.getBcscGuid());
		bcscPayeeMapping.setPayeeNumber(bcscPayeeMappingRequest.getPayeeNumber());
		return bcscPayeeMapping;
	}	

	private HttpStatus handleException(Exception exception) {
		HttpStatus status;
		
		if (exception instanceof BcscPayeeMappingException) {
			BcscPayeeMappingException bpme = (BcscPayeeMappingException)exception;
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