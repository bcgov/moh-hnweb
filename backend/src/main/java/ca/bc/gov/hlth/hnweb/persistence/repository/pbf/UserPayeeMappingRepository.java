package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.UserPayeeMapping;

/**
 * Repository for {@link UserPayeeMapping}
 *
 */
public interface UserPayeeMappingRepository extends JpaRepository<UserPayeeMapping, String> {

	public List<UserPayeeMapping> findByPayeeNumber(String payeeNumber);
	
}
