package ca.bc.gov.hlth.hnweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("1234");
        assertEquals(false, isActive);
    }

    @Test
    public void testfindActiveStatusByPayeeNumber_payee_archived() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("00053");
        assertEquals(false, isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_no_cancel_date() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("00023");
        assertEquals(true, isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_future_cancel_date() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("00033");
        assertEquals(true, isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_effective_today() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("00043");
        assertEquals(true, isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_not_yet_active_effective_tomorrow() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("T0055");
        assertEquals(false, isActive);
    }
    
    @Test
    public void testfindActiveStatusByPayeeNumber_payee_is_active_cancelled_today() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("T0053");
        assertEquals(true, isActive);
    }

    @Test
    public void testfindActiveStatusByPayeeNumber_payee_not_active_cancelled_yesterday() {
                
        boolean isActive = pbfClinicPayeeService.findActiveStatusByPayeeNumber("X0053");
        assertEquals(false, isActive);
    }
    
}
