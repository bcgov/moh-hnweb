package ca.bc.gov.hlth.hnweb.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ca.bc.gov.hlth.hnweb.persistence.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
	
	@Modifying
	@Query(value = "REFRESH MATERIALIZED VIEW mspdirect.organization", nativeQuery = true)
	void refreshMaterializedView();
	
	@Query(value = "select * from mspdirect.organization o where o.organization_name is not null "
			+ " or (o.organization_name is null and not exists (select organization from mspdirect.organization o2 where o2.organization_name is not null and o.organization = o2.organization))"
			+ "order by o.organization", nativeQuery = true)
	List<Organization> findUnique();

}
