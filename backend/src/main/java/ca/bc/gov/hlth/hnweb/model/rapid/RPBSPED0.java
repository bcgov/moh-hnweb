package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPED0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private ED0 ed0 = new ED0();

	public RPBSPED0() {
		super();
	}

	public RPBSPED0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ed0 = new ED0(bodyText);
	}

	public ED0 getEd0() {
		return ed0;
	}

	public void setEd0(ED0 ed0) {
		this.ed0 = ed0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ed0.serialize();
	}

}
