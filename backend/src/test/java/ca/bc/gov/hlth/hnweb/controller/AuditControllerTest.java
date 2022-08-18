package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(2, auditReport.getBody().getRecords().size());

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
		
		Transaction transaction1 = new Transaction();

		transaction1.setOrganization("00000010");
		transaction1.setServer("server1");
		transaction1.setSessionId("123456");
		transaction1.setSourceIp("0:0:0:0:0:0:0:1");
		transaction1.setTransactionId(UUID.randomUUID());
		Date transactionDate1 = new GregorianCalendar(2022, 7, 5).getTime();
		transaction1.setStartTime(transactionDate1);
		transaction1.setType(TransactionType.CHECK_ELIGIBILITY.name());
		transaction1.setUserId("test_user");
		transactionRepository.save(transaction1);

		AffectedParty affectedParty1 = new AffectedParty();
		affectedParty1.setIdentifier(IdentifierType.PHN.name());
		affectedParty1.setIdentifierType(IdentifierType.PHN.getValue());
		affectedParty1.setDirection(AffectedPartyDirection.INBOUND.getValue());
		affectedParty1.setTransaction(transaction1);
	
		affectedPartyRepository.save(affectedParty1);
	}

}
