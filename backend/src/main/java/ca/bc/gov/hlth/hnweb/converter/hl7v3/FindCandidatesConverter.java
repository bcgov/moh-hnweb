package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import ca.bc.gov.hlth.hnweb.model.NameSearchRequest;
import ca.bc.gov.hlth.hnweb.model.NameSearchResponse;
import ca.bc.gov.hlth.hnweb.model.NameSearchResult;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.Address;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesRequest;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResponse;
import ca.bc.gov.hlth.hnweb.model.v3.FindCandidatesResult;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

public class FindCandidatesConverter {

	private static final String IDENTIFIER_TYPE_CODE = "PH";
	private static final String ASSIGNING_AUTHORITY = "BC";
	private static final Logger logger = LoggerFactory.getLogger(FindCandidatesConverter.class);

	public FindCandidatesRequest convertRequest(NameSearchRequest nameSearchRequest) {
		logger.debug("Find Candidates for Name: [{}] DOB: [{}]",
				nameSearchRequest.getSurname() + nameSearchRequest.getGivenName(),
				nameSearchRequest.getDateOfBirth());

		FindCandidatesRequest findCandidatesRequest = new FindCandidatesRequest();

		Name name = new Name();
		name.setSurname(nameSearchRequest.getSurname());
		name.setFirstGivenName(nameSearchRequest.getGivenName());
		name.setSecondGivenName(nameSearchRequest.getSecondName());

		findCandidatesRequest.setName(name);
		findCandidatesRequest
				.setBirthDate(V3MessageUtil.dateOnlyFormatter.format(nameSearchRequest.getDateOfBirth()));
		findCandidatesRequest.setGender(nameSearchRequest.getGender());

		return findCandidatesRequest;

	}

	public NameSearchResponse convertResponse(FindCandidatesResponse findCandidatesResponse) throws IOException {
		logger.debug("Find Candidates response : {} ", findCandidatesResponse.toString());

		NameSearchResponse nameSearchResponse = new NameSearchResponse();
		
		String messageDetails = findCandidatesResponse.getMessage().getDetails();
		String messageText[] = messageDetails.split("\\|");
		String message = "";
		if (messageText.length > 1) {
			message = messageText[1];
		}
		nameSearchResponse.setMessage(message);
		nameSearchResponse.setStatus(StatusEnum.SUCCESS);

		if (findCandidatesResponse.getResultCount() > 0) {

			List<NameSearchResult> results = buildNameSearch(findCandidatesResponse);
			nameSearchResponse.setCandidates(results);
			logger.debug("Converted Name Search Response : {} ", nameSearchResponse);
		}
				
		return nameSearchResponse;

	}

	private List<NameSearchResult> buildNameSearch(FindCandidatesResponse findCandidatesResponse)
	{
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
			nameSearchResult.setGender(ns.getPerson().getGender());
			nameSearchResult.setGivenName(nameObj.getFirstGivenName());
			nameSearchResult.setSecondName(Optional.ofNullable(nameObj.getSecondGivenName()).orElse(""));
			nameSearchResult.setSurname(nameObj.getSurname());
			nameSearchResult.setNameTypeCode(nameObj.getType());

			String birthDate = V3MessageUtil.convertDateToString(ns.getPerson().getBirthDate());
			nameSearchResult.setDateOfBirth(birthDate);

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

		});

		return nameSearchList;
	}

}
