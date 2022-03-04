package ca.bc.gov.hlth.hnweb.security;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.service.AuditService;

/**
 * The generic Spring JwtClaimValidator throws an NPE when a claim is missing, so we customize it to avoid that exception
 */
@Component
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private static final Logger logger = LoggerFactory.getLogger(AudienceValidator.class);

    private static final OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "The " + AUD + " claim is not valid",
            "https://tools.ietf.org/html/rfc6750#section-3.1");

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String audience;
    
    @Autowired
    private AuditService auditService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Assert.notNull(jwt, "Token cannot be null");

        if (jwt.getAudience() != null && jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        } else {
    		// Audit the failure. No audience means the user does not have any MSP Direct roles and should be considered Unauthorized
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        	Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), TransactionType.UNKNOWN);    	
        	auditService.createTransactionEvent(transaction, TransactionEventType.UNAUTHORIZED);
        	
        	logger.warn("User {} is missing aud claim {} ", jwt.getClaim(SecurityUtil.CLAIM_USERNAME).toString(), audience);
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}