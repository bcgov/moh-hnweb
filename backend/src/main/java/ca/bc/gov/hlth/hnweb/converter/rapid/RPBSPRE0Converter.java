package ca.bc.gov.hlth.hnweb.converter.rapid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		LocalDate studentEndDate = request.getStudentEndDate() != null ? request.getStudentEndDate() : calculateStudentEndDate(request.getDependentDateOfBirth());
		re0.setStudentEndDate(studentEndDate.format(DateTimeFormatter.ofPattern(RAPID_YYYY_MM_FORMAT)));

		RPBSPRE0 rpbspre0 = new RPBSPRE0();
		rpbspre0.setRpbsHeader(rpbsHeader);
		rpbspre0.setRe0(re0);

		return rpbspre0;
	}
	
	public ReinstateOverAgeDependentResponse convertResponse(RPBSPRE0 rpbspre0) {
		ReinstateOverAgeDependentResponse response = new ReinstateOverAgeDependentResponse();
		RPBSHeader header = rpbspre0.getRpbsHeader();
		
		handleStatus(header, response);
		
		response.setPhn(rpbspre0.getRe0().getSubscriberPHN());
		
		return response;
	}

	@Override
	public String getTranCode() {
		return TRAN_CODE;
	}
	
	private LocalDate calculateStudentEndDate(LocalDate birthDate) {
		// The end date is to be stripped of the dd portion. If the end date is empty then a new end date
		// is to be made up from the dependents birth date and the current year plus one
		return LocalDate.of(LocalDate.now().getYear(), birthDate.getMonth(), birthDate.getDayOfYear()).plusYears(1);
	}

}