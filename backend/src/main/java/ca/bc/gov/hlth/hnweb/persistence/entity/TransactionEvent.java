package ca.bc.gov.hlth.hnweb.persistence.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Audit entity TransactionEvent
 */
@Entity
@Table(schema = "mspdirect", name = "transaction_event")
public class TransactionEvent {

	/**
	 * primary key
	 */
	@Id
	@Column(name = "transaction_event_id", columnDefinition = "bigserial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionEventId;

	/**
	 * time that the event occured
	 */
	@Basic
	@Column(name = "event_time", columnDefinition = "timestamptz", nullable = false)
	private Date eventTime;

	/**
	 * message id created by the application. could be the id from a request or
	 * response message to an external system (ie RAPID or HCIM).
	 */
	@Basic
	@Column(name = "message_id")
	private String messageId;

	/**
	 * transaction event type, for example error.
	 */
	@Basic
	@Column(name = "type", nullable = false)
	private String type;

	/**
	 * foreign key to the transaction this event belongs to.
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_id", nullable = false)
	private Transaction transaction;

	public TransactionEvent() {
	}

	public TransactionEvent(long transactionEventId) {
		this.transactionEventId = transactionEventId;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Long getTransactionEventId() {
		return transactionEventId;
	}

	public void setTransactionEventId(Long transactionEventId) {
		this.transactionEventId = transactionEventId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	@PrePersist
	public void prePersist() {
		if (eventTime == null) {
			eventTime = new Date();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
		result = prime * result + ((transaction == null) ? 0 : transaction.hashCode());
		result = prime * result + ((transactionEventId == null) ? 0 : transactionEventId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		TransactionEvent other = (TransactionEvent) obj;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!messageId.equals(other.messageId))
			return false;
		if (transaction == null) {
			if (other.transaction != null)
				return false;
		} else if (!transaction.equals(other.transaction))
			return false;
		if (transactionEventId == null) {
			if (other.transactionEventId != null)
				return false;
		} else if (!transactionEventId.equals(other.transactionEventId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TransactionEvent [transactionEventId=" + transactionEventId + ", eventTime=" + eventTime + ", messageId=" + messageId
				+ ", type=" + type + ", transaction=" + transaction + "]";
	}

}