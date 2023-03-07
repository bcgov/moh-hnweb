package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;

public interface PBFClinicPayeeRepository extends JpaRepository<PBFClinicPayee, Long> {

    /**
     * Counts the number of active records for a payee
     * 
     * @param payeeNumber
     * @return the number of active records for a payee
     */
    @Query("select count(pcp) from PBFClinicPayee pcp"
            + " where pcp.payeeNumber = :payeeNumber"
            + " and pcp.archived = false"
            + " and pcp.effectiveDate <= CURRENT_DATE"
            + " and (pcp.cancelDate IS NULL OR pcp.cancelDate >= CURRENT_DATE)")
    public long countActivePayeeEntries(String payeeNumber);
}
