package ca.bc.gov.hlth.hnweb.converter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.hlth.hnweb.config.HL7Config;
import ca.bc.gov.hlth.hnweb.model.GetPersonDetailsResponse;
import ca.bc.gov.hlth.hnweb.model.StatusEnum;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsRequest;
import ca.bc.gov.hlth.hnweb.model.v3.GetDemographicsResponse;
import ca.bc.gov.hlth.hnweb.model.v3.MessageMetaData;
import ca.bc.gov.hlth.hnweb.model.v3.Name;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;
import ca.bc.gov.hlth.hnweb.security.UserInfo;
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
	private static final String sourceSystemOverride = "MOH_CRS";
	private static final String organization = "MOH_CRS";
	private static final String mrn_source = "MOH_CRS";
	protected HL7Serializer hl7;
	protected MessageMetaData mmd;
	protected UserInfo userInfo;

	public XmlConverter(String transectionId) {
		this.hl7 = new HL7Serializer(new HL7Config());
		this.userInfo = SecurityUtil.loadUserInfo();
		this.mmd = new MessageMetaData(userInfo.getUsername(), sourceSystemOverride, organization, transectionId);
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
	public GetPersonDetailsResponse convertResponse(String xmlString) throws IOException {
		GetDemographicsResponse results = hl7.fromXml(xmlString, GetDemographicsResponse.class);
		logger.debug("Converted Demographics response : {} ", results.toString());
		
		GetPersonDetailsResponse getPersonDetailsResponse = handleStatus(results);
		logger.debug("Converted PersonDetails Response : {} ", getPersonDetailsResponse);
		return getPersonDetailsResponse;

	}
	

	private GetPersonDetailsResponse handleStatus(GetDemographicsResponse respObj) {
		GetPersonDetailsResponse getPersonDetailsResponse = new GetPersonDetailsResponse();
		
		String messageDetails = respObj.getMessage().getDetails();
		String messageText[] = messageDetails.split("\\|");
		String message = "";
		if (messageText.length > 1) {
			message = messageText[1];
		}
		if (respObj.getResultCount() == 0) {
			logger.debug("No result found for the Phn [{}]", respObj.getPerson().getPhn());

			if (messageText.length > 0) {
				getPersonDetailsResponse.setStatus(StatusEnum.ERROR);
				getPersonDetailsResponse.setMessage(message);
			}
		} else {
			buildPersonDetails(respObj, getPersonDetailsResponse);
			if (messageText.length > 0) {
				if (message.contains("Warning")) {
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

	private void buildPersonDetails(GetDemographicsResponse respObj, GetPersonDetailsResponse getPersonDetailsResponse) {
		getPersonDetailsResponse.setPhn(respObj.getPerson().getPhn());
		Name nameObj = respObj.getPerson().getDeclaredName();
		String birthDate = new SimpleDateFormat("yyyyMMdd").format(respObj.getPerson().getBirthDate());
		if (nameObj == null)
			nameObj = respObj.getPerson().getDocumentedName();
		
		getPersonDetailsResponse.setGivenName(nameObj.getFirstGivenName());
		getPersonDetailsResponse.setSecondName(nameObj.getSecondGivenName());
		getPersonDetailsResponse.setSurname(nameObj.getSurname());

		getPersonDetailsResponse.setDateOfBirth(birthDate);
		getPersonDetailsResponse.setGender(respObj.getPerson().getGender());
	}

	private GetDemographicsRequest buildDemographicsRequest(String phn) {
		GetDemographicsRequest getDemographics = new GetDemographicsRequest();
		getDemographics.setPhn(phn);
		getDemographics.setMrnSource(mrn_source);
		logger.debug("Creating request for the phn : {}", getDemographics.getPhn());

		return getDemographics;

	}

}
