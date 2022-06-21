package ca.bc.gov.hlth.hnweb.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for working with authentication, tokens.
 */
@Component
public class SecurityUtil {
	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	private static final String CLAIM_RESOURCE_ACCESS = "resource_access";
	private static final String CLAIM_SESSION_STATE = "session_state";
	public static final String CLAIM_USERNAME = "preferred_username";
	public static final String CLAIM_ORGANIZATION = "org_details";
	
	private static final String ORGANIZATION_ID = "id";
	
	private static final String USER_ROLES = "roles";

    private static String KEYCLOAK_CLIENT;
    
    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private void setKeycloakClientStatic(String keycloakClient){
        SecurityUtil.KEYCLOAK_CLIENT = keycloakClient;
    }

    public static UserInfo loadUserInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt)auth.getPrincipal();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setOrganization(extractOrganization(jwt));
		
		List<String> roles = loadRoles(jwt);
		userInfo.setRole(StringUtils.join(roles, " "));

		userInfo.setSessionState(jwt.getClaim(CLAIM_SESSION_STATE));
		userInfo.setUsername(jwt.getClaim(CLAIM_USERNAME));
		
		return userInfo;
	}
	
	private static String extractOrganization(Jwt jwt) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree((String)jwt.getClaim(CLAIM_ORGANIZATION));
			return node.get(ORGANIZATION_ID).asText();
		} catch (Exception e) {
			logger.warn("User {} does not have claim {} set", jwt.getClaim(CLAIM_USERNAME), CLAIM_ORGANIZATION);
			return null;
		}
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
        	List<String> currentPermissions = rolePermissions.get(role.toLowerCase());
        	if (currentPermissions != null) {
        		permissions.addAll(currentPermissions);	
        	} else {
        		logger.warn("Role {} has no permissions defined.", role);
        	}
        	
        });
        
        return permissions;
	}

}