package ca.bc.gov.hlth.hnweb.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
	private static final String CLAIM_USERNAME = "preferred_username";
	// TODO (weskubo-cgi) Verify the claim names
	@SuppressWarnings("unused")
	private static final String CLAIM_ORGANIZATION = "organization";
	@SuppressWarnings("unused")
	private static final String CLAIM_ROLE = "role";
	
	public static UserInfo loadUserInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt)auth.getPrincipal();
		UserInfo userInfo = new UserInfo();
		// TODO (weskubo-cgi) Replace these once the JWT has this info available
//		userInfo.setOrganization((jwt.getClaim(CLAIM_ORGANIZATION));
		userInfo.setOrganization("00000010");
//		userInfo.setRole(jwt.getClaim(CLAIM_ROLE));
		userInfo.setRole("HNAIADMINISTRATION");
		userInfo.setUsername(jwt.getClaim(CLAIM_USERNAME));
		
		return userInfo;
	}

}
