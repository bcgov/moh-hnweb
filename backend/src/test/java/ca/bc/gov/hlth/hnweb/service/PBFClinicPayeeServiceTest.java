package ca.bc.gov.hlth.hnweb.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Sql({ "classpath:scripts/bcsc_payee_mapping.sql", "classpath:scripts/pbf_clinic_payee.sql" })
public class PBFClinicPayeeServiceTest {

    @Autowired
    private PBFClinicPayeeService pbfClinicPayeeService;

    @Test
    public void testfindActiveStatusByPayeeNumber_payee_not_found() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("1234");
        assertFalse(isActive);
    }

    @Test
    public void testfindActiveStatusByPayeeNumber_payee_archived() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("00053");
        assertFalse(isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_no_cancel_date() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("00023");
        assertTrue(isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_future_cancel_date() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("00033");
        assertTrue(isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_effective_today() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("00043");
        assertTrue(isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_not_yet_active_effective_tomorrow() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("T0055");
        assertFalse(isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_cancelled_today() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("T0053");
        assertTrue(isActive);
    }

    @Test
    public void testfindActiveStatusByPayeeNumber_payee_not_active_cancelled_yesterday() {
                
        boolean isActive = pbfClinicPayeeService.getPayeeActiveStatus("X0053");
        assertFalse(isActive);
    }
    
}
