package ca.bc.gov.hlth.hnweb.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {
	private static final String CLAIM_RESOURCE_ACCESS = "resource_access";

	private static final String CLAIM_USERNAME = "preferred_username";
	// TODO (weskubo-cgi) Verify the claim names
	@SuppressWarnings("unused")
	private static final String CLAIM_ORGANIZATION = "organization";
	
	private static final String KEYCLOAK_CLIENT = "HN-WEB";
	
	private static final String USER_ROLES = "roles";

	public static UserInfo loadUserInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt)auth.getPrincipal();
		UserInfo userInfo = new UserInfo();
		// TODO (weskubo-cgi) Replace these once the JWT has this info available
//		userInfo.setOrganization((jwt.getClaim(CLAIM_ORGANIZATION));
		userInfo.setOrganization("00000010");
		
		List<String> roles = loadRoles(jwt);
		userInfo.setRole(StringUtils.join(roles, " "));

		userInfo.setUsername(jwt.getClaim(CLAIM_USERNAME));
		
		return userInfo;
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> loadRoles(Jwt jwt) {
		List<String> permissions = new ArrayList<>();
        Map<String, Object> resourceAccesses = (Map<String, Object>) jwt.getClaims().get(CLAIM_RESOURCE_ACCESS);
        
        if (resourceAccesses == null) {
        	return permissions;
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccesses.get(KEYCLOAK_CLIENT);
        if (resource == null) {
        	return permissions;
        }

        return (List<String>)resource.get(USER_ROLES);        
	}
	
	public static List<String> loadPermissions(Jwt jwt, Map<String, List<String>> rolePermissions) {
        List<String> roles = loadRoles(jwt);
        List<String> permissions = new ArrayList<>();
        roles.forEach(role -> {        		
        	permissions.addAll(rolePermissions.get(role));
        });
        
        return permissions;
	}

}
