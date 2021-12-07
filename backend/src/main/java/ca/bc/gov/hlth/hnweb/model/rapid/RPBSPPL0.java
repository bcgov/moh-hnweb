package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPPL0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private PL0 pl0 = new PL0();

	public RPBSPPL0() {
		super();
	}

	public RPBSPPL0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		pl0 = new PL0(bodyText);
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + pl0.serialize();
	}

	public PL0 getPl0() {
		return pl0;
	}

	public void setPl0(PL0 pl0) {
		this.pl0 = pl0;
	}

}
