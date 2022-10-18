package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSAddress;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPXP0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPhone;
import ca.bc.gov.hlth.hnweb.model.rapid.XP0;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberRequest;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.AddGroupMemberResponse;
import ca.bc.gov.hlth.hnweb.model.rest.groupmember.MemberAddress;
import ca.bc.gov.hlth.hnweb.model.rest.maintenance.RenewCancelledGroupCoverageResponse;

public class RPBSPXP0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPXP0";

	public RPBSPXP0Converter() {
		super();
	}
	
	public RPBSPXP0 convertRequest(AddGroupMemberRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		XP0 xp0 = new XP0();
		xp0.setGroupNumber(request.getGroupNumber());
		xp0.setEmployeeNumber(request.getGroupMemberNumber());
		xp0.setDepartmentNumber(request.getDepartmentNumber());
		xp0.setEffectiveDate(formatDate(request.getEffectiveDate()));
		
		RPBSPhone phone0 = new RPBSPhone();
		// Only area code and number are used in the request
		phone0.setPhoneAreaCode(StringUtils.substring(request.getPhone(), 0, 3));
		phone0.setPhoneNumber(StringUtils.substring(request.getPhone(), 3, 10));
		xp0.setPhone0(phone0);
		
		// Phone 1 isn't used
		xp0.setPhone1(new RPBSPhone());

		xp0.setHomeAddress(convertAddress(request.getHomeAddress()));
		xp0.setMailAddress(convertAddress(request.getMailingAddress()));
		
		xp0.setPhn(request.getPhn());
		xp0.setSpousePhn(request.getSpousePhn());
		
		// Note there are 8 slots but the V2 and UI only support 7 for dependent
		xp0.setDependentPhn0(request.getDependentPhn1());
		xp0.setDependentPhn1(request.getDependentPhn2());
		xp0.setDependentPhn2(request.getDependentPhn3());
		xp0.setDependentPhn3(request.getDependentPhn4());
		xp0.setDependentPhn4(request.getDependentPhn5());
		xp0.setDependentPhn5(request.getDependentPhn6());
		xp0.setDependentPhn6(request.getDependentPhn7());

		RPBSPXP0 rpbspxp0 = new RPBSPXP0();
		rpbspxp0.setRpbsHeader(rpbsHeader);
		rpbspxp0.setXp0(xp0);

		return rpbspxp0;
	}
	
	public AddGroupMemberResponse convertResponse(RPBSPXP0 rpbspxp0) {
		AddGroupMemberResponse response = new AddGroupMemberResponse();
		RPBSHeader header = rpbspxp0.getRpbsHeader();

		// If the result is an error and the phnInError is set, prepend to the existing message
		// This is existing legacy behaviour
		handleStatus(header, response, rpbspxp0.getXp0().getPhnInError());
		response.setPhn(rpbspxp0.getXp0().getPhn());
		
		return response;
	}
	
	public RenewCancelledGroupCoverageResponse convertResponseForRenewal(RPBSPXP0 rpbspxp0) {
		RenewCancelledGroupCoverageResponse response = new RenewCancelledGroupCoverageResponse();
		RPBSHeader header = rpbspxp0.getRpbsHeader();
		
		handleStatus(header, response);
		response.setPhn(rpbspxp0.getXp0().getPhn());
		
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