package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nimbusds.jose.shaded.json.JSONArray;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.security.TransactionType;

public class AuditReportControllerTest extends BaseControllerTest {

	@Autowired
	private AuditReportController auditReportController;

	@Test
	public void testGetOrganization() throws Exception {

		ResponseEntity<JSONArray> organization = auditReportController.getOrganization();

		assertEquals(HttpStatus.OK, organization.getStatusCode());
		JSONArray jsonArray = organization.getBody();

		assertNotNull(jsonArray);

	}

	@Test
	public void testGetAuditReport_withoutOptionalParam() {
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("test");
		auditReportRequest.setStartDate(LocalDate.now());
		auditReportRequest.setEndDate(LocalDate.now());

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(0, auditReport.getBody().getAuditReports().size());

	}

	@Test
	public void testGetAuditReport_withOptionalParam() {
		List<String> types = new ArrayList<>();
		types.add(TransactionType.CHECK_ELIGIBILITY.name());
		types.add(TransactionType.PHN_INQUIRY.name());
		
		List<String> orgs = new ArrayList<>();
		orgs.add("00000010");
		orgs.add("00000020");
		
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("test");
		auditReportRequest.setOrganizations(orgs);
		auditReportRequest.setTransactionTypes(types);
		auditReportRequest.setStartDate(LocalDate.now());
		auditReportRequest.setEndDate(LocalDate.now());

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(0, auditReport.getBody().getAuditReports().size());

	}

}
