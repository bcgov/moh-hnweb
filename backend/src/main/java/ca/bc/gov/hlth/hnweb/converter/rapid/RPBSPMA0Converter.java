package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.MA0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSAddress;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPMA0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressResponse;

public class RPBSPMA0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPMA0";

	public RPBSPMA0Converter() {
		super();
	}
	
	public RPBSPMA0 convertRequest(UpdateContractAddressRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		MA0 ma0 = new MA0();
		ma0.setGroupNumber(request.getGroupNumber());
		ma0.setPhn(request.getPhn());
		ma0.setHomeAddress(convertAddress(request.getHomeAddress()));
		ma0.setMailAddress(convertAddress(request.getMailingAddress()));
		RPBSPMA0 rpbspma0 = new RPBSPMA0();
		rpbspma0.setRpbsHeader(rpbsHeader);
		rpbspma0.setMa0(ma0);

		return rpbspma0;
	}
	
	public UpdateContractAddressResponse convertResponse(RPBSPMA0 rpbspma0) {
		UpdateContractAddressResponse response = new UpdateContractAddressResponse();
		RPBSHeader header = rpbspma0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspma0.getMa0().getPhn());
		
		return response;
	}
	
	private RPBSAddress convertAddress(MemberAddress memberAddress) {
		RPBSAddress rpbsAddress = new RPBSAddress();
		
		if (memberAddress == null) {
			return rpbsAddress;
		}
		rpbsAddress.setAddressLine1(memberAddress.getAddressLine1());
		rpbsAddress.setAddressLine2(memberAddress.getAddressLine2());
		rpbsAddress.setAddressLine3(memberAddress.getAddressLine3());
		rpbsAddress.setAddressLine4(memberAddress.getAddressLine4());
		rpbsAddress.setPostalCode(memberAddress.getPostalCode());
		
		return rpbsAddress;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}