package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPAJ0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private AJ0 aj0 = new AJ0();

	public RPBSPAJ0() {
		super();
	}

	public RPBSPAJ0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		aj0 = new AJ0(bodyText);
	}

	public AJ0 getAj0() {
		return aj0;
	}

	public void setAj0(AJ0 aj0) {
		this.aj0 = aj0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + aj0.serialize();
	}

}
