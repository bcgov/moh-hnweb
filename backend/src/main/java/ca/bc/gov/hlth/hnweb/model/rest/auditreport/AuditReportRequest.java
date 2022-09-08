package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.time.LocalDate;
import java.util.List;

public class AuditReportRequest {

	private String userId;

	private List<String> organizations;

	private List<String> transactionTypes;

	private LocalDate startDate;

	private LocalDate endDate;

	private Integer page = 0;

	private Integer rows = 10;

	private String sortField;

	private String sortDirection;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}

	public List<String> getTransactionTypes() {
		return transactionTypes;
	}

	public void setTransactionTypes(List<String> transactionTypes) {
		this.transactionTypes = transactionTypes;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	@Override
	public String toString() {
		return "AuditReportRequest [userId=" + userId + ", organizations=" + organizations + ", transactionTypes=" + transactionTypes
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", page=" + page + ", rows=" + rows + "]";
	}

}
