package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditOrganization;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditRecord;
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
		ResponseEntity<List<AuditOrganization>> response = auditReportController.getOrganizations();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<AuditOrganization> orgs = response.getBody();

		assertNotNull(orgs);
		// Check the number of valid records
		assertEquals(1, orgs.size());
		
		AuditOrganization org = orgs.get(0);

		assertEquals("00000010", org.getId());
		assertEquals("Ministry of Health", org.getName());
	}
	
	@Test
	public void testGetOrganization_multiple() throws Exception {
		createOrganization();
		
		// Create an additional "older" organization without the Name captured
		Organization orgNoName = new Organization();
		orgNoName.setOrganization("00000020");
		orgNoName.setOrganizationName(null);
		organizationRepository.save(orgNoName);
		
		ResponseEntity<List<AuditOrganization>> response = auditReportController.getOrganizations();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<AuditOrganization> orgs = response.getBody();

		assertNotNull(orgs);
		// Check the number of valid records
		assertEquals(2, orgs.size());
		
		AuditOrganization org1 = orgs.get(0);

		assertEquals("00000010", org1.getId());
		assertEquals("Ministry of Health", org1.getName());
		
		AuditOrganization org2 = orgs.get(1);

		assertEquals("00000020", org2.getId());
		assertNull(org2.getName());
	}

	@Test
	public void testGetAuditReport_withoutOptionalParam() {
		createAuditReports(2, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(2, auditReport.getBody().getRecords().size());

	}

	@Test
	public void testGetAuditReport_withOptionalParam() {
		createAuditReports(1, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.getValue());
		types.add(TransactionType.PHN_INQUIRY.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(1, auditReport.getBody().getRecords().size());

	}
	
	@Test
	public void testGetAuditReports_spgNoResults() {
		createAuditReports(15, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("E45");
		
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(0, auditReport.getBody().getRecords().size());

	}

	@Test
	public void testGetAuditReports_firstPage() {
		createAuditReports(15, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(10, auditReport.getBody().getRecords().size());
	}

	@Test
	public void testGetAuditReports_secondPage() {
		createAuditReports(15, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(1);
		auditReportRequest.setRows(10);

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(5, auditReport.getBody().getRecords().size());
	}

	@Test
	public void testGetAuditReports_sortAsc() {
		createAuditReports(5, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		createAuditReports(5, TransactionType.PHN_INQUIRY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);
		auditReportRequest.setSortDirection("ASC");
		auditReportRequest.setSortField("type");

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());

		List<AuditRecord> records = auditReport.getBody().getRecords();
		assertEquals(10, records.size());
		assertEquals(TransactionType.CHECK_ELIGIBILITY.getValue(), records.get(0).getType());
	}

	@Test
	public void testGetAuditReports_sortDesc() {
		createAuditReports(5, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		createAuditReports(5, TransactionType.PHN_INQUIRY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setPage(0);
		auditReportRequest.setRows(10);
		auditReportRequest.setSortDirection("DESC");
		auditReportRequest.setSortField("type");

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());

		List<AuditRecord> records = auditReport.getBody().getRecords();
		assertEquals(10, records.size());
		assertEquals(TransactionType.PHN_INQUIRY.getValue(), records.get(0).getType());
	}

	@Test
	public void testGetAuditReport_downloadCSV() throws IOException {
		createAuditReports(20, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		createAuditReports(20, TransactionType.PHN_INQUIRY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setSortDirection("ASC");
		auditReportRequest.setSortField("type");

		ResponseEntity<Resource> downloadReport = auditReportController.downloadAuditReport(auditReportRequest,
				createHttpServletRequest());
		 
		 List<String> reportData = new ArrayList<String>();
		 InputStream downloadData = downloadReport.getBody().getInputStream();
		 read(downloadData, reportData);
				 
		 assertEquals(HttpStatus.OK, downloadReport.getStatusCode());

		 //Check csv headers and data
		 assertEquals("Type", reportData.get(0));
		 assertEquals("Organization", reportData.get(1));
		 assertEquals("Organization Name", reportData.get(2));
		 assertEquals("SPG", reportData.get(3));
		 assertEquals("User ID", reportData.get(4));
		 assertEquals("Transaction Start Time", reportData.get(5));
		 assertEquals("Affected Party ID", reportData.get(6));
		 assertEquals("Affected Party ID Type", reportData.get(7));
		 assertEquals("Transaction ID\r\n" + 
		 		"CheckEligibility", reportData.get(8));		 		 
		 assertEquals("00000010", reportData.get(9));
		 assertEquals("Ministry of Health", reportData.get(10));
		 assertEquals("TRAININGHEALTHAUTH", reportData.get(11));
		 assertEquals("hnweb1", reportData.get(12));
		 assertEquals("2022-08-05T00:00:00", reportData.get(13));
		 assertEquals("800000001", reportData.get(14));
		 assertEquals("PHN", reportData.get(15));
		 
		 //Check the last (40th) record
		 assertEquals("00000010", reportData.get(321));
		 assertEquals("Ministry of Health", reportData.get(322));
		 assertEquals("TRAININGHEALTHAUTH", reportData.get(323));
		 assertEquals("hnweb1", reportData.get(324));
		 assertEquals("2022-08-05T00:00:00", reportData.get(325));
		 assertEquals("800000001", reportData.get(326));
		 assertEquals("PHN", reportData.get(327));
	}

	@Test
	public void testGetAuditReport_downloadCSV_sortDesc() throws IOException {
		createAuditReports(20, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
		createAuditReports(20, TransactionType.PHN_INQUIRY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");

		List<String> spgRoles = new ArrayList<>();
		spgRoles.add("TRAININGHEALTHAUTH");

		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("hnweb1");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setSpgRoles(spgRoles);
		auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
		auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
		auditReportRequest.setSortDirection("DESC");
		auditReportRequest.setSortField("type");

		ResponseEntity<Resource> downloadReport = auditReportController.downloadAuditReport(auditReportRequest,
				createHttpServletRequest());
		 
		 List<String> reportData = new ArrayList<String>();
		 InputStream downloadData = downloadReport.getBody().getInputStream();
		 read(downloadData, reportData);
				 
		 assertEquals(HttpStatus.OK, downloadReport.getStatusCode());

		 //Check csv headers and data
		 assertEquals("Type", reportData.get(0));
		 assertEquals("Organization", reportData.get(1));
		 assertEquals("Organization Name", reportData.get(2));
		 assertEquals("SPG", reportData.get(3));
		 assertEquals("User ID", reportData.get(4));
		 assertEquals("Transaction Start Time", reportData.get(5));
		 assertEquals("Affected Party ID", reportData.get(6));
		 assertEquals("Affected Party ID Type", reportData.get(7));
		 assertEquals("Transaction ID\r\n" + 
		 		"PHNInquiry", reportData.get(8));		 		 
		 assertEquals("00000010", reportData.get(9));
		 assertEquals("Ministry of Health", reportData.get(10));
		 assertEquals("TRAININGHEALTHAUTH", reportData.get(11));
		 assertEquals("hnweb1", reportData.get(12));
		 assertEquals("2022-08-05T00:00:00", reportData.get(13));
		 assertEquals("800000001", reportData.get(14));
		 assertEquals("PHN", reportData.get(15));
		 
		 //Check the last (40th) record
		 assertEquals("00000010", reportData.get(321));
		 assertEquals("Ministry of Health", reportData.get(322));
		 assertEquals("TRAININGHEALTHAUTH", reportData.get(323));
		 assertEquals("hnweb1", reportData.get(324));
		 assertEquals("2022-08-05T00:00:00", reportData.get(325));
		 assertEquals("800000001", reportData.get(326));
		 assertEquals("PHN", reportData.get(327));

	}

    @Test
    public void testGetAuditReport_outboundForEnrollSubscriber() throws IOException {
        createAuditReports(2, TransactionType.ENROLL_SUBSCRIBER, IdentifierType.PHN.getValue(), AffectedPartyDirection.OUTBOUND.getValue());

        List<String> orgs = new ArrayList<>();
        orgs.add("00000010");

        List<String> spgRoles = new ArrayList<>();
        spgRoles.add("TRAININGHEALTHAUTH");

        AuditReportRequest auditReportRequest = new AuditReportRequest();
        auditReportRequest.setUserId("hnweb1");
        auditReportRequest.setOrganizations(orgs);
        auditReportRequest.setSpgRoles(spgRoles);
        auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
        auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
        auditReportRequest.setPage(0);
        auditReportRequest.setRows(10);
        auditReportRequest.setSortDirection("DESC");
        auditReportRequest.setSortField("type");

        ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
                createHttpServletRequest());

        assertEquals(HttpStatus.OK, auditReport.getStatusCode());

        List<AuditRecord> records = auditReport.getBody().getRecords();
        assertEquals(2, records.size());
        assertEquals(TransactionType.ENROLL_SUBSCRIBER.getValue(), records.get(0).getType());
        assertEquals(IdentifierType.PHN.getValue(), records.get(0).getAffectedPartyType());

    }

    @Test
    public void testGetAuditReport_outboundAndInbound() throws IOException {
        createAuditReports(2, TransactionType.ENROLL_SUBSCRIBER, IdentifierType.PHN.getValue(), AffectedPartyDirection.OUTBOUND.getValue());
        createAuditReports(1, TransactionType.ENROLL_SUBSCRIBER, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
        createAuditReports(3, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

        List<String> orgs = new ArrayList<>();
        orgs.add("00000010");

        List<String> spgRoles = new ArrayList<>();
        spgRoles.add("TRAININGHEALTHAUTH");

        List<String> types = new ArrayList<>();
        types.add(TransactionType.ENROLL_SUBSCRIBER.getValue());

        AuditReportRequest auditReportRequest = new AuditReportRequest();
        auditReportRequest.setUserId("hnweb1");
        auditReportRequest.setOrganizations(orgs);
        auditReportRequest.setSpgRoles(spgRoles);
        auditReportRequest.setTransactionTypes(types);
        auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
        auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
        auditReportRequest.setPage(0);
        auditReportRequest.setRows(10);
        auditReportRequest.setSortDirection("DESC");
        auditReportRequest.setSortField("type");

        ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
                createHttpServletRequest());

        assertEquals(HttpStatus.OK, auditReport.getStatusCode());

        List<AuditRecord> records = auditReport.getBody().getRecords();
        assertEquals(3, records.size());

    }

    @Test
    public void testGetAuditReport_outboundAndInboundAll() throws IOException {
        createAuditReports(2, TransactionType.ENROLL_SUBSCRIBER, IdentifierType.PHN.getValue(), AffectedPartyDirection.OUTBOUND.getValue());
        createAuditReports(1, TransactionType.ENROLL_SUBSCRIBER, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());
        createAuditReports(3, TransactionType.CHECK_ELIGIBILITY, IdentifierType.PHN.getValue(), AffectedPartyDirection.INBOUND.getValue());

        List<String> orgs = new ArrayList<>();
        orgs.add("00000010");

        List<String> spgRoles = new ArrayList<>();
        spgRoles.add("TRAININGHEALTHAUTH");

        AuditReportRequest auditReportRequest = new AuditReportRequest();
        auditReportRequest.setUserId("hnweb1");
        auditReportRequest.setOrganizations(orgs);
        auditReportRequest.setSpgRoles(spgRoles);
        auditReportRequest.setStartDate(LocalDate.of(2022, 7, 1));
        auditReportRequest.setEndDate(LocalDate.of(2022, 12, 8));
        auditReportRequest.setPage(0);
        auditReportRequest.setRows(10);
        auditReportRequest.setSortDirection("DESC");
        auditReportRequest.setSortField("type");

        ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
                createHttpServletRequest());

        assertEquals(HttpStatus.OK, auditReport.getStatusCode());

        List<AuditRecord> records = auditReport.getBody().getRecords();
        assertEquals(6, records.size());

    }

	private void read(InputStream input, List<String> reportData) throws IOException {

		StringBuilder builder = new StringBuilder();

		int data = input.read();
		while (data != -1) {
			if (((char) data) != ',') {
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
		org.setOrganizationName("Ministry of Health");
		organizationRepository.save(org);

	}

	private void createAuditReports(int count, TransactionType transactionType, String identifierType, String direction) {
		for (int i = 0; i < count; i++) {
			Transaction transaction = new Transaction();

			transaction.setOrganization("00000010");
			transaction.setOrganizationName("Ministry of Health");
			transaction.setSpgRole("TRAININGHEALTHAUTH");
			transaction.setServer("server1");
			transaction.setSessionId("123456");
			transaction.setSourceIp("0:0:0:0:0:0:0:1");
			transaction.setTransactionId(UUID.randomUUID());
			Date transactionDate = new GregorianCalendar(2022, 7, 5).getTime();
			transaction.setStartTime(transactionDate);
			transaction.setType(transactionType.getValue());
			transaction.setUserId("hnweb1");
			transactionRepository.save(transaction);

			AffectedParty affectedParty = new AffectedParty();
			affectedParty.setIdentifier("800000001");
			affectedParty.setIdentifierType(identifierType);
			affectedParty.setDirection(direction);
			affectedParty.setTransaction(transaction);

			affectedPartyRepository.save(affectedParty);
		}
	}

}
