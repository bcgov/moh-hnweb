package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    long countActivePayeeEntries(String payeeNumber);
    
	@Query("select p2.payeeNumber from PBFClinicPayee p1 inner join PBFClinicPayee p2 on p1.reportGroup = p2.reportGroup "
			+ " where p1.payeeNumber = :payee and p2.archived = false")
	List<String> findPayeeClinicViaReportGroup(@Param("payee") String payee);

}
