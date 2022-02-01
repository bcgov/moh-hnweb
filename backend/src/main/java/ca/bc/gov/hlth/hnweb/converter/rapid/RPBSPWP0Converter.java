package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWP0;
import ca.bc.gov.hlth.hnweb.model.rapid.WP0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.CancelDependentResponse;;

public class RPBSPWP0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPWP0";

	public RPBSPWP0Converter() {
		super();
	}
	
	public RPBSPWP0 convertRequest(CancelDependentRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		WP0 wp0 = new WP0();
		wp0.setGroupNumber(request.getGroupNumber());
		wp0.setSubscriberPhn(request.getPhn());
		wp0.setBeneficiaryPhn(request.getDependentPhn());
		wp0.setCoverageCancelDate(formatDate(request.getCoverageCancelDate()));
		wp0.setPayerCancelReason(request.getCancelReason());

		RPBSPWP0 rpbspwp0 = new RPBSPWP0();
		rpbspwp0.setRpbsHeader(rpbsHeader);
		rpbspwp0.setWp0(wp0);

		return rpbspwp0;
	}
			
	public CancelDependentResponse convertResponse(RPBSPWP0 rpbspwp0) {
		CancelDependentResponse response = new CancelDependentResponse();
		RPBSHeader header = rpbspwp0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspwp0.getWp0().getSubscriberPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}