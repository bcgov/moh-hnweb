package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ca.bc.gov.hlth.hnweb.persistence.entity.Bulletin;	

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

	@Query("select b from Bulletin b where startDate <= CURRENT_DATE and endDate >= CURRENT_DATE order by startDate desc")
	List<Bulletin> findActiveBulletins();

}
