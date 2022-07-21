package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.AJ0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAJ0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.ChangeEffectiveDateResponse;

public class RPBSPAJ0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPAJ0";

	public RPBSPAJ0Converter() {
		super();
	}
	
	public RPBSPAJ0 convertRequest(ChangeEffectiveDateRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		AJ0 aj0 = new AJ0();
		aj0.setGroupNumber(request.getGroupNumber());
		aj0.setPhn(request.getPhn());
		aj0.setExistingEffectiveDate(formatDate(request.getExistingEffectiveDate()));
		aj0.setNewEffectiveDate(formatDate(request.getNewEffectiveDate()));

		RPBSPAJ0 rpbspaj0 = new RPBSPAJ0();
		rpbspaj0.setRpbsHeader(rpbsHeader);
		rpbspaj0.setAj0(aj0);

		return rpbspaj0;
	}
	
	public ChangeEffectiveDateResponse convertResponse(RPBSPAJ0 rpbspaj0) {
		ChangeEffectiveDateResponse response = new ChangeEffectiveDateResponse();
		RPBSHeader header = rpbspaj0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspaj0.getAj0().getPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}