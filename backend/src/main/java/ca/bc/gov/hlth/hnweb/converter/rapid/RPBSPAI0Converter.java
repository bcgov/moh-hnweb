package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.AI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAI0;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageResponse;

public class RPBSPAI0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPAI0";

	public RPBSPAI0Converter() {
		super();
	}
	
	public RPBSPAI0 convertRequest(RenewCancelledGroupCoverageRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		AI0 ai0 = new AI0();
		ai0.setGroupNumber(request.getGroupNumber());
		ai0.setPhn(request.getPhn());
		ai0.setNewCoverageEffectiveDate(formatDate(request.getNewCoverageEffectiveDate()));

		RPBSPAI0 rpbspai0 = new RPBSPAI0();
		rpbspai0.setRpbsHeader(rpbsHeader);
		rpbspai0.setAi0(ai0);
		
		return rpbspai0;
	}
	
	public RenewCancelledGroupCoverageResponse convertResponse(RPBSPAI0 rpbspai0) {
		RenewCancelledGroupCoverageResponse response = new RenewCancelledGroupCoverageResponse();
		RPBSHeader header = rpbspai0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspai0.getAi0().getPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}