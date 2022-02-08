package ca.bc.gov.hlth.hnweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.service.AuditService;

/**
 * Custom implementation of AccessDeniedHandler which supports auditing.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	private static final String ACCESS_IS_DENIED = "Access is denied";

    @Autowired
    private AuditService auditService;

	public CustomAccessDeniedHandler() {
		super();
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		// Audit the exception
    	Transaction transaction = auditService.createTransaction(request.getRemoteAddr(), TransactionType.UNKNOWN);    	
    	auditService.createTransactionEvent(transaction, TransactionEventType.UNAUTHORIZED);
    	
    	// Set the status code since the handler is now responsible for it
		// This can end up handling both 401 (missing token) and 403 (access denied) exceptions
		if (StringUtils.equals(accessDeniedException.getMessage(), ACCESS_IS_DENIED)) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
		} else {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

}
