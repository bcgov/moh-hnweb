package ca.bc.gov.hlth.hnweb.model.rapid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FixedWidthDefaults {
	
	@Value("${fixedWidth.header.organization}")
	private String organization;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
