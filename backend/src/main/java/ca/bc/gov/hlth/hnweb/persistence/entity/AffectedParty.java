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
	 * Specifies the direction of the the identifier in the transaction. (Inbound, Outbound)
	 */
	@Basic
	@Column(name = "direction", nullable = false)
	private String direction;

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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (affectedPartyId ^ (affectedPartyId >>> 32));
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((identifierType == null) ? 0 : identifierType.hashCode());
		result = prime * result + ((transaction == null) ? 0 : transaction.hashCode());
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
		AffectedParty other = (AffectedParty) obj;
		if (affectedPartyId != other.affectedPartyId)
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (identifierType == null) {
			if (other.identifierType != null)
				return false;
		} else if (!identifierType.equals(other.identifierType))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AffectedParty [affectedPartyId=" + affectedPartyId + ", identifier=" + identifier + ", identifierType=" + identifierType
				+ ", transaction=" + transaction + "]";
	}

}