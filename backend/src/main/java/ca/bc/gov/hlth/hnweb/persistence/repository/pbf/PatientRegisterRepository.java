package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;

public interface PatientRegisterRepository extends JpaRepository<PatientRegister, Long> {

	@Query("select p from PatientRegister p "
	+ "where p.payeeNumber in ("
	+ "select p2.payeeNumber from PBFClinicPayee p1 inner join PBFClinicPayee p2 "
    + "on p1.reportGroup="
	+ "p2.reportGroup where p1.payeeNumber= :payee )")
	//+ "and p1.effectiveDate<= "
	//+ "currentDate and currentDate<= "
	//+ "p1.cancelDate and p2.effectiveDate<= "
	//+ "currentDate and currentDate<=p2.cancelDate)")
	List<PatientRegister> findPatientRegisterByPayeeClinic(@Param("payee") String payee);

}
