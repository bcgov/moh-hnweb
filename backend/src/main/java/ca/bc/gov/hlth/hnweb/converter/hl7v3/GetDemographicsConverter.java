package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.rest.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.rest.enrollment.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.Address;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.model.v3.Person;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

/**
 * Converter class for V3 messages Contains methods to facilitate converter a
 * GetPersonDeatilsRequest to and GetDemographics and from
 * GetDemographicsRsponse to a GetPersonDeatilsResponse
 *
 */

public class GetDemographicsConverter {

	private static final Logger logger = LoggerFactory.getLogger(GetDemographicsConverter.class);
	private static final String MRN_SOURCE = "MOH_CRS";

	/**
	 * @param phn
	 * @return
	 */
	public GetDemographicsRequest convertRequest(String phn) {
		logger.debug("Get Demographics details for PHN [{}]", phn);

		GetDemographicsRequest demographicsRequest = new GetDemographicsRequest();
		demographicsRequest.setPhn(phn);
		demographicsRequest.setMrnSource(MRN_SOURCE);

		return demographicsRequest;

	}

	/**
	 * @param demographicsResponse
	 * @return
	 * @throws IOException
	 */
	public GetPersonDetailsResponse convertResponse(GetDemographicsResponse demographicsResponse) throws IOException {
		logger.debug("Demographics response : {} ", demographicsResponse.toString());

		GetPersonDetailsResponse getPersonDetailsResponse = buildGetPersonDetailsResponse(demographicsResponse);
		logger.debug("Converted PersonDetails Response : {} ", getPersonDetailsResponse);
		return getPersonDetailsResponse;

	}

	private GetPersonDetailsResponse buildGetPersonDetailsResponse(GetDemographicsResponse demographicsResponse) {
		GetPersonDetailsResponse getPersonDetailsResponse = new GetPersonDetailsResponse();

		// BCHCIM.GD.0.0018 | No results were returned. Please refine your search
		// criteria, and try again.

		// BCHCIM.GD.1.0015 | Warning: The identifier you used in the query has been
		// merged. The surviving identifier was returned.

		// BCHCIM.GD.2.0004 | Error: The EMPI is unavailable. Please report the problem
		// to the helpdesk.

		String messageDetails = demographicsResponse.getMessage().getDetails();
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
			getPersonDetailsResponse.setStatus(StatusEnum.WARNING);
		} else if (status.equalsIgnoreCase(StatusEnum.ERROR.name())) {
			message = messageStr[1];
			getPersonDetailsResponse.setStatus(StatusEnum.ERROR);
		} else {
			getPersonDetailsResponse.setStatus(StatusEnum.SUCCESS);
		}
		getPersonDetailsResponse.setMessage(String.format("%s%s", statusCode, message));

		if (demographicsResponse.getResultCount() > 0 && demographicsResponse.getPerson() != null) {
			buildPersonDetails(demographicsResponse, getPersonDetailsResponse);
		}
		logger.debug("Response message received for phn: {}", getPersonDetailsResponse.getPhn());
		return getPersonDetailsResponse;

	}

	private void buildPersonDetails(GetDemographicsResponse demographicsResponse,
			GetPersonDetailsResponse personDetailsResponse) {
		Person person = demographicsResponse.getPerson();
		// "Documented" should always be shown over a "Declared" name.
		Name name = person.getDocumentedName();
		if (name == null) {
			name = person.getDeclaredName();
		}

		if (name != null) {
			personDetailsResponse.setPhn(person.getPhn());
			personDetailsResponse.setGivenName(name.getFirstGivenName());
			personDetailsResponse.setSecondName(name.getSecondGivenName());
			personDetailsResponse.setSurname(name.getSurname());

			if (person.getBirthDate() != null) {
				String birthDate = V3MessageUtil.convertDateToString(person.getBirthDate());
				personDetailsResponse.setDateOfBirth(birthDate);
			}
			
			String dateOfDeath = person.getDeathDate() != null ? V3MessageUtil.convertDateToString(person.getDeathDate()) :"N/A";			
			personDetailsResponse.setDateOfDeath(dateOfDeath);
			
			personDetailsResponse.setGender(person.getGender());
		}
		
		Address physicalAddress = demographicsResponse.getPerson().getPhysicalAddress();
		if (physicalAddress != null) {
			personDetailsResponse.setAddress1(physicalAddress.getAddressLine1());
			personDetailsResponse.setAddress2(physicalAddress.getAddressLine2());
			personDetailsResponse.setAddress3(physicalAddress.getAddressLine3());
			personDetailsResponse.setCity(physicalAddress.getCity());
			personDetailsResponse.setProvince(physicalAddress.getProvince());
			personDetailsResponse.setPostalCode(physicalAddress.getPostalCode());
		}

		Address mailingAddress = demographicsResponse.getPerson().getMailingAddress();
		// Populate mailingAddress if different than physical address
		if (mailingAddress != null && !mailingAddress.equals(physicalAddress)) {
			personDetailsResponse.setMailingAddress1(mailingAddress.getAddressLine1());
			personDetailsResponse.setMailingAddress2(mailingAddress.getAddressLine2());
			personDetailsResponse.setMailingAddress3(mailingAddress.getAddressLine3());
			personDetailsResponse.setMailingAddressCity(mailingAddress.getCity());
			personDetailsResponse.setMailingAddressProvince(mailingAddress.getProvince());
			personDetailsResponse.setMailingAddressPostalCode(mailingAddress.getPostalCode());
		}
	}

}
