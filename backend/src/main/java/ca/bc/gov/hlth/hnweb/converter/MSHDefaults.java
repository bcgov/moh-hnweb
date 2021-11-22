package ca.bc.gov.hlth.hnweb.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MSHDefaults {

	@Value("${v2.msh.processingID}")
	private String processingID;

	@Value("${v2.msh.receivingFacility}")
	private String receivingFacility;
	
	@Value("${v2.msh.sendingApplication}")
	private String sendingApplication;
	
	@Value("${v2.msh.sendingFacility}")
	private String sendingFacility;

	public String getProcessingID() {
		return processingID;
	}

	public void setProcessingID(String processingID) {
		this.processingID = processingID;
	}

	public String getReceivingFacility() {
		return receivingFacility;
	}

	public void setReceivingFacility(String receivingFacility) {
		this.receivingFacility = receivingFacility;
	}

	public String getSendingApplication() {
		return sendingApplication;
	}

	public void setSendingApplication(String sendingApplication) {
		this.sendingApplication = sendingApplication;
	}

	public String getSendingFacility() {
		return sendingFacility;
	}

	public void setSendingFacility(String sendingFacility) {
		this.sendingFacility = sendingFacility;
	}
	
	
}
