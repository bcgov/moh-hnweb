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
	

    private GetPersonDetailsResponse buildPersonDetailsResponse(GetDemographicsResponse respObj)  {
    	GetPersonDetailsResponse personDetails = new GetPersonDetailsResponse();
    	
    	if (respObj.getResultCount() == 0) {
	    	 logger.debug("Error performing get demographics");
	    	 personDetails.setStatus(StatusEnum.ERROR);
	    	 personDetails.setMessage(respObj.getMessage().getDetails());
	    	 return personDetails;
	     }

    	personDetails.setPhn(respObj.getPerson().getPhn());
    
    	personDetails.setGivenName(respObj.getPerson().getDocumentedName().getFirstGivenName());
    	personDetails.setSecondName(respObj.getPerson().getDocumentedName().getSecondGivenName());
    	personDetails.setSecondName(respObj.getPerson().getDocumentedName().getSurname());
    	personDetails.setDateOfBirth(respObj.getPerson().getBirthDate());
    	
    	//No message for successful transaction is returned
    	personDetails.setMessage("Transaction Successful");
    	personDetails.setStatus(StatusEnum.SUCCESS);
    	
    	logger.info(personDetails.toString());
    	
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
