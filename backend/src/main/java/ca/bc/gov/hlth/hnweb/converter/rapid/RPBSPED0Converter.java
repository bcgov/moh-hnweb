package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.ED0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPED0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.UpdateGroupMemberResponse;

public class RPBSPED0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPED0";

	public RPBSPED0Converter() {
		super();
	}
	
	public RPBSPED0 convertRequest(UpdateGroupMemberRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		ED0 ed0 = new ED0();
		ed0.setGroupNumber(request.getGroupNumber());
		ed0.setPhn(request.getPhn());
		ed0.setDepartmentNumber(request.getDepartmentNumber());

		RPBSPED0 rpbsped0 = new RPBSPED0();
		rpbsped0.setRpbsHeader(rpbsHeader);
		rpbsped0.setEd0(ed0);

		return rpbsped0;
	}
	
	public UpdateGroupMemberResponse convertResponse(RPBSPED0 rpbsped0) {
		UpdateGroupMemberResponse response = new UpdateGroupMemberResponse();
		RPBSHeader header = rpbsped0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbsped0.getEd0().getPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}