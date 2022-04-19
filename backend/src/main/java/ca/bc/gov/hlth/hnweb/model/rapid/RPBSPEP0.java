package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPEP0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private EP0 ep0 = new EP0();

	public RPBSPEP0() {
		super();
	}

	public RPBSPEP0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ep0 = new EP0(bodyText);
	}	

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}
	
	public EP0 getEp0() {
		return ep0;
	}

	public void setEp0(EP0 ep0) {
		this.ep0 = ep0;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ep0.serialize();
	}

}
