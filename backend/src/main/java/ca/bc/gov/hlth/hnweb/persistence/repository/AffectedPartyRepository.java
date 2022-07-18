package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;

public interface AffectedPartyRepository extends JpaRepository<AffectedParty, Long> {

	@Query("SELECT af from AffectedParty af where "	 
		      +"(COALESCE(:organizations, null) is null or af.transaction.organization IN (:organizations)) and "
		      +"(COALESCE(:type, null) is null or af.transaction.type IN (:type)) and "
		      +"(upper(af.transaction.userId)= upper(:userId)) and "
		      +"(af.direction=:direction) and "
		      +"(af.transaction.startTime between :startDate and :endDate)")
	List<AffectedParty> findByTransactionAndDirection(@Param("type") List<String> type,
			@Param("organizations") List<String> organizations, @Param("userId") String userId, @Param("direction") String direction, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
