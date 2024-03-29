package ca.bc.gov.hlth.hnweb.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Information on the current user obtained from the JWT.
 */
public class UserInfo {
	private String username;
	private String userId;
	private String organization;
	private String organizationName;
	private String role;
	private String sessionState;
	private List<String> roles;

	public UserInfo() {
		super();
	}

	public UserInfo(String username, String organization, String organizationName, String role) {
		super();
		this.username = username;
		this.organization = organization;
		this.organizationName = organizationName;
		this.role = role;
	}

	public UserInfo(String username, String userId, String organization, String organizationName, String role, String sessionState) {
		super();
		this.username = username;
		this.userId = userId;
		this.organization = organization;
		this.organizationName = organizationName;
		this.role = role;
		this.sessionState = sessionState;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRole() {
		return StringUtils.join(this.roles, " ");
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", userId=" + userId + ", organization=" + organization + ", organizationName="
				+ organizationName + ", role=" + role + ", sessionState=" + sessionState + "]";
	}

}