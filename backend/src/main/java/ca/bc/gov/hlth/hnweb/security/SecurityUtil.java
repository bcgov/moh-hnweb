package ca.bc.gov.hlth.hnweb.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private static final String CLAIM_SUB = "sub"; // the Subject claim identifies the principal that is the subject of the JWT
	public static final String CLAIM_ORGANIZATION = "org_details";

	private static final String ORGANIZATION_ID = "id";

	private static final String ORGANIZATION_NAME = "name";

	private static final String USER_ROLES = "roles";
	
	private static final String UNKNOWN_ROLE = "UNKNOWN";

	private static String KEYCLOAK_CLIENT;

	private static SecurityProperties securityProperties;

	@Autowired
	public void setSecurityProperties(SecurityProperties properties) {
		SecurityUtil.securityProperties = properties;
	}

	@Value("${spring.security.oauth2.resourceserver.jwt.audience}")
	private void setKeycloakClientStatic(String keycloakClient) {
		SecurityUtil.KEYCLOAK_CLIENT = keycloakClient;
	}

	public static UserInfo loadUserInfo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) auth.getPrincipal();

		UserInfo userInfo = new UserInfo();
		extractOrganization(jwt, userInfo);

		List<String> roles = loadRoles(jwt);
		userInfo.setRoles(roles);

		userInfo.setSessionState(jwt.getClaim(CLAIM_SESSION_STATE));
		userInfo.setUsername(jwt.getClaim(CLAIM_USERNAME));
		userInfo.setUserId(jwt.getClaim(CLAIM_SUB));

		return userInfo;
	}

	public static String loadSPGBasedOnTransactionType(UserInfo userInfo, TransactionType transactionType) {
		Map<String, List<String>> rolePermissions = securityProperties.getRolePermissions();
		List<String> roles = userInfo.getRoles();
		if (roles.size() == 1) {
			return roles.get(0);
		}
		for (String role : roles) {
			List<String> permissions = rolePermissions.get(role.toLowerCase());
			if (permissions == null) {
				continue;
			}
			switch (transactionType) {
			case ENROLL_SUBSCRIBER:
				if (permissions.contains("AddPermitHolderWOPHN") || permissions.contains("AddPermitHolderWithPHN")) {
					return role;
				}
				break;
			case GET_PERSON_DETAILS:
				if (permissions.contains("AddPermitHolderWithPHN")) {
					return role;
				}
				break;
			case NAME_SEARCH:
				if (permissions.contains("AddPermitHolderWOPHN")) {
					return role;
				}
				break;
			case CONTRACT_INQUIRY:
				if (permissions.contains("ContractInquiry") || permissions.contains("GetContractAddress")) {
					return role;
				}
				break;
			case GET_PATIENT_REGISTRATION:
				if (permissions.contains("PatientRegistration")) {
					return role;
				}
				break;
			default:
				if (permissions.contains(transactionType.getValue())) {
					return role;
				}
			}
		}
		return UNKNOWN_ROLE;
	}
	
	
	private static void extractOrganization(Jwt jwt, UserInfo userInfo) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree((String)jwt.getClaim(CLAIM_ORGANIZATION));
			userInfo.setOrganization(node.get(ORGANIZATION_ID).asText());
			userInfo.setOrganizationName(node.get(ORGANIZATION_NAME).asText());
		} catch (Exception e) {
			logger.warn("User {} does not have claim {} set", jwt.getClaim(CLAIM_USERNAME), CLAIM_ORGANIZATION);
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

		return (List<String>) resource.get(USER_ROLES);
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