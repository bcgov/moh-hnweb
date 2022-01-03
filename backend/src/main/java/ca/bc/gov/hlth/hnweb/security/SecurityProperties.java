package ca.bc.gov.hlth.hnweb.security;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Contains a map of roles and permissions.
 */
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

	private Map<String, List<String>> rolePermissions;

	public Map<String, List<String>> getRolePermissions() {
		return rolePermissions;
	}

	public void setRolePermissions(Map<String, List<String>> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}

}
