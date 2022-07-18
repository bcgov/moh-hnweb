package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class AuditReportResponse extends BaseResponse {

	private List<AuditRecord> records = new ArrayList<>();

	public List<AuditRecord> getRecords() {
		return records;
	}

	public void setRecords(List<AuditRecord> auditReports) {
		this.records = auditReports;
	}
	
	@Override
	public String toString() {
		return "AuditReportResponse [auditReport=" + records + "]";
	}
}
