package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPWB0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private WB0 wb0 = new WB0();

	public RPBSPWB0() {
		super();
	}

	public RPBSPWB0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		wb0 = new WB0(bodyText);
	}

	public WB0 getWb0() {
		return wb0;
	}

	public void setWb0(WB0 wb0) {
		this.wb0 = wb0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + wb0.serialize();
	}

}
