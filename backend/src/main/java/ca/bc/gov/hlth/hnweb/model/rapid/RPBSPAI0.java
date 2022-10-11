package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPAI0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private AI0 ai0 = new AI0();

	public RPBSPAI0() {
		super();
	}

	public RPBSPAI0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		ai0 = new AI0(bodyText);
	}

	public AI0 getAi0() {
		return ai0;
	}

	public void setAi0(AI0 ai0) {
		this.ai0 = ai0;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ai0.serialize();
	}

}
