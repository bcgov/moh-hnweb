package ca.bc.gov.hlth.hnweb.controller;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;

@RequestMapping("/eligibility")
@RestController
public class EligibilityController {
	private static final Logger logger = LoggerFactory.getLogger(EligibilityController.class);

	@GetMapping("/checkEligibility")
	public ResponseEntity<CheckEligibilityResponse> checkEligibility(@RequestParam(name = "phn", required = true) String phn,
			@DateTimeFormat(iso = ISO.DATE) @RequestParam(name = "eligibilityDate", required = false) Date eligibilityDate) {
		logger.info("checkEligibility request - phn: {} date: {}", phn, eligibilityDate);
		
		CheckEligibilityResponse checkEligibilityResponse = new CheckEligibilityResponse();
		checkEligibilityResponse.setBeneficiaryOnDateChecked(Boolean.TRUE);
		checkEligibilityResponse.setCoverageEndDate(DateUtils.addDays(new Date(), 90));
		checkEligibilityResponse.setPhn(phn);
		checkEligibilityResponse.setReason("Some reason or other");
		checkEligibilityResponse.setExclusionPeriodEndDate(DateUtils.addDays(new Date(), 30));
		
		ResponseEntity<CheckEligibilityResponse> response = ResponseEntity.ok(checkEligibilityResponse);
	
		logger.info("checkEligibility response: {} ", checkEligibilityResponse);
		return response;
	}

}
