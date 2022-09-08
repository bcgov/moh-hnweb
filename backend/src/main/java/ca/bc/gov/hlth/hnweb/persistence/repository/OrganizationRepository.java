package ca.bc.gov.hlth.hnweb.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
	
	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW mspdirect.organization", nativeQuery = true)
	void refreshMaterializedView();

}
