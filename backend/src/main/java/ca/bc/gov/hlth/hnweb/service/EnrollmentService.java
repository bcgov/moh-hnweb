package ca.bc.gov.hlth.hnweb.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service for processing enrollment requests 
 *
 */
@Service
public class EnrollmentService {

	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EnrollmentService.class);

	public String enrollSubscriber(String v2) {
		
		LOGGER.debug("Enroll subscriber");

	    //TODO Add the code to send to external endpoint
		
		//Return stubbed response.
		return "MSH|^~\\&|RAIPRSN-NM-SRCH|BC00002041|HNWeb|moh_hnclient_dev|20211013124847.746-0700||ACK|71902|D|2.4\r\n" + 
				"MSA|AE|20191108082211|NHR529E^SEVERE SYSTEM ERROR\r\n" + 
				"ERR|^^^NHR529E";
	}
		
}
