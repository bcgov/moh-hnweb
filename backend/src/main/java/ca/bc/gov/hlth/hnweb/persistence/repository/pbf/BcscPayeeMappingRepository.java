package ca.bc.gov.hlth.hnweb.persistence.repository.pbf;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.BcscPayeeMapping;

/**
 * Repository for {@link BcscPayeeMapping}
 *
 */
public interface BcscPayeeMappingRepository extends JpaRepository<BcscPayeeMapping, String> {

	public List<BcscPayeeMapping> findByPayeeNumber(String payeeNumber);
	
}
