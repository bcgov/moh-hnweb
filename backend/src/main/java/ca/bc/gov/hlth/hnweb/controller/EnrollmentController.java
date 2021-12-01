package ca.bc.gov.hlth.hnweb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.converter.XmlConverter;
import ca.bc.gov.hlth.hnweb.converter.MSHDefaults;
import ca.bc.gov.hlth.hnweb.converter.R50Converter;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberResponse;
import ca.bc.gov.hlth.hnweb.model.EnrollSubscriberRequest;
import ca.bc.gov.hlth.hnweb.model.R50Response;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsRequest;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.v2.message.R50;
import ca.bc.gov.hlth.hnweb.service.EnrollmentService;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

/**
 * Handles request related to R50 Enroll Subscriber. These will include:
 * <ul>
 * <li>Z03
 * <li>Z04
 * <li>Z05
 * <li>Z06
 * <ul>		 
 *
 */
@RequestMapping("/enrollment")
@RestController
public class EnrollmentController {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(EnrollmentController.class);

	@Autowired
	private EnrollmentService enrollmentService;
	
	@Autowired
	private MSHDefaults mshDefaults;
	
	@PostMapping("/enroll-subscriber")
	public ResponseEntity<EnrollSubscriberResponse> enrollSubscriber(@Valid @RequestBody EnrollSubscriberRequest enrollSubscriberRequest) throws HL7Exception, IOException {
		
		logger.info("Subscriber enroll request: {} ", enrollSubscriberRequest.getPhn());
		
		R50Converter converter = new R50Converter(mshDefaults);
		R50 r50 = converter.convertRequest(enrollSubscriberRequest);
		String transactionId = UUID.randomUUID().toString();		
		Message r50Message = enrollmentService.enrollSubscriber(r50, transactionId);		
		R50Response r50Response = converter.convertResponse(r50Message);
		EnrollSubscriberResponse enrollSubscriberResponse = converter.buildEnrollSubscribeResponse(r50Response);
		ResponseEntity<EnrollSubscriberResponse> responseEntity = ResponseEntity.ok(enrollSubscriberResponse);
		
		logger.info("Subscriber enroll Response: {} ", r50Response.getAcknowledgementMessage());
		
		return responseEntity;
	}
	
	@PostMapping("/person-details")
	public ResponseEntity<GetPersonDetailsResponse> getDemographicDetails(@Valid @RequestBody GetPersonDetailsRequest requestObj) throws HL7Exception, IOException {
		
		logger.info("Demographic request: {} ", requestObj.getPhn());
			
		String transactionId = UUID.randomUUID().toString();
		XmlConverter converter = new XmlConverter();
		
		String xmlString = converter.convertRequest(requestObj.getPhn());
		ResponseEntity<String> demoGraphicsResponse = enrollmentService.getDemographics(xmlString, transactionId);	
		
		//getDemoGraphicsResponse();
		GetPersonDetailsResponse personDetails = converter.convertResponse(demoGraphicsResponse.getBody());
		ResponseEntity<GetPersonDetailsResponse> responseEntity = ResponseEntity.ok(personDetails);
		
		return responseEntity;
	}
	
	private String getDemoGraphicsResponse() throws IOException {
		// our XML file for this example
		File xmlFile = new File("src\\main\\resources\\GetDemographicsResponse.xml");
		        
		 // Let's get XML file as String using BufferedReader
		 // FileReader uses platform's default character encoding
		 // if you need to specify a different encoding, 
		 // use InputStreamReader
		 Reader fileReader;
				
		 fileReader = new FileReader(xmlFile);
				
		 BufferedReader bufReader = new BufferedReader(fileReader);
		        
		 StringBuilder sb = new StringBuilder();
		 String line = bufReader.readLine();
		 while( line != null){
		      sb.append(line).append("\n");
		      line = bufReader.readLine();
		  }
		 String xml2String = sb.toString();
		 logger.info("XML to String using BufferedReader : ");
		 //logger.info(xml2String);
		        
		  bufReader.close();
		        
		  return xml2String;
	}
	
	
}
