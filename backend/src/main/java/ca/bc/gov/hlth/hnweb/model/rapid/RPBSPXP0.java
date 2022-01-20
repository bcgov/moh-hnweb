package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPXP0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private XP0 xp0 = new XP0();

	public RPBSPXP0() {
		super();
	}

	public RPBSPXP0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		xp0 = new XP0(bodyText);
	}


	public XP0 getXp0() {
		return xp0;
	}

	public void setXp0(XP0 xp0) {
		this.xp0 = xp0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + xp0.serialize();
	}

}
