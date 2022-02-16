package ca.bc.gov.hlth.hnweb.model.rapid;

import org.apache.commons.lang3.StringUtils;

public class RPBSMsgData {

	/** 1	Data	String	Yes	9866...9866	1..1 */
	private String data;
	
	public RPBSMsgData() {
		super();
	}
	
	public RPBSMsgData(String message) {
		super();
		data = StringUtils.substring(message, 0, 10);
	}

	public String serialize() {
		// Serialize is only used when creating the request
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.rightPad(data, 10));

		return sb.toString();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
