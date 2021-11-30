package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.eligibility.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.rapid.FixedWidthDefaults;
import ca.bc.gov.hlth.hnweb.model.rapid.PL0;
import ca.bc.gov.hlth.hnweb.model.rapid.PL0Beneficary;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPL0;

public class R42Converter extends BaseFixedWidthConverter {
	private static final String TRAN_CODE = "RPBSPPL0";

	public R42Converter(FixedWidthDefaults fwDefaults) {
		super(fwDefaults);
	}
	
	public RPBSPPL0 convertRequest(LookupPhnRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(fwDefaults.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		PL0 pl0 = new PL0();
		pl0.setContractNumber(request.getContractNumber());
		pl0.setGroupNumber(request.getGroupNumber());

		RPBSPPL0 rpbsppl0 = new RPBSPPL0();
		rpbsppl0.setRpbsHeader(rpbsHeader);
		rpbsppl0.setPl0(pl0);

		return rpbsppl0;
	}
	
	public LookupPhnResponse convertResponse(RPBSPPL0 rpbsppl0) {
		LookupPhnResponse response = new LookupPhnResponse();
		RPBSHeader header = rpbsppl0.getRpbsHeader();
		
		handleStatus(header, response);
		
		if (StatusEnum.ERROR == response.getStatus()) {
			return response;
		}

		PL0 pl0 = rpbsppl0.getPl0();
		for (PL0Beneficary plb0Pl0Beneficary: pl0.getBeneficiaries()) {
			LookupPhnBeneficiary beneficiary = new LookupPhnBeneficiary();
			beneficiary.setPhn(plb0Pl0Beneficary.getPhn());
			beneficiary.setLastName(StringUtils.trim(plb0Pl0Beneficary.getFamilyName()));
			beneficiary.setFirstName(StringUtils.trim(plb0Pl0Beneficary.getFirstName()));
			beneficiary.setSecondName(StringUtils.trim(plb0Pl0Beneficary.getSecondName()));
			beneficiary.setThirdName(StringUtils.trim(plb0Pl0Beneficary.getThirdName()));
			// Convert the response Date from yyyy-MM-dd to yyyyMMdd
			beneficiary.setDateOfBirth(StringUtils.remove(plb0Pl0Beneficary.getBirthDate(), "-"));
			beneficiary.setGender(plb0Pl0Beneficary.getGender());
			response.getBeneficiaries().add(beneficiary);
		}
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}
	
	public static void main(String[] args) {
		System.out.println("6337109 valid " + isValidMod10("6337109"));
		
		System.out.println("6243109 valid " + isValidMod10("6243109"));
	}
	
	private static boolean isValidMod10(String mod10Str)
	{
		int numDigits = mod10Str.length();
		int sum = 0;
		int tmpDigit = 0;
		if (mod10Str.length()%2==0)
		{//Even number of digits in String to check
			for (int i=0;i<numDigits;i+=2)
			{
				//Odd Numbers
				tmpDigit = Character.getNumericValue(mod10Str.charAt(i));
				sum += tmpDigit==9?9:(tmpDigit*2)%9;
				//Even Numbers
				sum += Character.getNumericValue(mod10Str.charAt(i+1));
			}
		}
		else
		{//Odd Number of digits in String to check
			for (int i=1;i<numDigits;i+=2)
			{
				//Odd Numbers
				sum += Character.getNumericValue(mod10Str.charAt(i-1));
				//Even Numbers
				tmpDigit = Character.getNumericValue(mod10Str.charAt(i));
				sum += tmpDigit==9?9:(tmpDigit*2)%9;
			}
			//Last odd Number
			sum += Character.getNumericValue(mod10Str.charAt(numDigits-1));
		}
		if ((sum %10) == 0)
			return true;
		else
			return false;
	}

}