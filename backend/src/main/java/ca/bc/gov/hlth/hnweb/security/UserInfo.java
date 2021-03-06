package ca.bc.gov.hlth.hnweb.security;

/**
 * Information on the current user obtained from the JWT.
 */
public class UserInfo {
	private String username;
	private String organization;
	private String role;
	private String sessionState;

	public UserInfo() {
		super();
	}

	public UserInfo(String username, String organization, String role) {
		super();
		this.username = username;
		this.organization = organization;
		this.role = role;
	}
	
	public UserInfo(String username, String organization, String role, String sessionState) {
		super();
		this.username = username;
		this.organization = organization;
		this.role = role;
		this.sessionState = sessionState;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

}