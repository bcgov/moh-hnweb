package ca.bc.gov.hlth.hnweb.converter;

import org.slf4j.Logger;

import ca.bc.gov.hlth.hnweb.config.HL7Config;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.serialization.HL7Serializer;

public class XmlConverter {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(XmlConverter.class);
	private static final String dataEntererExt = "";
	private static final String sourceSystemOverride = "HOOPC";
	private static final String organization = "BCHCIM";
	private static final String mrn_source = "MOH_CRS";
	protected HL7Serializer hl7;
	protected MessageMetaData mmd;

	public XmlConverter() {
		hl7 = new HL7Serializer(new HL7Config());
		mmd = new MessageMetaData(dataEntererExt, sourceSystemOverride, organization);
	}

	/**
	 * @param phn
	 * @return
	 */
	public String convertRequest(String phn) {
		logger.debug("Get Demographics details for PHN [{}]", phn);

		GetDemographicsRequest request = buildDemographicsRequest(phn);
		Object formattedRequest = hl7.toXml(request, mmd);
		String historyRequest = formattedRequest.toString();
		logger.debug("Request XML : {} ", historyRequest);
		return historyRequest;

	}

	public GetPersonDetailsResponse convertResponse(String xmlString) {

		GetDemographicsResponse demographicsResponse = hl7.fromXml(xmlString, GetDemographicsResponse.class);
		logger.debug("Converted Demographics response : {} ", demographicsResponse.toString());
		GetPersonDetailsResponse personDetailsResponse = buildPersonDetailsResponse(demographicsResponse);
		logger.debug("Converted PersonDetails Response : {} ", personDetailsResponse);
		return personDetailsResponse;

	}

	private GetPersonDetailsResponse buildPersonDetailsResponse(GetDemographicsResponse respObj) {
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
				personDetails.setStatus(StatusEnum.ERROR);
				personDetails.setMessage(message);
			}
		} else {
			personDetails.setPhn(respObj.getPerson().getPhn());
			personDetails.setGivenName(respObj.getPerson().getDocumentedName().getFirstGivenName());
			personDetails.setSecondName(respObj.getPerson().getDocumentedName().getSecondGivenName());
			personDetails.setSurname(respObj.getPerson().getDocumentedName().getSurname());
			personDetails.setDateOfBirth(respObj.getPerson().getBirthDate());

			if (messageText.length > 0) {
				if (message.contains("Warning")) {
					personDetails.setStatus(StatusEnum.WARNING);
					personDetails.setMessage(messageText[1]);
				} else {
					personDetails.setStatus(StatusEnum.SUCCESS);
				}
			}

			logger.info(personDetails.toString());
		}

		return personDetails;

	}

	private GetDemographicsRequest buildDemographicsRequest(String phn) {

		GetDemographicsRequest getDemographics = new GetDemographicsRequest();
		getDemographics.setPhn(phn);
		getDemographics.setMrnSource(mrn_source);
		logger.debug("Creating request for the phn : {}", getDemographics.getPhn());

		return getDemographics;

	}

}
