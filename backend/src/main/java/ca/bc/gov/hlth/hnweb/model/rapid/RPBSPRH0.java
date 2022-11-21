package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPRH0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private RH0 rh0 = new RH0();
	
	public RPBSPRH0() {
		super();
	}

	public RPBSPRH0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);
		rpbsHeader = new RPBSHeader(headerText);
		rh0 = new RH0(bodyText);
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public RH0 getRh0() {
		return rh0;
	}

	public void setRh0(RH0 rh0) {
		this.rh0 = rh0;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + rh0.serialize();
	}

}
