package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import ca.bc.gov.hlth.hnweb.model.GetNameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.NameSearchResult;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.Address;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResult;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

public class FindCandidatesConverter {

	private static final String WARNING = "Warning";
	private static final Logger logger = LoggerFactory.getLogger(FindCandidatesConverter.class);

	public FindCandidatesRequest convertRequest(String surname, String firstName, String secondName, String dateOfBirth,
			String gender) {
		logger.debug("Find Candidates for Name: [{}] DOB: [{}]", surname + firstName, dateOfBirth);

		FindCandidatesRequest findCandidatesRequest = new FindCandidatesRequest();
		findCandidatesRequest.setSurname(surname);
		findCandidatesRequest.setFirstName(firstName);
		findCandidatesRequest.setSecondName(secondName);
		findCandidatesRequest.setDateOfBirth(dateOfBirth);
		findCandidatesRequest.setGender(gender);

		return findCandidatesRequest;

	}

	public GetNameSearchResponse convertResponse(FindCandidatesResponse findCandidatesResponse) throws IOException {
		logger.debug("Find Candidates response : {} ", findCandidatesResponse.toString());

		GetNameSearchResponse getNameSearchResponse = buildGetNameSearchResponse(findCandidatesResponse);
		logger.debug("Converted Name Search Response : {} ", getNameSearchResponse);
		return getNameSearchResponse;

	}

	private GetNameSearchResponse buildGetNameSearchResponse(FindCandidatesResponse findCandidatesResponse) {
		GetNameSearchResponse getNameSearchResponse = new GetNameSearchResponse();

		String messageDetails = findCandidatesResponse.getMessage().getDetails();
		String messageText[] = messageDetails.split("\\|");
		String message = "";
		if (messageText.length > 1) {
			message = messageText[1];
		}
		if (findCandidatesResponse.getResultCount() == 0) {
			logger.debug("No result found ");

			if (messageText.length > 0) {
				getNameSearchResponse.setStatus(StatusEnum.ERROR);
				getNameSearchResponse.setMessage(message);
			}
		} else {
			buildNameSearch(findCandidatesResponse, getNameSearchResponse);
			if (messageText.length > 0) {
				if (message.contains(WARNING)) {
					getNameSearchResponse.setStatus(StatusEnum.WARNING);
					getNameSearchResponse.setMessage(messageText[1]);
				} else {
					getNameSearchResponse.setStatus(StatusEnum.SUCCESS);
				}
			}

		}

		return getNameSearchResponse;

	}

	private void buildNameSearch(FindCandidatesResponse findCandidatesResponse,
			GetNameSearchResponse getNameSearchResponse) {
		List<NameSearchResult> nameSearchList = new ArrayList<NameSearchResult>();

		List<FindCandidatesResult> candidatesResult = findCandidatesResponse.getResults();

		if (!CollectionUtils.isEmpty(candidatesResult)) {
			candidatesResult.forEach(ns -> {
				NameSearchResult nameSearchResult = new NameSearchResult();
				nameSearchResult.setPhn(ns.getPerson().getPhn());
				Name nameObj = ns.getPerson().getDeclaredName();
				if (nameObj == null) {
					nameObj = ns.getPerson().getDocumentedName();
				}
				nameSearchResult.setGender(ns.getPerson().getGender());
				nameSearchResult.setGivenName(nameObj.getFirstGivenName());
				nameSearchResult.setSecondName(nameObj.getSecondGivenName());
				nameSearchResult.setSurname(nameObj.getSurname());

				String birthDate = V3MessageUtil.convertDateToString(ns.getPerson().getBirthDate());
				nameSearchResult.setDateOfBirth(birthDate);

				Address address = ns.getPerson().getPhysicalAddress();
				if (address != null) {
					nameSearchResult.setAddressLine1(ns.getPerson().getPhysicalAddress().getAddressLine1());
					nameSearchResult.setCity(ns.getPerson().getPhysicalAddress().getCity());
					nameSearchResult.setProvince(ns.getPerson().getPhysicalAddress().getProvince());
					nameSearchResult.setPostalCode(ns.getPerson().getPhysicalAddress().getPostalCode());
				}

				nameSearchResult.setScore(ns.getScore());
				nameSearchList.add(nameSearchResult);

			});

		}

		getNameSearchResponse.setResults(nameSearchList);
	}

}
