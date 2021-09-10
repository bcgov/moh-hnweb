package ca.bc.gov.hlth.hnweb.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	private static final String ROLE_PREFIX = "ROLE_";
	// TODO (weskubo-cgi) Replace with the correct resource_access
    private static final String API_CLIENT_NAME = "realm-management";

    @SuppressWarnings("unchecked")
	public Collection<GrantedAuthority> convert(final Jwt jwt) {

        Collection<GrantedAuthority> authorities = null;

        /* The roles in the access token look like
        "resource_access": {
          "user-management-service": {
            "roles": [
               "view-groups"
            ]
          }
        }*/
        final Map<String, Object> resourceAccesses = (Map<String, Object>) jwt.getClaims().get("resource_access");
        if (resourceAccesses != null) {
            final Map<String, Object> resource = (Map<String, Object>) resourceAccesses.get(API_CLIENT_NAME);
            if (resource != null) {
                authorities = ((List<String>) resource.get("roles")).stream()
                        .map(roleName -> ROLE_PREFIX + roleName) // prefix required to map to a Spring Security "role"
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }
        return authorities;
    }
}
