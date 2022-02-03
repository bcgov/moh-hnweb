package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class WP0 {

	/** 1 SubscriberPHN String No 0...10 */
	private String subscriberPhn;
	/** 2 GroupNumber String No	0...7 */
	private String groupNumber;
	/** 3 BeneficiaryPHN String	No 0...10 */
	private String beneficiaryPhn;	
	/** 4 CoverageCancelDate String No 0...10 */
	private String coverageCancelDate;
	/** 5 PayerCancelReason String No 0...1 */
	private String payerCancelReason;

	public WP0() {
		super();
	}

	public WP0(String message) {
		super();
		subscriberPhn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		beneficiaryPhn = StringUtils.substring(message, 17, 27);
		coverageCancelDate = StringUtils.substring(message, 27, 37);
		// I , E or P
		payerCancelReason = StringUtils.substring(message, 37, 38);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(subscriberPhn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));
		sb.append(StringUtils.rightPad(beneficiaryPhn, 10));
		sb.append(StringUtils.rightPad(coverageCancelDate, 10));
		sb.append(StringUtils.rightPad(payerCancelReason, 1));

		return sb.toString();
	}

	public String getSubscriberPhn() {
		return subscriberPhn;
	}

	public void setSubscriberPhn(String subscriberPhn) {
		this.subscriberPhn = subscriberPhn;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getBeneficiaryPhn() {
		return beneficiaryPhn;
	}

	public void setBeneficiaryPhn(String beneficiaryPhn) {
		this.beneficiaryPhn = beneficiaryPhn;
	}

	public String getCoverageCancelDate() {
		return coverageCancelDate;
	}

	public void setCoverageCancelDate(String coverageCancelDate) {
		this.coverageCancelDate = coverageCancelDate;
	}

	public String getPayerCancelReason() {
		return payerCancelReason;
	}

	public void setPayerCancelReason(String payerCancelReason) {
		this.payerCancelReason = payerCancelReason;
	}

}
