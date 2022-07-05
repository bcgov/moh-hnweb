package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class AuditReportingResponse extends BaseResponse {
		
	private List<AuditReportResponse> auditReportResponse = new ArrayList<>();

	public List<AuditReportResponse> getAuditReportResponse() {
		return auditReportResponse;
	}

	public void setAuditReportResponse(List<AuditReportResponse> auditReportResponse) {
		this.auditReportResponse = auditReportResponse;
	}
	
}
