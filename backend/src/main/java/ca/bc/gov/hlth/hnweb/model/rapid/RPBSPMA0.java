package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPMA0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private MA0 ma0 = new MA0();

	public RPBSPMA0() {
		super();
	}

	public RPBSPMA0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ma0 = new MA0(bodyText);
	}	

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public MA0 getMa0() {
		return ma0;
	}

	public void setMa0(MA0 ma0) {
		this.ma0 = ma0;
	}
	
	public String serialize() {
		return rpbsHeader.serialize() + ma0.serialize();
	}

}
