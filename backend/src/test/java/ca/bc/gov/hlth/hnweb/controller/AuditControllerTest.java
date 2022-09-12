package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedPartyDirection;
import ca.bc.gov.hlth.hnweb.persistence.entity.IdentifierType;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.repository.AffectedPartyRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.OrganizationRepository;
import ca.bc.gov.hlth.hnweb.persistence.repository.TransactionRepository;
import ca.bc.gov.hlth.hnweb.security.TransactionType;

public class AuditControllerTest extends BaseControllerTest {

	@Autowired
	private AuditController auditReportController;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private AffectedPartyRepository affectedPartyRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void testGetOrganization() throws Exception {
		createOrganization();
		ResponseEntity<List<String>> organization = auditReportController.getOrganizations();

		assertEquals(HttpStatus.OK, organization.getStatusCode());
		List<String> orgs = organization.getBody();

		assertNotNull(orgs);
		// Check the number of valid records
		assertEquals(1, orgs.size());

	}

	@Test
	public void testGetAuditReport_withoutOptionalParam() {;
		createAuditReport();
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(1, auditReport.getBody().getRecords().size());

	}

	@Test
	public void testGetAuditReport_withOptionalParam() {
		createAuditReport();
		
		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.name());
		types.add(TransactionType.PHN_INQUIRY.name());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		
		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(1, auditReport.getBody().getRecords().size());

	}
	
	@Test
	public void testGetAuditReport_downloadCSV() throws IOException {
		createAuditReport();
		
		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.name());
		types.add(TransactionType.PHN_INQUIRY.name());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		
		 ResponseEntity<Resource> downloadReport = auditReportController.downloadReport(auditReportRequest,
				createHttpServletRequest());
		 
		 List<String> reportData = new ArrayList<String>();
		 InputStream downloadData = downloadReport.getBody().getInputStream();
		 read(downloadData, reportData);
				 
		 assertEquals(HttpStatus.OK, downloadReport.getStatusCode());

		 //Check csv headers and data
		 assertEquals("Type", reportData.get(0));
		 assertEquals("Organization", reportData.get(1));
		 assertEquals("User ID", reportData.get(2));
		 assertEquals("Transaction Start Time", reportData.get(3));
		 assertEquals("Affected Party ID", reportData.get(4));
		 assertEquals("Affected Party ID Type", reportData.get(5));
		 assertEquals("Transaction ID\r\n" + 
		 		"CHECK_ELIGIBILITY", reportData.get(6));		 		 
		 assertEquals("00000010", reportData.get(7));
		 assertEquals("hnweb1", reportData.get(8));
		 assertEquals("2022-08-05 00:00:00", reportData.get(9));
		 assertEquals("PHN", reportData.get(10));
	}
	
	 private void read(InputStream input, List<String> reportData ) throws IOException {
		
	        StringBuilder builder = new StringBuilder();

	        int data = input.read();
	        while(data != -1){
	            if(((char)data) != ','){
	                builder.append((char) data);
	            } else {
	            	reportData.add(builder.toString().trim());
	                builder.delete(0, builder.length());
	            }

	            data = input.read();
	        }
	    }

	private void createOrganization() {
		Organization org = new Organization();
		org.setOrganization("00000010");
		organizationRepository.save(org);

	}

	private void createAuditReport() {
		Transaction transaction = new Transaction();

		transaction.setOrganization("00000010");
		transaction.setServer("server1");
		transaction.setSessionId("123456");
		transaction.setSourceIp("0:0:0:0:0:0:0:1");
		transaction.setTransactionId(UUID.randomUUID());
		Date transactionDate = new GregorianCalendar(2022, 7, 5).getTime();
		transaction.setStartTime(transactionDate);
		transaction.setType(TransactionType.CHECK_ELIGIBILITY.name());
		transaction.setUserId("hnweb1");
		transactionRepository.save(transaction);

		AffectedParty affectedParty = new AffectedParty();
		affectedParty.setIdentifier(IdentifierType.PHN.name());
		affectedParty.setIdentifierType(IdentifierType.PHN.getValue());
		affectedParty.setDirection(AffectedPartyDirection.INBOUND.getValue());
		affectedParty.setTransaction(transaction);
	
		affectedPartyRepository.save(affectedParty);
	}

}
