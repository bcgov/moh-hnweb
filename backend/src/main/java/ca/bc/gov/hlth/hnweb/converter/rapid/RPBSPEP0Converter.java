package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.rapid.EP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPEP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPhone;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.UpdateContractAddressResponse;

public class RPBSPEP0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPEP0";

	public RPBSPEP0Converter() {
		super();
	}

	public RPBSPEP0 convertRequest(UpdateContractAddressRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		EP0 ep0 = new EP0();
		ep0.setGroupNumber(request.getGroupNumber());
		ep0.setPhn(request.getPhn());

		RPBSPhone phone0 = new RPBSPhone();
		// Only area code and number are used in the request
		phone0.setPhoneAreaCode(StringUtils.substring(request.getPhone(), 0, 3));
		phone0.setPhoneNumber(StringUtils.substring(request.getPhone(), 3, 10));
		ep0.setPhone0(phone0);

		// Phone 1 isn't used
		ep0.setPhone1(new RPBSPhone());
		RPBSPEP0 rpbspep0 = new RPBSPEP0();
		rpbspep0.setRpbsHeader(rpbsHeader);
		rpbspep0.setEp0(ep0);

		return rpbspep0;
	}

	public UpdateContractAddressResponse convertResponse(RPBSPEP0 rpbspep0) {
		UpdateContractAddressResponse response = new UpdateContractAddressResponse();
		RPBSHeader header = rpbspep0.getRpbsHeader();

		handleStatus(header, response);
		response.setPhn(rpbspep0.getEp0().getPhn());

		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}