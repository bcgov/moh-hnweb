package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

public class FindCandidatesConverter {

	private static final String WARNING = "Warning";
	private static final String ERROR = "Error";
	private static final String IDENTIFIER_TYPE_CODE = "PH";
	private static final String ASSIGNING_AUTHORITY = "BC";
	private static final Logger logger = LoggerFactory.getLogger(FindCandidatesConverter.class);

	public FindCandidatesRequest convertRequest(NameSearchRequest nameSearchRequest) {
		logger.debug("Find Candidates for Name: [{}] DOB: [{}]",
				nameSearchRequest.getSurname() + nameSearchRequest.getGivenName(), nameSearchRequest.getDateOfBirth());

		FindCandidatesRequest findCandidatesRequest = new FindCandidatesRequest();

		Name name = new Name();
		name.setSurname(nameSearchRequest.getSurname());
		name.setFirstGivenName(nameSearchRequest.getGivenName());
		name.setSecondGivenName(nameSearchRequest.getSecondName());

		findCandidatesRequest.setName(name);
		findCandidatesRequest.setBirthDate(V3MessageUtil.dateOnlyFormatter.format(nameSearchRequest.getDateOfBirth()));
		findCandidatesRequest.setGender(nameSearchRequest.getGender());

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
		if (messageText.length > 1) {
			message = messageText[1];
		}
		nameSearchResponse.setMessage(message);
		String[] messageStr = message.split(":");
		String status = messageStr[0].trim();

		if (status.contentEquals(WARNING)) {
			nameSearchResponse.setStatus(StatusEnum.WARNING);
			nameSearchResponse.setMessage(messageStr[1]);
		} else if (status.contentEquals(ERROR)) {
			nameSearchResponse.setStatus(StatusEnum.ERROR);
			nameSearchResponse.setMessage(messageStr[1]);
		} else
			nameSearchResponse.setStatus(StatusEnum.SUCCESS);

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
			NameSearchResult nameSearchResult = new NameSearchResult();
			nameSearchResult.setPhn(ns.getPerson().getPhn());
			nameSearchResult.setIdentifierTypeCode(IDENTIFIER_TYPE_CODE);
			nameSearchResult.setAssigningAuthority(ASSIGNING_AUTHORITY);

			Name nameObj = ns.getPerson().getDeclaredName();
			if (nameObj == null) {
				nameObj = ns.getPerson().getDocumentedName();
			}

			if (nameObj != null) {
				nameSearchResult.setGivenName(Optional.ofNullable(nameObj.getFirstGivenName()).orElse(""));
				nameSearchResult.setSecondName(Optional.ofNullable(nameObj.getSecondGivenName()).orElse(""));
				nameSearchResult.setSurname(Optional.ofNullable(nameObj.getSurname()).orElse(""));
				nameSearchResult.setNameTypeCode(Optional.ofNullable(nameObj.getType()).orElse(""));

				String birthDate = V3MessageUtil.convertDateToString(ns.getPerson().getBirthDate());
				nameSearchResult.setDateOfBirth(birthDate);
				nameSearchResult.setGender(ns.getPerson().getGender());

				Address address = ns.getPerson().getPhysicalAddress();
				if (address != null) {
					nameSearchResult.setAddress1(ns.getPerson().getPhysicalAddress().getAddressLine1());
					nameSearchResult.setAddress2(ns.getPerson().getPhysicalAddress().getAddressLine2());
					nameSearchResult.setAddress3(ns.getPerson().getPhysicalAddress().getAddressLine3());
					nameSearchResult.setCity(ns.getPerson().getPhysicalAddress().getCity());
					nameSearchResult.setProvince(ns.getPerson().getPhysicalAddress().getProvince());
					nameSearchResult.setPostalCode(ns.getPerson().getPhysicalAddress().getPostalCode());
				}

				Address mailingAddress = ns.getPerson().getMailingAddress();
				if (mailingAddress != null) {
					nameSearchResult.setAddress1(ns.getPerson().getMailingAddress().getAddressLine1());
					nameSearchResult.setAddress2(ns.getPerson().getMailingAddress().getAddressLine2());
					nameSearchResult.setAddress3(ns.getPerson().getMailingAddress().getAddressLine3());
					nameSearchResult.setCity(ns.getPerson().getMailingAddress().getCity());
					nameSearchResult.setProvince(ns.getPerson().getMailingAddress().getProvince());
					nameSearchResult.setPostalCode(ns.getPerson().getMailingAddress().getPostalCode());
				}

				nameSearchResult.setScore(ns.getScore());
				nameSearchList.add(nameSearchResult);
			}

		});

		return nameSearchList;
	}

}
