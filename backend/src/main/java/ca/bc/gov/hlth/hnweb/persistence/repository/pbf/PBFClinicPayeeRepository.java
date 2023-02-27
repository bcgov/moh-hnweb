package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.PBFClinicPayee;

public interface PBFClinicPayeeRepository extends JpaRepository<PBFClinicPayee, Long> {

    public PBFClinicPayee findByPayeeNumber(String payeeNumber); 
}
