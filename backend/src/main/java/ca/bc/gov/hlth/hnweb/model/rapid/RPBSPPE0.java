package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSPPE0 {
	private RPBSHeader rpbsHeader = new RPBSHeader();
	private CI0 ci0 = new CI0();

	public RPBSPPE0() {
		super();
	}

	public RPBSPPE0(String message) {
		String headerText = StringUtils.substring(message, 0, 144);
		String bodyText = StringUtils.substring(message, 144);

		rpbsHeader = new RPBSHeader(headerText);

		ci0 = new CI0(bodyText);
	}

	public RPBSHeader getRpbsHeader() {
		return rpbsHeader;
	}

	public void setRpbsHeader(RPBSHeader rpbsHeader) {
		this.rpbsHeader = rpbsHeader;
	}

	public String serialize() {
		return rpbsHeader.serialize() + ci0.serialize();
	}

	public CI0 getCi0() {
		return ci0;
	}

	public void setCi0(CI0 ci0) {
		this.ci0 = ci0;
	}

}
