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
@Table(name = "affected_party")
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
	@Column(name = "identifier", nullable = false)
	private String identifier;

	/**
	 * the type of identifier (PHN, MRN, drivers license no, etc)
	 */
	@Basic
	@Column(name = "identifier_type", nullable = false)
	private String identifierType;

	/**
	 * Foreign key to the transaction the party is the subject of.
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	public AffectedParty() {
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

	public String getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "AffectedParty [affectedPartyId=" + affectedPartyId + ", identifier=" + identifier + ", identifierType=" + identifierType
				+ ", transaction=" + transaction + "]";
	}

}