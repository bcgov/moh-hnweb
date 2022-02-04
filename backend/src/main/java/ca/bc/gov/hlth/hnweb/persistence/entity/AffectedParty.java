package ca.bc.gov.hlth.hnweb.persistence.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Audit entity AffectedParty
 */
@Entity
@Table(schema = "mspdirect", name = "affected_party")
public class AffectedParty {

	/**
	 * primary key
	 */
	@Id
	@Column(name = "affected_party_id", columnDefinition = "bigserial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long affectedPartyId;

	/**
	 * identifier number, such as a PHN or MRN
	 */
	@Basic
	private String identifier;

	/**
	 * name of the source system that issued the identifier
	 */
	@Basic
	@Column(name = "identifier_source")
	private String identifierSource;

	/**
	 * the type of identifier (PHN, MRN, drivers license no, etc)
	 */
	@Basic
	@Column(name = "identifier_type")
	private String identifierType;

	/**
	 * Status of the identifier (Active, Merged, Deleted)
	 */
	@Basic
	private String status;

	/**
	 * Foreign key to the transaction the party is the subject of.
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private Transaction transaction;

	public AffectedParty() {
	}

	public AffectedParty(long affectedPartyId) {
		this.affectedPartyId = affectedPartyId;
	}

	public long getAffectedPartyId() {
		return affectedPartyId;
	}

	public void setAffectedPartyId(long affectedPartyId) {
		this.affectedPartyId = affectedPartyId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifierSource() {
		return identifierSource;
	}

	public void setIdentifierSource(String identifierSource) {
		this.identifierSource = identifierSource;
	}

	public String getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "AffectedParty [affectedPartyId=" + affectedPartyId + ", identifier=" + identifier + ", identifierSource=" + identifierSource
				+ ", identifierType=" + identifierType + ", status=" + status + ", transaction=" + transaction + "]";
	}

}