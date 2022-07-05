package ca.bc.gov.hlth.hnweb.converter.rapid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.rapid.RPBSHeader;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0Beneficiary;
import ca.bc.gov.hlth.hnweb.model.rapid.RPBSPPE0Data;
import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnBeneficiary;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnRequest;
import ca.bc.gov.hlth.hnweb.model.rest.eligibility.InquirePhnResponse;

public class RPBSPPE0Converter extends BaseRapidConverter {
	
	protected static final Logger logger = LoggerFactory.getLogger(RPBSPPE0Converter.class);

	private static final String TRAN_CODE = "RPBSPPE0";

	public RPBSPPE0Converter() {
		super();
	}
	
	public RPBSPPE0 convertRequest(InquirePhnRequest request) {
		String eligibilityDate = new SimpleDateFormat(RAPID_DATE_FORMAT).format(new Date());
		
		RPBSHeader rpbsHeader = new RPBSHeader();
		rpbsHeader.setOrganization(userInfo.getOrganization());
		rpbsHeader.setTranCode(getTranCode());		
		
		RPBSPPE0Data data = new RPBSPPE0Data();
		data.setPhns(request.getPhns());
		data.setEligibilityDate(eligibilityDate);
		
		RPBSPPE0 rpbsppe0 = new RPBSPPE0();
		rpbsppe0.setRpbsHeader(rpbsHeader);
		rpbsppe0.setRpbsData(data);

		return rpbsppe0;
	}
	
	public InquirePhnResponse convertResponse(RPBSPPE0 rpbspe0) {
		InquirePhnResponse response = new InquirePhnResponse();

		RPBSHeader header = rpbspe0.getRpbsHeader();
		RPBSPPE0Data data = rpbspe0.getRpbsData();
		
		// If no results were returned check the header handle the status and return
		if (data.getBeneficiaries().isEmpty()) {
			handleStatus(header, response);
			return response;
		}

		Boolean hasWarning = Boolean.FALSE;
		String successMessage = null;
		List<String> warningMessages = new ArrayList<>();
		
		for (RPBSPPE0Beneficiary beneficiary: data.getBeneficiaries()) {
			// Handle the individual statuses
			String statusCode = beneficiary.getStatusCode();
			String statusText = StringUtils.trimToEmpty(beneficiary.getStatusText());

			if (StringUtils.equals(statusCode, STATUS_CODE_SUCCESS)) {
				successMessage = String.format("%s %s", statusCode, SUCCESS_MESSAGE);
				
				// Map each record
				InquirePhnBeneficiary ipBeneficiary = new InquirePhnBeneficiary();
				ipBeneficiary.setPhn(beneficiary.getPhn());
				ipBeneficiary.setLastName(StringUtils.trim(beneficiary.getLastName()));
				ipBeneficiary.setFirstName(StringUtils.trim(beneficiary.getFirstName()));			
				ipBeneficiary.setDateOfBirth(convertDate(beneficiary.getBirthDate()));
				ipBeneficiary.setGender(beneficiary.getGender());
				ipBeneficiary.setEligible(beneficiary.getEligible());
				ipBeneficiary.setStudent(beneficiary.getStudent());
				response.getBeneficiaries().add(ipBeneficiary);
				
			} else {
				hasWarning = true;
				warningMessages.add(statusCode + " " + statusText + " (" + beneficiary.getPhn() + ")");
			}
		}
		if (hasWarning) {
			response.setStatus(StatusEnum.WARNING);
			response.setMessage(String.join("\n", warningMessages));
		} else {
			response.setStatus(StatusEnum.SUCCESS);
			// If the entire result is a success then just return a single message
			response.setMessage(successMessage);
		}
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}

}