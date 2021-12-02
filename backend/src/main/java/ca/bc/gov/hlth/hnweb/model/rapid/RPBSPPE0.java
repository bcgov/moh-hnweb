package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPPE0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private RPBSPPE0Data rpbsData = new RPBSPPE0Data();

	public RPBSPPE0() {
		super();
	}

	public RPBSPPE0(String message) {
		String headerText = StringUtils.substring(message, 0, RPBSHeader.SEGMENT_LENGTH);
		String bodyText = StringUtils.substring(message, RPBSHeader.SEGMENT_LENGTH);

		rpbsHeader = new RPBSHeader(headerText);

		rpbsData = new RPBSPPE0Data(bodyText);
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + rpbsData.serialize();
	}

	public RPBSPPE0Data getRpbsData() {
		return rpbsData;
	}

	public void setRpbsData(RPBSPPE0Data rpbsData) {
		this.rpbsData = rpbsData;
	}

}
