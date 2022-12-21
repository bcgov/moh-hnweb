package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.NameSearchResult;
import ca.bc.gov.hlth.hnweb.model.v3.Address;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResult;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.model.v3.Person;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

public class FindCandidatesConverter {

	private static final String IDENTIFIER_TYPE_CODE = "PH";
	private static final String ASSIGNING_AUTHORITY = "BC";
	private static final Logger logger = LoggerFactory.getLogger(FindCandidatesConverter.class);

	public FindCandidatesRequest convertRequest(NameSearchRequest nameSearchRequest) {
		String surname = nameSearchRequest.getSurname();
		String givenName = nameSearchRequest.getGivenName();

		logger.debug("Find Candidates for Name: [{} {}] DOB: [{}]", surname, givenName,
				nameSearchRequest.getDateOfBirth());

		FindCandidatesRequest findCandidatesRequest = new FindCandidatesRequest();

		Name name = new Name();
		name.setSurname(surname);
		name.setFirstGivenName(givenName);
		name.setSecondGivenName(nameSearchRequest.getSecondName());

		findCandidatesRequest.setName(name);
		findCandidatesRequest.setBirthDate(V3MessageUtil.dateOnlyFormatter.format(nameSearchRequest.getDateOfBirth()));
		String gender = StringUtils.equals(nameSearchRequest.getGender(), "U") ? "UNK":nameSearchRequest.getGender();
		findCandidatesRequest.setGender(gender);
		return findCandidatesRequest;

	}

	public NameSearchResponse convertResponse(FindCandidatesResponse findCandidatesResponse) throws IOException {
		logger.debug("Find Candidates response : {} ", findCandidatesResponse.toString());

		NameSearchResponse nameSearchResponse = new NameSearchResponse();

		String messageDetails = findCandidatesResponse.getMessage().getDetails();

		// BCHCIM.FC.0.0017 | Warning: The maximum number of results were returned, and
		// more may be available. Please refine your search criteria and try again.

		// BCHCIM.FC.0.0018 | No candidates found. Please refine your search.

		// BCHCIM.FC.2.0004 | Error: The EMPI is unavailable. Please report the problem
		// to the helpdesk.

		String messageText[] = messageDetails.split("\\|");
		String message = "";
		String statusCode = "";
		if (messageText.length > 1) {
			statusCode = messageText[0];
			message = messageText[1];
		}

		String[] messageStr = message.split(":");
		String status = messageStr[0].trim();

		if (status.equalsIgnoreCase(StatusEnum.WARNING.name())) {
			message = messageStr[1];
			nameSearchResponse.setStatus(StatusEnum.WARNING);
		} else if (status.equalsIgnoreCase(StatusEnum.ERROR.name())) {
			message = messageStr[1];
			nameSearchResponse.setStatus(StatusEnum.ERROR);
		} else {
			nameSearchResponse.setStatus(StatusEnum.SUCCESS);
		}

		nameSearchResponse.setMessage(String.format("%s%s", statusCode, message));

		if (findCandidatesResponse.getResultCount() > 0) {

			List<NameSearchResult> results = buildNameSearch(findCandidatesResponse);
			nameSearchResponse.setCandidates(results);
			logger.debug("Converted Name Search Response : {} ", nameSearchResponse);
		}

		return nameSearchResponse;

	}

	private List<NameSearchResult> buildNameSearch(FindCandidatesResponse findCandidatesResponse) {
		List<NameSearchResult> nameSearchList = new ArrayList<NameSearchResult>();

		List<FindCandidatesResult> candidatesResult = findCandidatesResponse.getResults();

		if (CollectionUtils.isEmpty(candidatesResult)) {
			return nameSearchList;
		}

		candidatesResult.forEach(ns -> {
			Person person = ns.getPerson();
			
			NameSearchResult nameSearchResult = new NameSearchResult();
			nameSearchResult.setPhn(person.getPhn());
			nameSearchResult.setIdentifierTypeCode(IDENTIFIER_TYPE_CODE);
			nameSearchResult.setAssigningAuthority(ASSIGNING_AUTHORITY);

			// "Documented" should always be shown over a "Declared" name.
			Name name = person.getDocumentedName();
			if (name == null) {
				name = person.getDeclaredName();
			}

			if (name != null) {
				nameSearchResult.setGivenName(Optional.ofNullable(name.getFirstGivenName()).orElse(""));
				nameSearchResult.setSecondName(Optional.ofNullable(name.getSecondGivenName()).orElse(""));
				nameSearchResult.setSurname(Optional.ofNullable(name.getSurname()).orElse(""));
				nameSearchResult.setNameTypeCode(Optional.ofNullable(name.getType()).orElse(""));

				if (person.getBirthDate() != null) {
					String birthDate = V3MessageUtil.convertDateToString(person.getBirthDate());
					nameSearchResult.setDateOfBirth(birthDate);					
				}
				
				String dateOfDeath = person.getDeathDate() != null ? V3MessageUtil.convertDateToString(person.getDeathDate()) : "N/A";	
				nameSearchResult.setDateOfDeath(dateOfDeath);
				
				nameSearchResult.setGender(person.getGender());

				Address physicalAddress = ns.getPerson().getPhysicalAddress();
				if (physicalAddress != null) {
					nameSearchResult.setAddress1(physicalAddress.getAddressLine1());
					nameSearchResult.setAddress2(physicalAddress.getAddressLine2());
					nameSearchResult.setAddress3(physicalAddress.getAddressLine3());
					nameSearchResult.setCity(physicalAddress.getCity());
					nameSearchResult.setProvince(physicalAddress.getProvince());
					nameSearchResult.setPostalCode(physicalAddress.getPostalCode());
				}

				Address mailingAddress = ns.getPerson().getMailingAddress();
				// Populate mailingAddress if different than physical address
				if (mailingAddress != null && !mailingAddress.equals(physicalAddress)) {
					nameSearchResult.setMailingAddress1(mailingAddress.getAddressLine1());
					nameSearchResult.setMailingAddress2(mailingAddress.getAddressLine2());
					nameSearchResult.setMailingAddress3(mailingAddress.getAddressLine3());
					nameSearchResult.setMailingAddressCity(mailingAddress.getCity());
					nameSearchResult.setMailingAddressProvince(mailingAddress.getProvince());
					nameSearchResult.setMailingAddressPostalCode(mailingAddress.getPostalCode());
				}

				nameSearchResult.setScore(ns.getScore());
				nameSearchList.add(nameSearchResult);
			}

		});

		return nameSearchList;
	}

}
