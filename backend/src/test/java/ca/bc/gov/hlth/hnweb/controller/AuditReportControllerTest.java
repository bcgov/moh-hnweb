package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

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
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		auditReportRequest.setUserId("test");
		auditReportRequest.setOrganization("00000010");
		auditReportRequest.setTransactionType(TransactionType.CHECK_ELIGIBILITY.name());
		auditReportRequest.setStartDate(LocalDate.now());
		auditReportRequest.setEndDate(LocalDate.now());

		ResponseEntity<AuditReportResponse> auditReport = auditReportController.getAuditReport(auditReportRequest,
				createHttpServletRequest());

		assertEquals(HttpStatus.OK, auditReport.getStatusCode());
		assertEquals(0, auditReport.getBody().getAuditReports().size());

	}

}
