package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0Data;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.BeneficiaryContractPeriod;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.GetContractPeriodsResponse;

public class RPBSPMC0Converter extends BaseRapidConverter {
	
	private static final String TRAN_CODE = "RPBSPMC0";

	public RPBSPMC0Converter() {
		super();
	}
	
	public RPBSPMC0 convertRequest(GetContractPeriodsRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		RPBSPMC0Data rpbsmc0Data = new RPBSPMC0Data();
		rpbsmc0Data.setPhn(request.getPhn());
		
		RPBSPMC0 rpbspmc0 = new RPBSPMC0();
		rpbspmc0.setRpbsHeader(rpbsHeader);
		rpbspmc0.setRpbsmc0Data(rpbsmc0Data);

		return rpbspmc0;
	}
	
	public GetContractPeriodsResponse convertResponse(RPBSPMC0 rpbspmc0) {
		GetContractPeriodsResponse response = new GetContractPeriodsResponse();
		RPBSHeader header = rpbspmc0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspmc0.getRpbsmc0Data().getPhn());
		
		rpbspmc0.getRpbsmc0Data().getBeneficiaries().forEach(b -> {
			//The UI displays this as a flat structure so map contract period to a BeneficiaryContractPeriod
			b.getContractPeriods().forEach(cp -> {
				BeneficiaryContractPeriod beneficiaryContractPeriod = new BeneficiaryContractPeriod();
				beneficiaryContractPeriod.setPhn(b.getPhn());
				beneficiaryContractPeriod.setFirstName(b.getFirstName());
				beneficiaryContractPeriod.setLastName(b.getLastName());
				beneficiaryContractPeriod.setDateOfBirth(b.getBirthDate());
				beneficiaryContractPeriod.setGender(b.getGender());
				beneficiaryContractPeriod.setContractHolder(cp.getPhn());
				beneficiaryContractPeriod.setGroupNumber(cp.getGroupNumber());
				beneficiaryContractPeriod.setRelationship(cp.getRelationship());
				beneficiaryContractPeriod.setEffectiveDate(cp.getEffectiveDate());
				beneficiaryContractPeriod.setCancelDate(cp.getCancelDate());
				beneficiaryContractPeriod.setCancelReason(cp.getReasonCode());
				response.getBeneficiaryContractPeriods().add(beneficiaryContractPeriod);
			});
		});
				
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}