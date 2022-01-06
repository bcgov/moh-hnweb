package ca.bc.gov.hlth.hnweb.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${config.allowed-origins}")
    private String allowedOrigins;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakClientRoleConverter());
        System.out.println("allowed origin Value: "+allowedOrigins);
        http
        	.cors(Customizer.withDefaults())
            .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/docs/**").permitAll()
            // TODO (weskubo-cgi) Replace these with the correct role loaded from application.yaml
            .mvcMatchers(HttpMethod.GET,"/eligibility/**").fullyAuthenticated()
            .mvcMatchers(HttpMethod.GET,"/r41/**").hasRole("manage-clients")
            //.mvcMatchers(HttpMethod.GET, "/r41/**").permitAll()
            .mvcMatchers("/*").denyAll()
            .and()
            .oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(allowedOrigins);
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(Collections.singletonList("Location"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
