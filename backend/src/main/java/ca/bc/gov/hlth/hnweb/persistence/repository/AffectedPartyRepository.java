package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.AffectedParty;

public interface AffectedPartyRepository extends JpaRepository<AffectedParty, Long> {

	@Query("SELECT af from AffectedParty af, Transaction t where "
			  +"(t.transactionId=af.transaction) and "		 
		      +"(t.organization=:organization or :organization is null or :organization = '') and "
		      +"(t.type = :type or :type is null or :type = '') and "
		      +"(t.userId=:userId) and "
		      +"(af.direction='Inbound') and "
		      +"(t.startTime between :startDate and :endDate)")
	List<AffectedParty> findByTransactionAndDirection(@Param("type") String type,
			@Param("organization") String organization, @Param("userId") String userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
