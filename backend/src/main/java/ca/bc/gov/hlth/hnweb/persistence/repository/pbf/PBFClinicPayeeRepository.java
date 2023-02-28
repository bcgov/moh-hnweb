package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;

public interface PBFClinicPayeeRepository extends JpaRepository<PBFClinicPayee, Long> {

    /**
     * 
     * @param payeeNumber
     * @return
     */
    @Query("select pcp from PBFClinicPayee pcp"
            + " where pcp.payeeNumber = :payeeNumber"
            + " and pcp.archived = false"
            + " and pcp.effectiveDate <= CURRENT_DATE"
            + " and (pcp.cancelDate IS NULL OR pcp.cancelDate >= CURRENT_DATE)"
            + " order by pcp.effectiveDate DESC")
    public List<PBFClinicPayee> findActiveByPayeeNumber(String payeeNumber);
}
