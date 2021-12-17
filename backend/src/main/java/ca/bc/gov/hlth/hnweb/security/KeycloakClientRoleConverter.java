package ca.bc.gov.hlth.hnweb.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class KeycloakClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private SecurityProperties securityProperties;

	public Collection<GrantedAuthority> convert(final Jwt jwt) {
    	List<String> permissions = SecurityUtil.loadPermissions(jwt, securityProperties.getRolePermissions());
    	
    	List<GrantedAuthority> authorities = permissions.stream()
    			.map(roleName -> ROLE_PREFIX + roleName) // prefix required to map to a Spring Security "role"
    			.map(SimpleGrantedAuthority::new)
    			.collect(Collectors.toList());
         
        return authorities;
    }
}
