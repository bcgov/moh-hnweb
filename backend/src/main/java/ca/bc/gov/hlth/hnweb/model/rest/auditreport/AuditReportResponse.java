package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

import java.time.LocalDate;

public class AuditReportResponse {

	private String type;

	private String organization;

	private String userId;

	private LocalDate transactionStartTime;

	private String affectedPartyId;

	private String affectedPartyType;

	private String transactionId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDate getTransactionStartTime() {
		return transactionStartTime;
	}

	public void setTransactionStartTime(LocalDate transactionStartTime) {
		this.transactionStartTime = transactionStartTime;
	}

	public String getAffectedPartyId() {
		return affectedPartyId;
	}

	public void setAffectedPartyId(String affectedPartyId) {
		this.affectedPartyId = affectedPartyId;
	}

	public String getAffectedPartyType() {
		return affectedPartyType;
	}

	public void setAffectedPartyType(String affectedPartyType) {
		this.affectedPartyType = affectedPartyType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}