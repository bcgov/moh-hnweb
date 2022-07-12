package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;

public interface AffectedPartyRepository extends JpaRepository<AffectedParty, Long> {

	@Query("SELECT af from AffectedParty af where "		 
		      +"(af.transaction.organization IN (:organizations) or :organizations is null) and "
		      +"(af.transaction.type IN (:types) or :types is null) and "
		      +"(af.transaction.userId=:userId) and "
		      +"(af.direction='Inbound') and "
		      +"(af.transaction.startTime between :startDate and :endDate)")
	List<AffectedParty> findByTransactionAndDirection(@Param("types") List<String> types,
			@Param("organizations") List<String> organizations, @Param("userId") String userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
