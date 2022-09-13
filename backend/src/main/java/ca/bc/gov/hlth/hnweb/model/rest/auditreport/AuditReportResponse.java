package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.hnweb.model.rest.BaseResponse;

public class AuditReportResponse extends BaseResponse {

	private List<AuditRecord> records = new ArrayList<>();

	private Long totalRecords;

	public List<AuditRecord> getRecords() {
		return records;
	}

	public void setRecords(List<AuditRecord> auditReports) {
		this.records = auditReports;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		return "AuditReportResponse [auditReport=" + records + "]";
	}
}
