package ca.bc.gov.hlth.hnweb.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class OrganizationValidator implements OAuth2TokenValidator<Jwt> {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationValidator.class);
    
    private static final String CLAIM_ORGANIZATION = "org_details";

    private static final OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, " User has no Organization",
            "https://tools.ietf.org/html/rfc6750#section-3.1");

    
    @Autowired
    private AuditService auditService;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Assert.notNull(jwt, "Token cannot be null");
        
        String org = jwt.getClaim(CLAIM_ORGANIZATION);
        
        if (StringUtils.isNotEmpty(org)) {
            return OAuth2TokenValidatorResult.success();
        } else {
    		// Audit the failure. Only users with defined Organization should be considered as authorized and able to access MSP Direct.
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        	Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), TransactionType.UNKNOWN);    	
        	auditService.createTransactionEvent(transaction, TransactionEventType.UNAUTHORIZED);
        	
            logger.info(error.getDescription());
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}
