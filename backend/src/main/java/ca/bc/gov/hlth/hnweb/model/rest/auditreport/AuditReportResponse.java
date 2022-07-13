package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class AuditReportResponse extends BaseResponse {

	private List<AuditReport> auditReports = new ArrayList<>();

	public List<AuditReport> getAuditReports() {
		return auditReports;
	}

	public void setAuditReports(List<AuditReport> auditReports) {
		this.auditReports = auditReports;
	}
	
	@Override
	public String toString() {
		return "AuditReportResponse [auditReports=" + auditReports + "]";
	}
}
