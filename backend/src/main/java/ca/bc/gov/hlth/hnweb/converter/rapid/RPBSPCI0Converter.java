package ca.bc.gov.hlth.hnweb.converter.rapid;

import org.apache.commons.lang3.StringUtils;

import ca.bc.gov.hlth.hnweb.model.rapid.CI0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPCI0;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryRequest;
import ca.bc.gov.hlth.hnweb.model.rest.mspcontracts.ContractInquiryResponse;

public class RPBSPCI0Converter extends BaseRapidConverter {
	private static final String TRAN_CODE = "RPBSPCI0";

	public RPBSPCI0Converter() {
		super();
	}

	public RPBSPCI0 convertRequest(ContractInquiryRequest request) {
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());

		CI0 ci0 = new CI0();
		ci0.setPhn(request.getPhn());
		ci0.setGroupNumber(request.getGroupNumber());

		RPBSPCI0 rpbspci0 = new RPBSPCI0();
		rpbspci0.setRpbsHeader(rpbsHeader);
		rpbspci0.setCi0(ci0);

		return rpbspci0;
	}

	public ContractInquiryResponse convertResponse(RPBSPCI0 rpbspci0) {
		ContractInquiryResponse response = new ContractInquiryResponse();
		RPBSHeader header = rpbspci0.getRpbsHeader();

		handleStatus(header, response);

		CI0 ci0 = rpbspci0.getCi0();
		// Populate Person info
		response.setPhn(ci0.getPhn());
		response.setGroupNumber(ci0.getGroupNumber());
		response.setGroupMemberNumber(ci0.getEmployeeNumber());
		response.setGroupMemberDepartmentNumber(ci0.getDepartmentNumber());

		response.setHomeAddressLine1(ci0.getHomeAddress().getAddressLine1());
		response.setHomeAddressLine2(ci0.getHomeAddress().getAddressLine2());
		response.setHomeAddressLine3(ci0.getHomeAddress().getAddressLine3());
		response.setHomeAddressLine4(ci0.getHomeAddress().getAddressLine4());
		response.setHomeAddressPostalCode(ci0.getHomeAddress().getPostalCode());

		response.setMailingAddressLine1(ci0.getMailAddress().getAddressLine1());
		response.setMailingAddressLine2(ci0.getMailAddress().getAddressLine2());
		response.setMailingAddressLine3(ci0.getMailAddress().getAddressLine3());
		response.setMailingAddressLine4(ci0.getMailAddress().getAddressLine4());
		response.setMailingAddressPostalCode(ci0.getMailAddress().getPostalCode());

		response.setTelephone(ci0.getPhone().getPhoneAreaCode() + " " + ci0.getPhone().getPhoneNumber());

		for (RPBSBeneficiary rpbsBeneficiary : ci0.getBeneficiary()) {
			ContractInquiryBeneficiary beneficiary = new ContractInquiryBeneficiary();
			beneficiary.setPhn(rpbsBeneficiary.getPhn());
			beneficiary.setFamilyName(StringUtils.trim(rpbsBeneficiary.getFamilyName()));
			beneficiary.setFirstName(StringUtils.trim(rpbsBeneficiary.getFirstName()));
			beneficiary.setSecondName(StringUtils.trim(rpbsBeneficiary.getSecondName()));
			beneficiary.setThirdName(StringUtils.trim(rpbsBeneficiary.getThirdName()));
			// Convert the response Date from yyyy-MM-dd to yyyyMMdd
			beneficiary.setBirthDate(StringUtils.remove(rpbsBeneficiary.getBirthDate(), "-"));
			beneficiary.setEffectiveDate(StringUtils.remove(rpbsBeneficiary.getEffectiveDate(), "-"));
			beneficiary.setCancelDate(StringUtils.remove(rpbsBeneficiary.getCancelDate(), "-"));
			beneficiary.setGender(rpbsBeneficiary.getGender());
			beneficiary.setCancelReason(rpbsBeneficiary.getCancelReason());
			beneficiary.setStudentStatus(rpbsBeneficiary.getStudentStatus());
			beneficiary.setRelationshipCode(rpbsBeneficiary.getRelationshipCode());
			response.getContractInquiryBeneficiaries().add(beneficiary);
		}

		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}