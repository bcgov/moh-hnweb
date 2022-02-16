package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSMsgData;
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

		RPBSMsgData rpbsMsgData = new RPBSMsgData();
		rpbsMsgData.setData(request.getPhn());
		
		RPBSPMC0 rpbspmc0 = new RPBSPMC0();
		rpbspmc0.setRpbsHeader(rpbsHeader);
		rpbspmc0.setRpbsMsgData(rpbsMsgData);

		return rpbspmc0;
	}
	
	public GetContractPeriodsResponse convertResponse(RPBSPMC0 rpbspmc0) {
		GetContractPeriodsResponse response = new GetContractPeriodsResponse();
		RPBSHeader header = rpbspmc0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspmc0.getRpbsMsgData().getData());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}