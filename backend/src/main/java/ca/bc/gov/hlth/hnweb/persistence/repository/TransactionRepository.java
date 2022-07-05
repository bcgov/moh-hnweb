package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.bc.gov.hlth.hnweb.persistence.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

	@Query("SELECT DISTINCT organization FROM Transaction")
	List<String> findDistinctOrganization();

	@Query("SELECT t from Transaction t, AffectedParty af where t.type =:type AND t.organization =:organization and t.transactionId=af.transaction")
	List<Transaction> findByTypeAndOrganization(@Param("type") String type,
			@Param("organization") String organization);

}
