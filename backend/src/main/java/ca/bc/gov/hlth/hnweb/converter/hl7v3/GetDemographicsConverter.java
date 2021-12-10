package ca.bc.gov.hlth.hnweb.converter.hl7v3;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
import ca.bc.gov.hlth.hnweb.serialization.HL7Config;
import ca.bc.gov.hlth.hnweb.serialization.HL7Serializer;
import ca.bc.gov.hlth.hnweb.util.V2MessageUtil;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

/**
 * Converter class for V3 messages Contains methods to facilitate converter a
 * GetPersonDeatilsRequest to and GetDemographics and from
 * GetDemographicsRsponse to a GetPersonDeatilsResponse
 *
 */

public class GetDemographicsConverter {

	private static final String WARNING = "Warning";
	private static final Logger logger = LoggerFactory.getLogger(GetDemographicsConverter.class);
	private static final String MRN_SOURCE = "MOH_CRS";

	/**
	 * @param phn
	 * @return
	 */
	public GetDemographicsRequest convertRequest(String phn) {
		logger.debug("Get Demographics details for PHN [{}]", phn);

		GetDemographicsRequest demographicsRequest = buildDemographicsRequest(phn);
		return demographicsRequest;

	}

	/**
	 * @param demographicsResponse
	 * @return
	 * @throws IOException
	 */
	public GetPersonDetailsResponse convertResponse(GetDemographicsResponse demographicsResponse) throws IOException {
		logger.debug("Demographics response : {} ", demographicsResponse.toString());

		GetPersonDetailsResponse getPersonDetailsResponse = handleStatus(demographicsResponse);
		logger.debug("Converted PersonDetails Response : {} ", getPersonDetailsResponse);
		return getPersonDetailsResponse;

	}

	private GetPersonDetailsResponse handleStatus(GetDemographicsResponse demographicsResponse) {
		GetPersonDetailsResponse getPersonDetailsResponse = new GetPersonDetailsResponse();

		String messageDetails = demographicsResponse.getMessage().getDetails();
		String messageText[] = messageDetails.split("\\|");
		String message = "";
		if (messageText.length > 1) {
			message = messageText[1];
		}
		if (demographicsResponse.getResultCount() == 0) {
			logger.debug("No result found for the Phn [{}]", demographicsResponse.getPerson().getPhn());

			if (messageText.length > 0) {
				getPersonDetailsResponse.setStatus(StatusEnum.ERROR);
				getPersonDetailsResponse.setMessage(message);
			}
		} else {
			buildPersonDetails(demographicsResponse, getPersonDetailsResponse);
			if (messageText.length > 0) {
				if (message.contains(WARNING)) {
					getPersonDetailsResponse.setStatus(StatusEnum.WARNING);
					getPersonDetailsResponse.setMessage(messageText[1]);
				} else {
					getPersonDetailsResponse.setStatus(StatusEnum.SUCCESS);
				}
			}

			logger.debug("Response message received for phn: {}", getPersonDetailsResponse.getPhn());
		}

		return getPersonDetailsResponse;

	}

	private void buildPersonDetails(GetDemographicsResponse demographicsResponse,
			GetPersonDetailsResponse personDetailsResponse) {				
		Name nameObj = demographicsResponse.getPerson().getDeclaredName();		
		if (nameObj == null) {
			nameObj = demographicsResponse.getPerson().getDocumentedName();
		}

		personDetailsResponse.setPhn(demographicsResponse.getPerson().getPhn());
		personDetailsResponse.setGivenName(nameObj.getFirstGivenName());
		personDetailsResponse.setSecondName(nameObj.getSecondGivenName());
		personDetailsResponse.setSurname(nameObj.getSurname());

		String birthDate = new SimpleDateFormat(V2MessageUtil.DATE_FORMAT_DATE_ONLY).format(demographicsResponse.getPerson().getBirthDate());
		personDetailsResponse.setDateOfBirth(birthDate);
		personDetailsResponse.setGender(demographicsResponse.getPerson().getGender());
	}

	private GetDemographicsRequest buildDemographicsRequest(String phn) {
		GetDemographicsRequest getDemographics = new GetDemographicsRequest();
		getDemographics.setPhn(phn);
		getDemographics.setMrnSource(MRN_SOURCE);
		logger.debug("Creating request for the phn : {}", getDemographics.getPhn());

		return getDemographics;

	}

}
