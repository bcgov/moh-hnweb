package ca.bc.gov.hlth.hnweb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;
import ca.bc.gov.hlth.hnweb.persistence.entity.TransactionEventType;
import ca.bc.gov.hlth.hnweb.service.AuditService;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
    
    @Autowired
    private AuditService auditService;
    
    

	public CustomAccessDeniedHandler() {
		super();
	}



	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		System.out.println("handle");
		String ip = request.getRemoteAddr();

    	Transaction transaction = auditService.createTransaction(ip, TransactionType.UNKNOWN);    	
    	auditService.createTransactionEvent(transaction, TransactionEventType.UNAUTHORIZED);
	}

}
