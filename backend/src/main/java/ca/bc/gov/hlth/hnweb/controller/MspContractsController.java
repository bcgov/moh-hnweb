package ca.bc.gov.hlth.hnweb.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.converter.rapid.RPBSPMC0Converter;
import ca.bc.gov.hlth.hnweb.exception.HNWebException;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;
import ca.bc.gov.hlth.hnweb.service.MspContractsService;

/**
 * Handle requests related to MSP Contracts.
 *
 */
@RequestMapping("/msp-contracts")
@RestController
public class MspContractsController {

	private static final Logger logger = LoggerFactory.getLogger(MspContractsController.class);

	@Autowired
	private MspContractsService mspContractsService;

	/**
	 * Get Contracts Periods for a Personal Health Number (PHN) Inquiry
	 * Maps to the legacy R32.
	 *  
	 * @param getContractPeriodsRequest
	 * @return The result of the operation.
	 */
	@PostMapping("/get-contract-periods")
	public ResponseEntity<GetContractPeriodsResponse> getContractPeriods(@Valid @RequestBody GetContractPeriodsRequest getContractPeriodsRequest) {

		try {
			RPBSPMC0Converter converter = new RPBSPMC0Converter();
			RPBSPMC0 rpbspmc0Request = converter.convertRequest(getContractPeriodsRequest);
			RPBSPMC0 rpbspmc0Response = mspContractsService.getContractPeriods(rpbspmc0Request);
			GetContractPeriodsResponse getContractPeriodsResponse = converter.convertResponse(rpbspmc0Response);
					
			ResponseEntity<GetContractPeriodsResponse> response = ResponseEntity.ok(getContractPeriodsResponse);

			logger.info("getContractPeriodsResponse response: {} ", getContractPeriodsResponse);
			return response;
		} catch (HNWebException hwe) {
			switch (hwe.getType()) {
			case DOWNSTREAM_FAILURE:
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, hwe.getMessage(), hwe);
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /add-dependent request", hwe);				
			}
		} catch (WebClientException wce) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, wce.getMessage(), wce);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /add-dependent request", e);
		}		
	}

}