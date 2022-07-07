package ca.bc.gov.hlth.hnweb.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReport;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.service.AuditService;

@RequestMapping("/audit-reports")
@RestController
public class AuditReportController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditReportController.class);

	@Autowired
	private AuditService auditService;

	/**
	 * Retrieves the audit record
	 * @param auditReportRequest
	 * @param request
	 * @return List of audit records
	 */
	@PostMapping("/get-audit-report")
	public ResponseEntity<AuditReportResponse> getAuditReport(
			@Valid @RequestBody AuditReportRequest auditReportRequest, HttpServletRequest request) {
		List<AuditReport> auditReport = convertReport(auditService.getAuditReport(
				auditReportRequest.getTransactionType(), auditReportRequest.getOrganization(),
				auditReportRequest.getUserId(), auditReportRequest.getStartDate(), auditReportRequest.getEndDate()));
		
		AuditReportResponse auditReportingResponse = new AuditReportResponse();
		auditReportingResponse.setAuditReports(auditReport);
		auditReportingResponse.setStatus(StatusEnum.SUCCESS);	
		
		ResponseEntity<AuditReportResponse> responseEntity = ResponseEntity.ok(auditReportingResponse);
		
		return responseEntity;
	}

	/**
	 * Retrieves distinct organization
	 * @return array of organization
	 */
	@GetMapping("/get-organization")
	public ResponseEntity<JSONArray> getOrganization() {
		JSONArray organization = convertOrganization(auditService.getOrganization());
		ResponseEntity<JSONArray> responseEntity = ResponseEntity.ok(organization);
		return responseEntity;
	}

	private List<AuditReport> convertReport(List<AffectedParty> affectedParties) {
		List<AuditReport> auditReportResponse = new ArrayList<>();
		affectedParties.forEach(affectedParty -> {
			AuditReport model = new AuditReport();
			model.setOrganization(affectedParty.getTransaction().getOrganization());
			model.setTransactionId(affectedParty.getTransaction().getTransactionId().toString());
			model.setType(affectedParty.getTransaction().getType());
			model.setUserId(affectedParty.getTransaction().getUserId());
			model.setAffectedPartyId(affectedParty.getIdentifier());
			model.setAffectedPartyType(affectedParty.getIdentifierType());		
			model.setTransactionStartTime(convertDate(affectedParty.getTransaction().getStartTime()));

			auditReportResponse.add(model);

		});
		return auditReportResponse;
	}

	private JSONArray convertOrganization(List<Organization> organizations) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		organizations.forEach(organization -> {			
			if (organization != null) {
				jsonObject.put("text", organization.getOrganization());
				jsonObject.put("value", organization.getOrganization());
			}
		});

		jsonArray.add(jsonObject);
		return jsonArray;
	}
	
	private LocalDate convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
