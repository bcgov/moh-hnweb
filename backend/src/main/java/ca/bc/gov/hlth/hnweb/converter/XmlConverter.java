package ca.bc.gov.hlth.hnweb.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.config.HL7Config;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.PersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.serialization.HL7Serializer;
import ca.bc.gov.hlth.hnweb.util.V3MessageUtil;

/**
 * Converter class for V3 messages Contains methods to facilitate converter a
 * GetPersonDeatilsRequest to and GetDemographics and from
 * GetDemographicsRsponse to a GetPersonDeatilsResponse
 *
 */

public class XmlConverter {

	private static final Logger logger = LoggerFactory.getLogger(XmlConverter.class);
	private static final String dataEntererExt = "SOURCESYSTEMUSERNAME";
	private static final String sourceSystemOverride = "MOH_CRS";
	private static final String organization = "MOH_CRS";
	private static final String mrn_source = "MOH_CRS";
	protected HL7Serializer hl7;
	protected MessageMetaData mmd;

	public XmlConverter(String transectionId) {
		hl7 = new HL7Serializer(new HL7Config());
		mmd = new MessageMetaData(dataEntererExt, sourceSystemOverride, organization, transectionId);
	}

	/**
	 * Serializes and creates soap wrapper around xml request.
	 * @param phn
	 * @return
	 */
	public String convertRequest(String phn) {
		logger.debug("Get Demographics details for PHN [{}]", phn);

		GetDemographicsRequest request = buildDemographicsRequest(phn);
		Object formattedRequest = hl7.toXml(request, mmd);
		String requestObj = V3MessageUtil.wrap(formattedRequest.toString());
		logger.debug("Get Demographics wrapped xml request[{}]", requestObj);
		return requestObj;

	}

	/**
	 * @param xmlString
	 * @return
	 * @throws IOException
	 */
	public PersonDetailsResponse convertResponse(String xmlString) throws IOException {
		GetDemographicsResponse results = hl7.fromXml(xmlString, GetDemographicsResponse.class);
		logger.debug("Converted Demographics response : {} ", results.toString());
		
		PersonDetailsResponse personDetailsResponse = buildPersonDetailsResponse(results);
		logger.debug("Converted PersonDetails Response : {} ", personDetailsResponse);
		return personDetailsResponse;

	}

	private PersonDetailsResponse buildPersonDetailsResponse(GetDemographicsResponse respObj) {
		PersonDetailsResponse personDetailsResponse = new PersonDetailsResponse();
		GetPersonDetailsResponse personDetails = new GetPersonDetailsResponse();
		
		String messageDetails = respObj.getMessage().getDetails();
		String messageText[] = messageDetails.split("\\|");
		String message = "";
		if (messageText.length > 1) {
			message = messageText[1];
		}
		if (respObj.getResultCount() == 0) {
			logger.debug("No result found for the Phn [{}]", respObj.getPerson().getPhn());

			if (messageText.length > 0) {
				personDetailsResponse.setStatus(StatusEnum.ERROR);
				personDetailsResponse.setMessage(message);
			}
		} else {
			personDetails.setPhn(respObj.getPerson().getPhn());
			Name nameObj = respObj.getPerson().getDeclaredName();
			String birthDate = new SimpleDateFormat("yyyyMMdd").format(respObj.getPerson().getBirthDate());
			if (nameObj == null)
				nameObj = respObj.getPerson().getDocumentedName();
			
			personDetails.setGivenName(nameObj.getFirstGivenName());
			personDetails.setSecondName(nameObj.getSecondGivenName());
			personDetails.setSurname(nameObj.getSurname());
		
			personDetails.setDateOfBirth(birthDate);
			personDetails.setGender(respObj.getPerson().getGender());

			if (messageText.length > 0) {
				if (message.contains("Warning")) {
					personDetailsResponse.setStatus(StatusEnum.WARNING);
					personDetailsResponse.setMessage(messageText[1]);
				} else {
					personDetailsResponse.setStatus(StatusEnum.SUCCESS);
				}
			}

			logger.info("Response message for given phn is: {}", personDetails.toString());
		}
		
		personDetailsResponse.setPerson(personDetails);

		return personDetailsResponse;

	}

	private GetDemographicsRequest buildDemographicsRequest(String phn) {
		GetDemographicsRequest getDemographics = new GetDemographicsRequest();
		getDemographics.setPhn(phn);
		getDemographics.setMrnSource(mrn_source);
		logger.debug("Creating request for the phn : {}", getDemographics.getPhn());

		return getDemographics;

	}

}
