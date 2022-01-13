package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPEE0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private EE0 ee0 = new EE0();

	public RPBSPEE0() {
		super();
	}

	public RPBSPEE0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ee0 = new EE0(bodyText);
	}

	public EE0 getEe0() {
		return ee0;
	}

	public void setEe0(EE0 ee0) {
		this.ee0 = ee0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ee0.serialize();
	}

}
