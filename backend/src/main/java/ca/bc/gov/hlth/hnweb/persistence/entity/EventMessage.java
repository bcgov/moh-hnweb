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
 * Audit entity EventMessage
 */
@Entity
@Table(schema = "mspdirect", name = "event_message")
public class EventMessage {

	/**
	 * primary key
	 */
	@Id
	@Column(name = "event_message_id", columnDefinition = "bigserial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventMessageId;

	/**
	 * Response or error code returned as a part of the event.
	 */
	@Basic
	@Column(name = "error_code")
	private String errorCode;

	/**
	 * Error level, similar to logging levels (INFO, WARNING, ERROR, ETC)
	 */
	@Basic
	@Column(name = "error_level")
	private String errorLevel;

	/**
	 * Complete response/error message text.
	 */
	@Basic
	@Column(name = "message_text", columnDefinition = "text", length = 2147483647)
	private String messageText;

	/**
	 * foreign key to the original event
	 */
	@ManyToOne
	@JoinColumn(name = "transaction_event_id")
	private TransactionEvent transactionEvent;

	public EventMessage() {
	}

	public EventMessage(long eventMessageId) {
		this.eventMessageId = eventMessageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(String errorLevel) {
		this.errorLevel = errorLevel;
	}

	public Long getEventMessageId() {
		return eventMessageId;
	}

	public void setEventMessageId(Long eventMessageId) {
		this.eventMessageId = eventMessageId;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public TransactionEvent getTransactionEvent() {
		return transactionEvent;
	}

	public void setTransactionEvent(TransactionEvent transactionEvent) {
		this.transactionEvent = transactionEvent;
	}

	@Override
	public String toString() {
		return "EventMessage [eventMessageId=" + eventMessageId + ", errorCode=" + errorCode + ", errorLevel=" + errorLevel
				+ ", messageText=" + messageText + ", transactionEvent=" + transactionEvent + "]";
	}

}