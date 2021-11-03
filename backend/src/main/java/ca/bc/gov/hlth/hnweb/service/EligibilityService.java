package ca.bc.gov.hlth.hnweb.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.model.CheckEligibilityResponse;

@Service
public class EligibilityService {
	
	/**
	 * TODO (weskubo-cgi) This is just a stubbed out service for unit testing.
	 * The real service will integrated with a downstream application and return
	 * HL7 objects.
	 * @return
	 */
	public CheckEligibilityResponse checkEligibility(String phn, Date eligibilityDate) {
		CheckEligibilityResponse checkEligibilityResponse = new CheckEligibilityResponse();
		checkEligibilityResponse.setBeneficiaryOnDateChecked(Boolean.TRUE);
		checkEligibilityResponse.setCoverageEndDate(DateUtils.addDays(new Date(), 90));
		checkEligibilityResponse.setPhn(phn);
		checkEligibilityResponse.setReason("Some reason or other");
		checkEligibilityResponse.setExclusionPeriodEndDate(DateUtils.addDays(new Date(), 30));
		
		return checkEligibilityResponse;
	}

}
