package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0Data;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMC0;
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
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}