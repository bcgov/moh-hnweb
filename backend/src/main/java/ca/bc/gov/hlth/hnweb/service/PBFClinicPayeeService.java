package ca.bc.gov.hlth.hnweb.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.model.rest.pbf.PayeeStatus;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PBFClinicPayeeRepository;

/**
 * Service for processing PBF clinic payee requests
 *
 */
@Service
public class PBFClinicPayeeService extends BaseService {

    @Autowired
    private PBFClinicPayeeRepository pbfClinicPayeeRepository;
    
    /**
     * Determines the status of the payee
     * 
     * @param payeeNumber
     * @return returns the status of the payee otherwise null if the payee could not be found 
     */
    public PayeeStatus getPayeeStatus(String payeeNumber) {
        
        PayeeStatus payeeStatus = null;        
        Date now = new Date();

        PBFClinicPayee pbfClinicPayee = pbfClinicPayeeRepository.findByPayeeNumber(payeeNumber);
        
        if (pbfClinicPayee == null) {
            payeeStatus = PayeeStatus.NOT_FOUND;
        } else if (pbfClinicPayee.getArchived()) {
            payeeStatus = PayeeStatus.ARCHIVED;
        } else {
//          [payee cancel date] >= today >= [payee effective date]
            if (pbfClinicPayee.getCancelDate() != null && pbfClinicPayee.getCancelDate().before(DateUtils.addDays(now, -1))) {
                payeeStatus = PayeeStatus.CANCELLED;
            } else if (pbfClinicPayee.getEffectiveDate().after(now)) {
                payeeStatus = PayeeStatus.NOT_YET_ACTIVE;
            } else {
                payeeStatus = PayeeStatus.ACTIVE;
            }        
        }
        
        return payeeStatus;        
    }

}
