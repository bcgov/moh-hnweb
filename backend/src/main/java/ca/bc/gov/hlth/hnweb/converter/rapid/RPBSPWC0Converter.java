package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWC0;
import ca.bc.gov.hlth.hnweb.model.rapid.WC0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelGroupMemberResponse;

public class RPBSPWC0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPWC0";

	public RPBSPWC0Converter() {
		super();
	}
	
	public RPBSPWC0 convertRequest(CancelGroupMemberRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		WC0 wc0 = new WC0();
		wc0.setGroupNumber(request.getGroupNumber());
		wc0.setPhn(request.getPhn());
		wc0.setCoverageCancelDate(formatDate(request.getCoverageCancelDate()));
		wc0.setPayerCancelReason(request.getCancelReason());

		RPBSPWC0 rpbspwc0 = new RPBSPWC0();
		rpbspwc0.setRpbsHeader(rpbsHeader);
		rpbspwc0.setWc0(wc0);

		return rpbspwc0;
	}
	
	public CancelGroupMemberResponse convertResponse(RPBSPWC0 rpbspwc0) {
		CancelGroupMemberResponse response = new CancelGroupMemberResponse();
		RPBSHeader header = rpbspwc0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspwc0.getWc0().getPhn());
		
		return response;
	}


	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}