package ca.bc.gov.hlth.hnweb.persistence.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

	/**
	 * Unique identifier for the transaction. This is to be issued by the
	 * application, not the database.
	 */
	@Id
	@Column(name = "transaction_id", updatable = false, nullable = false)
	private UUID transactionId;

	/**
	 * Type of transaction. Pulled from the message header. for example E45, R15
	 */
	@Basic
	@Column(name = "type", nullable = false)
	private String type;

	/**
	 * Unique Identifier for the user session. Use the OIDC session_state parameter
	 * from the auth response.
	 */
	@Basic
	@Column(name = "session_id")
	private String sessionId;

	/**
	 * Name of the server/instance/container id that processed the transaction. Used
	 * to track potential issues in an environment with multiple instances.
	 */
	@Basic
	@Column(name = "server", nullable = false)
	private String server;

	/**
	 * IP address of the source system that sent the transaction. (note that you may
	 * have to look in the HTTP headers due to load balancing),IP address of the
	 * source system that sent the transaction. (note that you may have to look in
	 * the HTTP headers due to load balancing)
	 */
	@Basic
	@Column(name = "source_ip", nullable = false)
	private String sourceIp;

	/**
	 * Organization ID of the user that initiated the transaction
	 */
	@Basic
	@Column(name = "organization")
	private String organization;

	/**
	 * Organization Name of the user that initiated the transaction
	 */
	@Basic
	@Column(name = "organization_name")
	private String organizationName;

	/**
	 * SPG of the user performing the transaction
	 */
	@Basic
	@Column(name = "spg_role")
	private String spgRole;

	/**
	 * ID of the user that initiated the transaction
	 */
	@Basic
	@Column(name = "user_id")
	private String userId;

	/**
	 * Time that the transaction was started/created
	 */
	@Basic
	@Column(name = "start_time", columnDefinition = "TIMESTAMPTZ", nullable = false)
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

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getSpgRole() {
		return spgRole;
	}

	public void setSpgRole(String spgRole) {
		this.spgRole = spgRole;
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

	@PrePersist
	public void prePersist() {
		if (startTime == null) {
			startTime = new Date();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + ((server == null) ? 0 : server.hashCode());
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		result = prime * result + ((sourceIp == null) ? 0 : sourceIp.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.equals(other.server))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		if (sourceIp == null) {
			if (other.sourceIp != null)
				return false;
		} else if (!sourceIp.equals(other.sourceIp))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (transactionId == null) {
			if (other.transactionId != null)
				return false;
		} else if (!transactionId.equals(other.transactionId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", type=" + type + ", sessionId=" + sessionId + ", server=" + server
				+ ", sourceIp=" + sourceIp + ", organization=" + organization + ", userId=" + userId + ", startTime=" + startTime + "]";
	}

}