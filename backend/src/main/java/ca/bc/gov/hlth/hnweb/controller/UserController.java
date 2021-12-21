package ca.bc.gov.hlth.hnweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.security.SecurityProperties;
import ca.bc.gov.hlth.hnweb.security.SecurityUtil;

/**
 * Handles requests related to user security.
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private SecurityProperties securityProperties;
	
    /**
     * Retrieves all the permission for the current user.
     * 
     * @return The list of permissions.
     */
	@GetMapping("/permissions")
	public ResponseEntity<List<String>> getPermissions() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt)auth.getPrincipal();
		List<String> permissions = SecurityUtil.loadPermissions(jwt, securityProperties.getRolePermissions());
		ResponseEntity<List<String>> responseEntity = ResponseEntity.ok(permissions);
		return responseEntity; 
	}

}
