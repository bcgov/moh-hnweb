package ca.bc.gov.hlth.hnweb.model.fixedwidth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PL0 {

	/** 1 ContractNumber String No 0...9 */
	private String contractNumber = "";
	/** 2 GroupNumber String No 0...7 .. */
	private String groupNumber = "";
	/** 1 PHNCount String No 0...2 */
	private String phnCount = "";

	private List<PL0Beneficary> beneficiaries = new ArrayList<PL0Beneficary>();

	public PL0() {
		super();
	}

	public PL0(String message) {
		super();
		groupNumber = StringUtils.substring(message, 0, 9);
		contractNumber = StringUtils.substring(message, 9, 16);
		phnCount = StringUtils.substring(message, 16, 18);

		String beneficiariesStr = StringUtils.substring(message, 18);

		if (StringUtils.isNotBlank(beneficiariesStr)) {
			int count = 0;
			String beneficiary = StringUtils.substring(beneficiariesStr, 0, PL0Beneficary.SEGMENT_LENGTH);
			while (StringUtils.isNotBlank(beneficiary)) {
				PL0Beneficary pl0Beneficary = new PL0Beneficary(beneficiary);
				beneficiaries.add(pl0Beneficary);
				count++;
				beneficiary = StringUtils.substring(beneficiariesStr, PL0Beneficary.SEGMENT_LENGTH * count, PL0Beneficary.SEGMENT_LENGTH * (count + 1));
			}
		}
	}

	public String serialize() {
		// Serialize is only used in when creating the request
		// where only the first two fields are used
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(contractNumber, 9));
		sb.append(StringUtils.rightPad(groupNumber, 7));		

		return sb.toString();
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getPhnCount() {
		return phnCount;
	}

	public void setPhnCount(String phnCount) {
		this.phnCount = phnCount;
	}

	public List<PL0Beneficary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<PL0Beneficary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}

}
