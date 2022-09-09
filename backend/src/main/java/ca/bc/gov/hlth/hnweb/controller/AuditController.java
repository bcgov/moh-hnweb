package ca.bc.gov.hlth.hnweb.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.util.StringUtils;

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

		Page<AffectedParty> pageable = auditService.getAffectedParties(
				auditReportRequest.getTransactionTypes(), auditReportRequest.getOrganizations(),
				auditReportRequest.getUserId(), auditReportRequest.getStartDate(), auditReportRequest.getEndDate(),
				auditReportRequest.getPage(), auditReportRequest.getRows(), auditReportRequest.getSortField(), auditReportRequest.getSortDirection());
		List<AuditRecord> auditReport = convertReport(pageable.getContent());

		int first = auditReportRequest.getPage() * auditReportRequest.getRows();
		logger.info("Returning {}-{} of {} audit records", first, first + pageable.getNumberOfElements(), pageable.getTotalElements());

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
	public ResponseEntity<List<String>> getOrganizations() {
		List<String> organizations = convertOrganization(auditService.getOrganizations());
		ResponseEntity<List<String>> responseEntity = ResponseEntity.ok(organizations);
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

	private List<String> convertOrganization(List<Organization> organizations) {
		return organizations.stream().filter(org -> StringUtils.isNotBlank(org.getOrganization())).map(org -> org.getOrganization()).collect(Collectors.toList());
	}

	private LocalDateTime convertDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}
