package ca.bc.gov.hlth.hnweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.PBFClinicPayeeRepository;

/**
 * Service for processing PBF clinic payee requests
 *
 */
@Service
public class PBFClinicPayeeService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PBFClinicPayeeService.class);

    @Autowired
    private PBFClinicPayeeRepository pbfClinicPayeeRepository;
    
    /**
     * Determines the status of the payee by checking if an active record exists in PBFClinicPayee table
     * 
     * @param payeeNumber
     * @return returns true if an active record is found otherwise returns false 
     */
    public boolean getPayeeActiveStatus(String payeeNumber) {
        long activeCount = pbfClinicPayeeRepository.countActivePayeeEntries(payeeNumber);
        
        if (activeCount > 1) {
            logger.warn("{} active records found for Payee {}.", activeCount, payeeNumber);
        }
        
        return activeCount > 0;
    }

}
