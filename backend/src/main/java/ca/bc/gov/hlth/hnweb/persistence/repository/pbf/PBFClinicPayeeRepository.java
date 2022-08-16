package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;

public interface PBFClinicPayeeRepository extends JpaRepository<PBFClinicPayee, Long> {

	@Query("select p.pbfClinicPayeeId from PBFClinicPayee p " + "where p.payeeNumber in (:payeeList)")
	List<String> findPBFClinicPayeeByPayeeNumber(@Param("payeeList") List<String> payeeList);
}
