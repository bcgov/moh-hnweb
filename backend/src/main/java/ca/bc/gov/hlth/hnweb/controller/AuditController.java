package ca.bc.gov.hlth.hnweb.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditRecord;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.service.AuditService;

@RequestMapping("/audit")
@RestController
public class AuditController extends BaseController {

	private static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

	private static final Logger logger = LoggerFactory.getLogger(AuditController.class);

	@Autowired
	private AuditService auditService;

	/**
	 * Retrieves the audit record
	 * 
	 * @param auditReportRequest
	 * @param request
	 * @return List of audit records
	 */
	@PostMapping("/audit-report")
	public ResponseEntity<AuditReportResponse> getAuditReport(@Valid @RequestBody AuditReportRequest auditReportRequest,
			HttpServletRequest request) {
		List<AuditRecord> auditReport = convertReport(auditService.getAffectedParties(
				auditReportRequest.getTransactionTypes(), auditReportRequest.getOrganizations(),
				auditReportRequest.getUserId(), auditReportRequest.getStartDate(), auditReportRequest.getEndDate()));

		logger.info("Audit Report size : {}", auditReport.size());

		AuditReportResponse auditReportingResponse = new AuditReportResponse();
		auditReportingResponse.setRecords(auditReport);
		auditReportingResponse.setStatus(StatusEnum.SUCCESS);

		ResponseEntity<AuditReportResponse> responseEntity = ResponseEntity.ok(auditReportingResponse);

		return responseEntity;
	}

	/**
	 * Retrieves distinct organization
	 * 
	 * @return list of organization
	 */
	@GetMapping("/organizations")
	public ResponseEntity<List<String>> getOrganizations() {
		List<String> organization = convertOrganization(auditService.getOrganizations());
		ResponseEntity<List<String>> responseEntity = ResponseEntity.ok(organization);
		return responseEntity;
	}

	private List<AuditRecord> convertReport(List<AffectedParty> affectedParties) {
		List<AuditRecord> auditReportResponse = new ArrayList<>();
		affectedParties.forEach(affectedParty -> {
			AuditRecord model = new AuditRecord();
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

	@PostMapping("/download-report")
	public ResponseEntity<Resource> downloadReport(@Valid @RequestBody AuditReportRequest auditReportRequest,
			HttpServletRequest request) {

		DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		String currentDateTime = dateFormatter.format(new Date());

		List<AffectedParty> affectedPartiesForDownload = auditService.getAffectedPartiesForDownload(
				auditReportRequest.getTransactionTypes(), auditReportRequest.getOrganizations(),
				auditReportRequest.getUserId(), auditReportRequest.getStartDate(), auditReportRequest.getEndDate());
		List<AuditRecord> auditReport = convertReport(affectedPartiesForDownload);

		final InputStreamResource resource = new InputStreamResource(auditService.load(auditReport));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit_" + currentDateTime + ".csv")
				.contentType(MediaType.parseMediaType("application/csv")).body(resource);

	}

	private List<String> convertOrganization(List<Organization> organizations) {
		List<String> orgianizations = new ArrayList<>();

		organizations.forEach(organization -> {
			if (organization != null) {
				orgianizations.add(organization.getOrganization());
			}
		});
		return orgianizations;
	}

	private LocalDateTime convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
