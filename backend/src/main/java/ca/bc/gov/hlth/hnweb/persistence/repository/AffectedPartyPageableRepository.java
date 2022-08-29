package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;

public interface AffectedPartyPageableRepository extends PagingAndSortingRepository<AffectedParty, Long> {

	@Query("SELECT af from AffectedParty af where "	 
		      +"(COALESCE(:organizations, null) is null or af.transaction.organization IN (:organizations)) and "
		      +"(COALESCE(:type, null) is null or af.transaction.type IN (:type)) and "
		      +"(upper(af.transaction.userId)= upper(:userId)) and "
		      +"(af.direction=:direction) and "
		      +"(date_trunc('day', af.transaction.startTime) between :startDate and :endDate) "
		      + "order by af.transaction.startTime desc")
	Page<AffectedParty> findByTransactionAndDirection(@Param("type") List<String> type,
			@Param("organizations") List<String> organizations, @Param("userId") String userId, @Param("direction") String direction, @Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

}
