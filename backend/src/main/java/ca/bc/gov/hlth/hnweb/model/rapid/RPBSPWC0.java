package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPWC0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private WC0 wc0 = new WC0();

	public RPBSPWC0() {
		super();
	}

	public RPBSPWC0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		wc0 = new WC0(bodyText);
	}

	public WC0 getWc0() {
		return wc0;
	}

	public void setWc0(WC0 wc0) {
		this.wc0 = wc0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + wc0.serialize();
	}

}
