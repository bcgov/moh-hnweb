package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPRE0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private RE0 re0 = new RE0();

	public RPBSPRE0() {
		super();
	}

	public RPBSPRE0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		re0 = new RE0(bodyText);
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + re0.serialize();
	}

	public RE0 getRe0() {
		return re0;
	}

	public void setRe0(RE0 re0) {
		this.re0 = re0;
	}

}
