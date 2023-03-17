package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PatientRegister;

public interface PatientRegisterRepository extends JpaRepository<PatientRegister, Long> {
	
	List<PatientRegister> findByPhnAndArchivedIsFalseOrderByEffectiveDateDesc(String phn);
		
}
