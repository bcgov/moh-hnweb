package ca.bc.gov.hlth.hnweb.converter.rapid;

import java.time.LocalDate;

import ca.bc.gov.hlth.hnweb.model.rapid.AI0;
import ca.bc.gov.hlth.hnweb.model.rapid.R45NewPayer;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSAddress;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPAI0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;
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
		
	public AddGroupMemberRequest buildAddGroupMemberRequest(RPBSPAI0 rpbspai0) {
		R45NewPayer newPayerData = rpbspai0.getAi0().getNewPayerData();
		AddGroupMemberRequest request = new AddGroupMemberRequest();
		request.setEffectiveDate(LocalDate.parse(rpbspai0.getAi0().getNewCoverageEffectiveDate()));
		request.setPhn(newPayerData.getPhn());
		request.setSpousePhn(newPayerData.getSpousePhn());
		request.setDependentPhn1(newPayerData.getDependentPhn1());
		request.setDependentPhn2(newPayerData.getDependentPhn2());
		request.setDependentPhn3(newPayerData.getDependentPhn3());
		request.setDependentPhn4(newPayerData.getDependentPhn4());
		request.setDependentPhn5(newPayerData.getDependentPhn5());
		request.setDependentPhn6(newPayerData.getDependentPhn6());
		request.setDependentPhn7(newPayerData.getDependentPhn7());
		request.setGroupNumber(newPayerData.getGroupNumber());
		request.setGroupMemberNumber(newPayerData.getEmployeeNumber());
		request.setDepartmentNumber(newPayerData.getDepartmentNumber());
		request.setHomeAddress(convertToMemberAddress(newPayerData.getHomeAddress()));
		request.setMailingAddress(convertToMemberAddress(newPayerData.getMailAddress()));
		request.setPhone(newPayerData.getPhone1().getPhoneNumber());
		return request;
	}
	
	private MemberAddress convertToMemberAddress(RPBSAddress rpbsAddress) {
		MemberAddress memberAddress = new MemberAddress();
		
		if (rpbsAddress == null) {
			return memberAddress;
		}
		memberAddress.setAddressLine1(rpbsAddress.getAddressLine1());
		memberAddress.setAddressLine2(rpbsAddress.getAddressLine2());
		memberAddress.setAddressLine3(rpbsAddress.getAddressLine3());
		memberAddress.setAddressLine4(rpbsAddress.getAddressLine4());
		memberAddress.setPostalCode(rpbsAddress.getPostalCode());
		
		return memberAddress;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}