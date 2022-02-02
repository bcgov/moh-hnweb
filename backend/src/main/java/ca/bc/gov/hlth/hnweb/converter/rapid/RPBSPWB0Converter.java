package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPWB0;
import ca.bc.gov.hlth.hnweb.model.rapid.WB0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddDependentResponse;

public class RPBSPWB0Converter extends BaseRapidConverter {
	
	private static final String TRAN_CODE = "RPBSPWB0";

	public RPBSPWB0Converter() {
		super();
	}
	
	public RPBSPWB0 convertRequest(AddDependentRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		WB0 wb0 = new WB0();
		wb0.setGroupNumber(request.getGroupNumber());
		wb0.setPhn(request.getPhn());
		wb0.setBenficiaryPHN(request.getDependentPhn());
		wb0.setCoverageEffectiveDate(formatDate(request.getCoverageEffectiveDate()));
		wb0.setRelationshipCode(request.getRelationship());
		wb0.setStudentFlag(request.getIsStudent());
		if (StringUtils.equals("Y", request.getIsStudent())) {
			wb0.setStudentEndDate(formatDate(request.getStudentEndDate()));
		} else {
			wb0.setStudentEndDate("");
		}

		RPBSPWB0 rpbspwb0 = new RPBSPWB0();
		rpbspwb0.setRpbsHeader(rpbsHeader);
		rpbspwb0.setWb0(wb0);

		return rpbspwb0;
	}
	
	public AddDependentResponse convertResponse(RPBSPWB0 rpbspwb0) {
		AddDependentResponse response = new AddDependentResponse();
		RPBSHeader header = rpbspwb0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspwb0.getWb0().getPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}