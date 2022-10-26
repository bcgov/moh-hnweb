package ca.bc.gov.hlth.hnweb.converter.rapid;

import ca.bc.gov.hlth.hnweb.model.rapid.AG0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAG0;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ChangeCancelDateResponse;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateRequest;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.ExtendCancelDateResponse;

public class RPBSPAG0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPAG0";

	public RPBSPAG0Converter() {
		super();
	}
	
	public RPBSPAG0 convertRequest(ChangeCancelDateRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		AG0 ag0 = new AG0();
		ag0.setGroupNumber(request.getGroupNumber());
		ag0.setPhn(request.getPhn());
		ag0.setExistingCancellationDate(formatDate(request.getExistingCancellationDate()));
		ag0.setNewCancellationDate(formatDate(request.getNewCancellationDate()));

		RPBSPAG0 rpbspag0 = new RPBSPAG0();
		rpbspag0.setRpbsHeader(rpbsHeader);
		rpbspag0.setAg0(ag0);
		
		return rpbspag0;
	}
	
	public RPBSPAG0 convertRequest(ExtendCancelDateRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		AG0 ag0 = new AG0();
		ag0.setGroupNumber(request.getGroupNumber());
		ag0.setPhn(request.getPhn());
		ag0.setExistingCancellationDate(formatDate(request.getExistingCancellationDate()));
		ag0.setNewCancellationDate(formatDate(request.getNewCancellationDate()));
		ag0.setImmigrationCode(request.getImmigrationCode());
		ag0.setPermitIssueDate(formatDate(request.getPermitIssueDate()));
		ag0.setPermitExpiryDate(formatDate(request.getPermitExpiryDate()));
		

		RPBSPAG0 rpbspag0 = new RPBSPAG0();
		rpbspag0.setRpbsHeader(rpbsHeader);
		rpbspag0.setAg0(ag0);
		
		return rpbspag0;
	}
	
	public ChangeCancelDateResponse convertResponse(RPBSPAG0 rpbspag0) {
		ChangeCancelDateResponse response = new ChangeCancelDateResponse();
		RPBSHeader header = rpbspag0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspag0.getAg0().getPhn());
		
		return response;
	}
	
	public ExtendCancelDateResponse convertExtendCancelDateResponse(RPBSPAG0 rpbspag0) {
		ExtendCancelDateResponse response = new ExtendCancelDateResponse();
		RPBSHeader header = rpbspag0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspag0.getAg0().getPhn());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}