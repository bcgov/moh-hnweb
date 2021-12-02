package ca.bc.gov.hlth.hnweb.model.rapid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class RPBSPPE0Data {
	private static final int MAX_PHNS = 10; 
	
	/** 1 PHN String No 0...100 .. */
	private List<String> phns;
	/** 2 Eligibility Date No 0...10 .. */
	private String eligibilityDate;
	
	private List<RPBSPPE0Beneficiary> beneficiaries = new ArrayList<RPBSPPE0Beneficiary>();


	public RPBSPPE0Data() {
		super();
	}

	public RPBSPPE0Data(String message) {
		super();
		this.eligibilityDate = StringUtils.substring(message, 100, 110);

		String beneficiariesStr = StringUtils.substring(message, 110);

		if (StringUtils.isNotBlank(beneficiariesStr)) {
			int count = 0;
			String beneficiary = StringUtils.substring(beneficiariesStr, 0, RPBSPPE0Beneficiary.SEGMENT_LENGTH);
			while (StringUtils.isNotBlank(beneficiary)) {
				RPBSPPE0Beneficiary rpbsppe0Beneficiary = new RPBSPPE0Beneficiary(beneficiary);
				beneficiaries.add(rpbsppe0Beneficiary);
				count++;
				beneficiary = StringUtils.substring(beneficiariesStr, RPBSPPE0Beneficiary.SEGMENT_LENGTH * count, RPBSPPE0Beneficiary.SEGMENT_LENGTH * (count + 1));
			}
		}
	}

	public String serialize() {
		// Only serialize the fields required in the request
		StringBuilder sb = new StringBuilder();
		
		// Only use the first 10 PHNs
		List<String> usablePhns = null;
		if (phns.size() > MAX_PHNS) {
			usablePhns = phns.subList(0, MAX_PHNS);	
		} else {
			usablePhns = phns;
		}
		StringBuilder phnSb = new StringBuilder();
		usablePhns.forEach(phnSb::append);			
		
		sb.append(StringUtils.rightPad(phnSb.toString(), 100));
		sb.append(StringUtils.rightPad(eligibilityDate, 10));
		return sb.toString();
	}

	

	public List<String> getPhns() {
		return phns;
	}

	public void setPhns(List<String> phns) {
		this.phns = phns;
	}

	public String getEligibilityDate() {
		return eligibilityDate;
	}

	public void setEligibilityDate(String eligibilityDate) {
		this.eligibilityDate = eligibilityDate;
	}

	public List<RPBSPPE0Beneficiary> getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(List<RPBSPPE0Beneficiary> beneficiaries) {
		this.beneficiaries = beneficiaries;
	}


}
