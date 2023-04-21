package ca.bc.gov.hlth.hnweb.pbf;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Contains maps for PBF properties including: Cancel Reason, Deregistration
 * Reason, Registration Reason
 */
@Component
@ConfigurationProperties(prefix = "pbf")
public class PBFProperties {

	private Map<String, String> cancelReasons;

	private Map<String, String> deregistrationReasons;

	private Map<String, String> registrationReasons;

	public Map<String, String> getCancelReasons() {
		return cancelReasons;
	}

	public void setCancelReasons(Map<String, String> cancelReasons) {
		this.cancelReasons = cancelReasons;
	}

	public Map<String, String> getDeregistrationReasons() {
		return deregistrationReasons;
	}

	public void setDeregistrationReasons(Map<String, String> deregistrationReasons) {
		this.deregistrationReasons = deregistrationReasons;
	}

	public Map<String, String> getRegistrationReasons() {
		return registrationReasons;
	}

	public void setRegistrationReasons(Map<String, String> registrationReasons) {
		this.registrationReasons = registrationReasons;
	}

}
