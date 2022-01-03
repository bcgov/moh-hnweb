package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.EE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEE0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberResponse;

public class RPBSPEE0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPEE0";

	public RPBSPEE0Converter() {
		super();
	}
	
	public RPBSPEE0 convertRequest(UpdateGroupMemberRequest request) {
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
	
	public UpdateGroupMemberResponse convertResponse(RPBSPEE0 rpbspee0) {
		UpdateGroupMemberResponse response = new UpdateGroupMemberResponse();
		RPBSHeader header = rpbspee0.getRpbsHeader();
		
		handleStatus(header, response);
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}