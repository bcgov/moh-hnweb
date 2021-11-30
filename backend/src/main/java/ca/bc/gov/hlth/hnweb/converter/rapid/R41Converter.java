package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.InquirePhnResponse;
import ca.bc.gov.hlth.hnweb.model.rapid.CI0;
import ca.bc.gov.hlth.hnweb.model.rapid.FixedWidthDefaults;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;

public class R41Converter extends BaseRapidConverter {
	//private static final String TRAN_CODE = "RPBSPPE0";
	private static final String TRAN_CODE = "RPBSPPMC";

	public R41Converter(FixedWidthDefaults fwDefaults) {
		super(fwDefaults);
	}
	
	public RPBSPPE0 convertRequest(InquirePhnRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(fwDefaults.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		// TODO (weskubo-cgi) This is not the correct format
		CI0 ci0 = new CI0();
		for (String phn: request.getPhns()) {
			ci0.setPhn(phn);	
		}

		RPBSPPE0 rpbsppe0 = new RPBSPPE0();
		rpbsppe0.setRpbsHeader(rpbsHeader);
		rpbsppe0.setCi0(ci0);

		return rpbsppe0;
	}
	
	public InquirePhnResponse convertResponse(RPBSPPE0 rpbspmc0) {
		InquirePhnResponse response = new InquirePhnResponse();
		RPBSHeader header = rpbspmc0.getRpbsHeader();
		
		if (StringUtils.equals(header.getIdentifier(), RPBSHeader.IDENTIFER_ERRORMSG)) {
			response.setMessage(header.getStatusCode() + " - " + header.getStatusText());
			return response;
		}
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}