package ca.bc.gov.hlth.hnweb.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;
import ca.bc.gov.hlth.hnweb.service.EligibilityService;

@RequestMapping("/eligibility")
@RestController
public class EligibilityController {
	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	@Autowired
	private EligibilityService eligibilityService;
	
	@GetMapping("/checkEligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@RequestParam(name = "phn", required = true) String phn,
			@DateTimeFormat(iso = ISO.DATE) @RequestParam(name = "eligibilityDate", required = false) Date eligibilityDate) {
		logger.info("checkEligibility request - phn: {} date: {}", phn, eligibilityDate);

		try {
			CheckEligibilityResponse checkEligibilityResponse = eligibilityService.checkEligibility(phn, eligibilityDate);
			
			ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);
		
			logger.info("checkEligibility response: {} ", checkEligibilityResponse);
			return response;	
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /checkEligibility request", e);
		}
		
	}

}
