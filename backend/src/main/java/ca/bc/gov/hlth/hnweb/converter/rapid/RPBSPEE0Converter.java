package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.EE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateNumberAndDeptResponse;

public class RPBSPEE0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPEE0";

	public RPBSPEE0Converter() {
		super();
	}
	
	public RPBSPEE0 convertRequest(UpdateNumberAndDeptRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		EE0 ee0 = new EE0();
		ee0.setGroupNumber(request.getGroupNumber());
		ee0.setPhn(request.getPhn());
		ee0.setEmployeeNumber(request.getGroupMemberNumber());

		RPBSPEE0 rpbspee0 = new RPBSPEE0();
		rpbspee0.setRpbsHeader(rpbsHeader);
		rpbspee0.setEe0(ee0);

		return rpbspee0;
	}
	
	public UpdateNumberAndDeptResponse convertResponse(RPBSPEE0 rpbspee0) {
		UpdateNumberAndDeptResponse response = new UpdateNumberAndDeptResponse();
		RPBSHeader header = rpbspee0.getRpbsHeader();
		
		handleStatus(header, response);
		
		response.setPhn(rpbspee0.getEe0().getPhn());
		response.setGroupNumber(rpbspee0.getEe0().getGroupNumber());
		response.setGroupMemberNumber(rpbspee0.getEe0().getEmployeeNumber());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}