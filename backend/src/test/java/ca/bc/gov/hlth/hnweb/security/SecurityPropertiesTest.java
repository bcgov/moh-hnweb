package ca.bc.gov.hlth.hnweb.security;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SecurityPropertiesTest {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Test
	public void testGetProperties_success() {
		Map<String, List<String>> rolePermissions = securityProperties.getRolePermissions();
		assertEquals(4, rolePermissions.size());
		
		List<String> e45Permissions = rolePermissions.get("E45");
		assertEquals(1, e45Permissions.size());
		assertEquals("MSPCoverageCheck", e45Permissions.get(0));
	}

}
