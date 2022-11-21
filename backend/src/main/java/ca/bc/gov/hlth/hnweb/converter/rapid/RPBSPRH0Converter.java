package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.RH0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRH0;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateCancelledGroupCoverageResponse;

public class RPBSPRH0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPRH0";

	public RPBSPRH0Converter() {
		super();
	}

	public RPBSPRH0 convertRequest(ReinstateCancelledGroupCoverageRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		RH0 rh0 = new RH0();
		rh0.setGroupNumber(request.getGroupNumber());
		rh0.setPhn(request.getPhn());
		rh0.setCancelDateToBeRemoved(formatDate(request.getCancelDateToBeRemoved()));

		RPBSPRH0 rpbsprh0 = new RPBSPRH0();
		rpbsprh0.setRpbsHeader(rpbsHeader);
		rpbsprh0.setRh0(rh0);

		return rpbsprh0;
	}

	public ReinstateCancelledGroupCoverageResponse convertResponse(RPBSPRH0 rpbsprh0) {
		ReinstateCancelledGroupCoverageResponse response = new ReinstateCancelledGroupCoverageResponse();
		RPBSHeader header = rpbsprh0.getRpbsHeader();

		handleStatus(header, response);
		response.setPhn(rpbsprh0.getRh0().getPhn());

		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}