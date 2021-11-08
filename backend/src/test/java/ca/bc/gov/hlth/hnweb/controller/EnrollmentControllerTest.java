package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.uhn.hl7v2.HL7Exception;

@SpringBootTest
public class EnrollmentControllerTest {

	@Autowired
	private EnrollmentController enrollmentController;
	
	@Test
	public void testEnrollSubscriber_Error() throws HL7Exception, IOException {
		EnrollSubscriberRequest enrollSubscriberRequest = createEnrollSubscriberRequest();
		EnrollSubscriberResponse enrollSubscriberResponse = enrollmentController.enrollSubscriber(enrollSubscriberRequest);
		
		assertEquals("AE", enrollSubscriberResponse.getAcknowledgementCode());
	}

	private EnrollSubscriberRequest createEnrollSubscriberRequest() {
		EnrollSubscriberRequest enrollSubscriberRequest = new EnrollSubscriberRequest();
		enrollSubscriberRequest.setPhn("123456789");
		enrollSubscriberRequest.setSurname("Test");
		enrollSubscriberRequest.setGender("M");
		return enrollSubscriberRequest;
	}

}
