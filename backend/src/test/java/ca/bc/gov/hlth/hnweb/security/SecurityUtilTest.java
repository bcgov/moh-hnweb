package ca.bc.gov.hlth.hnweb.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;

@SpringBootTest
public class SecurityUtilTest {
	
    @Autowired
    private SecurityProperties securityProperties;
	
	@Test
	public void testLoadPermissions_E45() {
		Jwt jwt = createToken("E45");
		
		List<String> permissions = SecurityUtil.loadPermissions(jwt, securityProperties.getRolePermissions());
		assertEquals(1, permissions.size());
		assertEquals("E45", permissions.get(0));
	}
	
	@Test
	public void testLoadPermissions_Eligibility() {
		Jwt jwt = createToken("Eligibility");
		
		List<String> permissions = SecurityUtil.loadPermissions(jwt, securityProperties.getRolePermissions());
		assertEquals(3, permissions.size());
		assertEquals("E45", permissions.get(0));
		assertEquals("R15", permissions.get(1));
		assertEquals("R41", permissions.get(2));
	}
	
	private Jwt createToken(String... roles) {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "RS256");
		headers.put("typ", "JWT");		
		
		Map<String, Object> claims = new HashMap<>();
		
		Map<String, Object> resourceAccesses = new HashMap<>();
		claims.put("resource_access", resourceAccesses);

        Map<String, Object> resource = new HashMap<>();
        resourceAccesses.put("MSPDIRECT-SERVICE", resource);
        
        List<String> userRoles = new ArrayList<>();
        userRoles.addAll(Arrays.asList(roles));
        resource.put("roles", userRoles);
		
		return new Jwt("jwt-token-value", Instant.now(), Instant.now().plusSeconds(60), headers, claims);
	}

}
