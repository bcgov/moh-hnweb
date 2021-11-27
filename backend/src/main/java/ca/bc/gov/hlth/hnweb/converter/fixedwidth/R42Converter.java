package ca.bc.gov.hlth.hnweb.converter.fixedwidth;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.LookupPhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.LookupPhnRequest;
import ca.bc.gov.hlth.hnweb.model.LookupPhnResponse;
import ca.bc.gov.hlth.hnweb.model.fixedwidth.FixedWidthDefaults;
import ca.bc.gov.hlth.hnweb.model.fixedwidth.PL0;
import ca.bc.gov.hlth.hnweb.model.fixedwidth.PL0Beneficary;
import ca.bc.gov.hlth.hnweb.model.fixedwidth.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.fixedwidth.RPBSPPL0;

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
		
		if (StringUtils.equals(header.getIdentifier(), RPBSHeader.IDENTIFER_ERRORMSG)) {
			response.setErrorMessage(header.getStatusCode() + " - " + header.getStatusText());
			return response;
		}

		if (StringUtils.equals(header.getIdentifier(), RPBSHeader.IDENTIFER_RESPONSE)) {
			response.setWarningMessage(header.getStatusCode() + " - " + header.getStatusText());
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

}