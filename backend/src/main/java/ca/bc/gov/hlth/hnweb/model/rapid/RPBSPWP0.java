package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPWP0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private WP0 wp0 = new WP0();

	public RPBSPWP0() {
		super();
	}

	public RPBSPWP0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		wp0 = new WP0(bodyText);
	}

	public WP0 getWp0() {
		return wp0;
	}

	public void setWp0(WP0 wp0) {
		this.wp0 = wp0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + wp0.serialize();
	}

}
