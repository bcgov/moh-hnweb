package ca.bc.gov.hlth.hnweb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportRequest;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportResponse;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.AuditReportingResponse;
import ca.bc.gov.hlth.hnweb.model.rest.auditreport.OrganizationModel;
import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;
import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.service.AuditService;

@RequestMapping("/audit-reports")
@RestController
public class AuditReportController extends BaseController {

	@Autowired
	private AuditService auditService;

	@PostMapping("/get-audit-report")
	public ResponseEntity<AuditReportingResponse> getAuditReport(
			@Valid @RequestBody AuditReportRequest auditReportRequest, HttpServletRequest request) {
		List<AuditReportResponse> auditReport = convertReport(auditService
				.getAuditReport(auditReportRequest.getTransactionType(), auditReportRequest.getOrganization()));
		AuditReportingResponse auditReportingResponse = new AuditReportingResponse();
		auditReportingResponse.setAuditReportResponse(auditReport);
		ResponseEntity<AuditReportingResponse> responseEntity = ResponseEntity.ok(auditReportingResponse);
		return responseEntity;
	}

	@PostMapping("/get-organization")
	public ResponseEntity<List<OrganizationModel>> getOrganization(@Valid @RequestBody AuditReportRequest auditReportRequest, HttpServletRequest request) {
		List<OrganizationModel> organization = convertOrganization(auditService.getOrganization());
		ResponseEntity<List<OrganizationModel>> responseEntity = ResponseEntity.ok(organization);
		return responseEntity;
	}

	private List<AuditReportResponse> convertReport(List<Transaction> transactions) {
		List<AuditReportResponse> auditReportResponse = new ArrayList<>();
		transactions.forEach(transaction -> {
			AuditReportResponse model = new AuditReportResponse();
			model.setOrganization(transaction.getOrganization());
			model.setTransactionId(transaction.getTransactionId().toString());
			model.setType(transaction.getType());
			
			System.out.println(transaction.getOrganization());

			auditReportResponse.add(model);

		});
		return auditReportResponse;
	}

	private List<OrganizationModel> convertOrganization(List<Organization> organizations) {
		List<OrganizationModel> organizationModel = new ArrayList<>();
		organizations.forEach(organization -> {
			OrganizationModel model = new OrganizationModel();
			if (organization != null) {
				model.setOrganizationId(organization.getOrganization());
				organizationModel.add(model);
			}
		});
		return organizationModel;
	}

}
