package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class WC0 {

	/** 1 PHN String Yes 0...10 1..1 */
	private String phn;
	/** 2 GroupNumber String Yes 0...7 1..1 */
	private String groupNumber;
	/** 3 CoverageCancelDate RPBSDate Yes 0...10 1..1 */
	private String coverageCancelDate;
	/** 4 PayerCancelReason String Yes 0...1 1..1 */
	private String payerCancelReason;

	public WC0() {
		super();
	}

	public WC0(String message) {
		super();
		phn = StringUtils.substring(message, 0, 10);
		groupNumber = StringUtils.substring(message, 10, 17);
		coverageCancelDate = StringUtils.substring(message, 17, 27);
		// K or E
		payerCancelReason = StringUtils.substring(message, 27, 28);
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(phn, 10));
		sb.append(StringUtils.rightPad(groupNumber, 7));

		return sb.toString();
	}

	public String getPhn() {
		return phn;
	}

	public void setPhn(String phn) {
		this.phn = phn;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
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
