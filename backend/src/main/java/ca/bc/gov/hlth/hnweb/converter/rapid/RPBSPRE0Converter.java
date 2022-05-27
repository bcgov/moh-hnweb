package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.rapid.RE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPRE0;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ReinstateOverAgeDependentResponse;

public class RPBSPRE0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPRE0";

	public RPBSPRE0Converter() {
		super();
	}
	
	public RPBSPRE0 convertRequest(ReinstateOverAgeDependentRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		RE0 re0 = new RE0();
		re0.setSubscriberPHN(request.getPhn());
		re0.setGroupNumber(request.getGroupNumber());
		re0.setDependentPHN(request.getDependentPhn());
		re0.setDependentDOB(formatDate(request.getDependentDateOfBirth()));
		re0.setCanadianStudent(request.getIsStudent());
		if (StringUtils.equals("Y", request.getIsStudent())) {
			re0.setStudentEndDate(formatDate(request.getStudentEndDate()));
		} else {
			re0.setStudentEndDate("");
		}

		RPBSPRE0 rpbspre0 = new RPBSPRE0();
		rpbspre0.setRpbsHeader(rpbsHeader);
		rpbspre0.setRe0(re0);

		return rpbspre0;
	}
	
	public ReinstateOverAgeDependentResponse convertResponse(RPBSPRE0 rpbspre0) {
		ReinstateOverAgeDependentResponse response = new ReinstateOverAgeDependentResponse();
		RPBSHeader header = rpbspre0.getRpbsHeader();
		
		handleStatus(header, response);
		
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}