package ca.bc.gov.hlth.hnweb.controller;

import static ca.bc.gov.hlth.hnweb.service.AuditService.DATE_TIME_FORMAT;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditOrganization;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditRecord;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.service.AuditService;

@RequestMapping("/audit")
@RestController
public class AuditController extends BaseController {

	private static final String AUDIT_REPORT_PREFIX = "auditreport_";

	private static final String AUDIT_REPORT_EXTENSION = ".csv";

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

		Page<AffectedParty> pageable = auditService.getAffectedParties(auditReportRequest.getTransactionTypes(),
				auditReportRequest.getOrganizations(), auditReportRequest.getSpgRoles(), auditReportRequest.getUserId(),
				auditReportRequest.getStartDate(), auditReportRequest.getEndDate(), auditReportRequest.getPage(),
				auditReportRequest.getRows(), auditReportRequest.getSortField(), auditReportRequest.getSortDirection());
		List<AuditRecord> auditReport = convertReport(pageable.getContent());

		int first = auditReportRequest.getPage() * auditReportRequest.getRows();
		logger.info("Returning {}-{} of {} audit records", first, first + pageable.getNumberOfElements(),
				pageable.getTotalElements());

		AuditReportResponse auditReportingResponse = new AuditReportResponse();
		auditReportingResponse.setRecords(auditReport);
		auditReportingResponse.setTotalRecords(pageable.getTotalElements());
		auditReportingResponse.setStatus(StatusEnum.SUCCESS);

		ResponseEntity<AuditReportResponse> responseEntity = ResponseEntity.ok(auditReportingResponse);

		return responseEntity;
	}

	/**
	 * Retrieves distinct organizations.
	 * 
	 * @return list of organization
	 */
	@GetMapping("/organizations")
	public ResponseEntity<List<AuditOrganization>> getOrganizations() {
		List<Organization> organizations = auditService.getOrganizations();
		List<AuditOrganization> auditOrganizations = organizations.stream()
                .map(o -> new AuditOrganization(o.getOrganization(), o.getOrganizationName()))
                .collect(Collectors.toList());

		return ResponseEntity.ok(auditOrganizations);
	}

	/**
	 * Retrieves audit records for download
	 * 
	 * @param auditReportRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/download-report")
	public ResponseEntity<Resource> downloadAuditReport(@Valid @RequestBody AuditReportRequest auditReportRequest,
			HttpServletRequest request) {

		List<AffectedParty> affectedPartiesForDownload = auditService.getAffectedPartiesForDownload(
				auditReportRequest.getTransactionTypes(), auditReportRequest.getOrganizations(),
				auditReportRequest.getSpgRoles(), auditReportRequest.getUserId(), auditReportRequest.getStartDate(),
				auditReportRequest.getEndDate(), auditReportRequest.getSortField(),
				auditReportRequest.getSortDirection());
		List<AuditRecord> auditReport = convertReport(affectedPartiesForDownload);
		logger.info("Number of records returned for download : {}", auditReport.size());

		InputStreamResource resource = new InputStreamResource(auditService.load(auditReport));

		String currentDateTime = new SimpleDateFormat(DATE_TIME_FORMAT).format(new Date());
		String fileName = AUDIT_REPORT_PREFIX + currentDateTime + AUDIT_REPORT_EXTENSION;

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName))
				.contentType(MediaType.parseMediaType("application/csv")).body(resource);

	}

	private List<AuditRecord> convertReport(List<AffectedParty> affectedParties) {
		List<AuditRecord> auditReportResponse = new ArrayList<>();
		affectedParties.forEach(affectedParty -> {
			AuditRecord model = new AuditRecord();
			model.setOrganization(affectedParty.getTransaction().getOrganization());
			model.setOrganizationName(affectedParty.getTransaction().getOrganizationName());
			model.setSpgRole(affectedParty.getTransaction().getSpgRole());
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

	private LocalDateTime convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
