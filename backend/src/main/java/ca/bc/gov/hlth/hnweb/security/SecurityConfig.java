package ca.bc.gov.hlth.hnweb.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${config.allowed-origins}")
    private String allowedOrigins;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    
    @Autowired
    private KeycloakClientRoleConverter keycloakClientRoleConverter;

    @Autowired
    private AudienceValidator audienceValidator;
    
    @Autowired
    private OrganizationValidator organizationValidator;
    
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(keycloakClientRoleConverter);
        http
        	.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
        	.and()
        	.cors(Customizer.withDefaults())
            .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/docs/**").permitAll()
            .mvcMatchers(HttpMethod.GET, "/bulletins").fullyAuthenticated()           
            .mvcMatchers(HttpMethod.POST, "/eligibility/check-msp-coverage-status").hasRole("MSPCoverageCheck")
            .mvcMatchers(HttpMethod.POST, "/eligibility/check-eligibility").hasRole("CheckEligibility")
            .mvcMatchers(HttpMethod.POST, "/eligibility/inquire-phn").hasRole("PHNInquiry")
            .mvcMatchers(HttpMethod.POST, "/eligibility/lookup-phn").hasRole("PHNLookup")
            .mvcMatchers(HttpMethod.POST, "/enrollment/enroll-subscriber").hasAnyRole("AddPermitHolderWithPHN", "AddPermitHolderWOPHN")
            .mvcMatchers(HttpMethod.POST, "/enrollment/get-person-details").hasRole("AddPermitHolderWithPHN")
            .mvcMatchers(HttpMethod.POST, "/enrollment/name-search").hasRole("AddPermitHolderWOPHN")
            .mvcMatchers(HttpMethod.POST, "/group-member/add-dependent").hasRole("AddDependent")
            .mvcMatchers(HttpMethod.POST, "/group-member/add-group-member").hasRole("AddGroupMember")
            .mvcMatchers(HttpMethod.POST, "/group-member/cancel-dependent").hasRole("CancelDependent")
            .mvcMatchers(HttpMethod.POST, "/group-member/cancel-group-member").hasRole("CancelGroupMember")
            .mvcMatchers(HttpMethod.POST, "/group-member/update-number-and-dept").hasRole("UpdateNumberAndDept")
            .mvcMatchers(HttpMethod.POST, "/group-member/reinstate-over-age-dependent").hasRole("ReinstateOverAgeDependent")
            .mvcMatchers(HttpMethod.POST, "/msp-contracts/get-contract-periods").hasRole("GetContractPeriods")
            .mvcMatchers(HttpMethod.POST, "/msp-contracts/update-contract-address").hasRole("UpdateContractAddress")
            .mvcMatchers(HttpMethod.POST, "/msp-contracts/inquire-contract").hasAnyRole("ContractInquiry", "GetGroupMembersContractAddress")  //inquire-contract endpoint will require this multi role as it is used by both R40 and R37 transactions
            .mvcMatchers(HttpMethod.GET, "/user/**").fullyAuthenticated()
            .mvcMatchers("/*").denyAll()
            .and()
            .oauth2ResourceServer().jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        OAuth2TokenValidator<Jwt> withOrganization = new DelegatingOAuth2TokenValidator<>(withAudience, organizationValidator);

        jwtDecoder.setJwtValidator(withOrganization);

        return jwtDecoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
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
