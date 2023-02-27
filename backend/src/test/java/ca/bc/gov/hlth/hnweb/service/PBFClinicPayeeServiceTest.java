package ca.bc.gov.hlth.hnweb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.hlth.hnweb.model.rest.pbf.PayeeStatus;

@SpringBootTest
@Transactional
@Sql({ "classpath:scripts/bcsc_payee_mapping.sql", "classpath:scripts/pbf_clinic_payee.sql" })
public class PBFClinicPayeeServiceTest {

    @Autowired
    private PBFClinicPayeeService pbfClinicPayeeService;

    @Test
    public void testGetPayeeStatus_payee_not_found() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("1234");
        assertEquals(PayeeStatus.NOT_FOUND, payeeStatus);
    }

    @Test
    public void testGetPayeeStatus_payee_archived() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("00053");
        assertEquals(PayeeStatus.ARCHIVED, payeeStatus);
    }
    
    @Test
    public void testGetPayeeStatus_payee_is_active_no_cancel_date() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("00023");
        assertEquals(PayeeStatus.ACTIVE, payeeStatus);
    }
    
    @Test
    public void testGetPayeeStatus_payee_is_active_future_cancel_date() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("00033");
        assertEquals(PayeeStatus.ACTIVE, payeeStatus);
    }
    
    @Test
    public void testGetPayeeStatus_payee_is_active_effective_today() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("00043");
        assertEquals(PayeeStatus.ACTIVE, payeeStatus);
    }
    
    @Test
    public void testGetPayeeStatus_payee_not_yet_active_effective_tomorrow() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("T0055");
        assertEquals(PayeeStatus.NOT_YET_ACTIVE, payeeStatus);
    }
    
    @Test
    public void testGetPayeeStatus_payee_is_active_cancelled_today() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("T0053");
        assertEquals(PayeeStatus.ACTIVE, payeeStatus);
    }

    @Test
    public void testGetPayeeStatus_payee_not_active_cancelled_yesterday() {
                
        PayeeStatus payeeStatus = pbfClinicPayeeService.getPayeeStatus("X0053");
        assertEquals(PayeeStatus.CANCELLED, payeeStatus);
    }
    
}
