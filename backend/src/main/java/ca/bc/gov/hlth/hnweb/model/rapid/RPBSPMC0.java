package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPMC0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private RPBSMsgData rpbsMsgData = new RPBSMsgData();

	public RPBSPMC0() {
		super();
	}

	public RPBSPMC0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		rpbsMsgData = new RPBSMsgData(bodyText);
	}

	public RPBSMsgData getRpbsMsgData() {
		return rpbsMsgData;
	}

	public void setRpbsMsgData(RPBSMsgData rpbsMsgData) {
		this.rpbsMsgData = rpbsMsgData;
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + rpbsMsgData.serialize();
	}

}
