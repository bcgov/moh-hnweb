package ca.bc.gov.hlth.hnweb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingException;
import ca.bc.gov.hlth.hnweb.exception.BcscPayeeMappingExceptionType;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.BcscPayeeMapping;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.BcscPayeeMappingRepository;

/**
 * Service for maintaining BCSC Users to MSP Payee Number mappings
 */
@Service
public class BcscPayeeMappingService {

	private static final Logger logger = LoggerFactory.getLogger(BcscPayeeMappingService.class);

	@Autowired
	private BcscPayeeMappingRepository bcscPayeeMappingRepository;

	/**
	 * Adds a mapping.
	 * 
	 * @param bcscPayeeMapping the mapping information being added
	 * @return the newly created entity
	 * @throws BcscPayeeMappingException if the a BcscPayeeMapping entity for the new ID is already present
	 */
	public BcscPayeeMapping add(BcscPayeeMapping bcscPayeeMapping) throws BcscPayeeMappingException {
		Optional<BcscPayeeMapping> optional = bcscPayeeMappingRepository.findById(bcscPayeeMapping.getBcscGuid());
		
		if (optional.isPresent()) {
			logger.info("BcscPayeeMapping entity already exists with ID {}", bcscPayeeMapping.getBcscGuid());
			throw new BcscPayeeMappingException(BcscPayeeMappingExceptionType.ENTITY_ALREADY_EXISTS);
		}

		return bcscPayeeMappingRepository.save(bcscPayeeMapping);
	}
	
	/**
	 * Updates a mapping.
	 * 
	 * @param bcscPayeeMapping the new mapping information
	 * @param id the id of the entity to be updated
	 * @return the updated BcscPayeeMapping entity
	 * @throws BcscPayeeMappingException if the BcscPayeeMapping entity does not exist
	 */
	public BcscPayeeMapping update(BcscPayeeMapping bcscPayeeMapping, String id) throws BcscPayeeMappingException {
		Optional<BcscPayeeMapping> optional = bcscPayeeMappingRepository.findById(id);

		if (optional.isEmpty()) {
			logger.info("BcscPayeeMapping entity not found for ID {}", id);
			throw new BcscPayeeMappingException(BcscPayeeMappingExceptionType.ENTITY_NOT_FOUND);
		}

		BcscPayeeMapping existingEntity = optional.get();		
		existingEntity.setPayeeNumber(bcscPayeeMapping.getPayeeNumber());
		
		return bcscPayeeMappingRepository.save(existingEntity);		
	}
	
	/**
	 * Finds a BcscPayeeMapping for the given id
	 * @param id the id of the entity to be found.
	 * @return an Optional with BcscPayeeMapping entity or empty if not found
	 */
	public Optional<BcscPayeeMapping> find(String id) {
		return bcscPayeeMappingRepository.findById(id);
	}

	/**
	 * Deletes the BcscPayeeMapping entity for the give ID
	 * 
	 * @param id the id of the BcscPayeeMapping entity to be deleted
	 * @throws BcscPayeeMappingException if the BcscPayeeMapping entity does not exist
	 */
	public void delete(String id) throws BcscPayeeMappingException {
		Optional<BcscPayeeMapping> optional = bcscPayeeMappingRepository.findById(id);
		
		if (optional.isEmpty()) {
			logger.info("BcscPayeeMapping entity not found for ID {}", id);
			throw new BcscPayeeMappingException(BcscPayeeMappingExceptionType.ENTITY_NOT_FOUND);
		}
		
		bcscPayeeMappingRepository.deleteById(id);
	}

}

