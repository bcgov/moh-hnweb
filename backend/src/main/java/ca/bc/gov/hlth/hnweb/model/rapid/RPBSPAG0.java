package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPAG0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private AG0 ag0 = new AG0();

	public RPBSPAG0() {
		super();
	}

	public RPBSPAG0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ag0 = new AG0(bodyText);
	}

	public AG0 getAg0() {
		return ag0;
	}

	public void setAg0(AG0 ag0) {
		this.ag0 = ag0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ag0.serializ();
	}
	
}
