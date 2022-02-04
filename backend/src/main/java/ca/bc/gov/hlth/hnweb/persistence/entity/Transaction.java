package ca.bc.gov.hlth.hnweb.persistence.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	
	/** 
	 * Unique identifier for the transaction. This is to be issued by the application, not the database. 
	 */
	@Id
	@Column(name = "transaction_id", updatable = false, nullable = false)
	private UUID transactionId;
	
	/**
	 * Type of transaction. Pulled from the message header. for example E45, R15
	 */
	@Basic
	@Column(name="type")
	private String type;
	
	/**
	 * Unique Identifier for the user session. Use the OIDC session_state parameter from the auth response.
	 */
	@Basic
	@Column(name="session_id")
	private String sessionId;
	
	/**
	 * Name of the server/instance/container id that processed the transaction. Used to track potential issues in an environment with multiple instances.
	 */
	@Basic
	@Column(name="server")
	private String server;
	
	/**
	 * Name of the server/instance/container id that processed the transaction. Used to track potential issues in an environment with multiple instances.
	 */
	@Basic
	@Column(name="source_ip")
	private String sourceIp;
	
	/**
	 * Organization ID of the user that initiated the transaction
	 */
	@Basic
	@Column(name="organization")
	private String organization;
	
	/**
	 * ID of the user that initiated the transaction
	 */
	@Basic
	@Column(name="user_id")
	private String userId;

	/**
	 * Time that the transaction was started/created
	 */
	@Basic
	@Column(name="start_time", columnDefinition="TIMESTAMPTZ")
	private Date startTime;

	public UUID getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
