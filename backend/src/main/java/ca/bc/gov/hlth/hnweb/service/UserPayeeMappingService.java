package ca.bc.gov.hlth.hnweb.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingException;
import ca.bc.gov.hlth.hnweb.exception.UserPayeeMappingExceptionType;
import ca.bc.gov.hlth.hnweb.persistence.entity.pbf.UserPayeeMapping;
import ca.bc.gov.hlth.hnweb.persistence.repository.pbf.UserPayeeMappingRepository;

/**
 * Service for maintaining Keycloak Users to MSP Payee Number mappings
 */
@Service
public class UserPayeeMappingService {

	private static final Logger logger = LoggerFactory.getLogger(UserPayeeMappingService.class);

	@Autowired
	private UserPayeeMappingRepository userPayeeMappingRepository;

	/**
	 * Adds a mapping.
	 * 
	 * @param userPayeeMapping the mapping information being added
	 * @return the newly created entity
	 * @throws UserPayeeMappingException if the a UserPayeeMapping entity for the new ID is already present
	 */
	public UserPayeeMapping add(UserPayeeMapping userPayeeMapping) throws UserPayeeMappingException {
		Optional<UserPayeeMapping> optional = userPayeeMappingRepository.findById(userPayeeMapping.getUserGuid());
		
		if (optional.isPresent()) {
			logger.info("UserPayeeMapping entity already exists with ID {}", userPayeeMapping.getUserGuid());
			throw new UserPayeeMappingException(UserPayeeMappingExceptionType.ENTITY_ALREADY_EXISTS);
		}

		return userPayeeMappingRepository.save(userPayeeMapping);
	}
	
	/**
	 * Updates a mapping.
	 * 
	 * @param userPayeeMapping the new mapping information
	 * @param id the id of the entity to be updated
	 * @return the updated UserPayeeMapping entity
	 * @throws UserPayeeMappingException if the UserPayeeMapping entity does not exist
	 */
	public UserPayeeMapping update(UserPayeeMapping userPayeeMapping, String id) throws UserPayeeMappingException {
		Optional<UserPayeeMapping> optional = userPayeeMappingRepository.findById(id);

		if (optional.isEmpty()) {
			logger.info("UserPayeeMapping entity not found for ID {}", id);
			throw new UserPayeeMappingException(UserPayeeMappingExceptionType.ENTITY_NOT_FOUND);
		}

		UserPayeeMapping existingEntity = optional.get();		
		existingEntity.setPayeeNumber(userPayeeMapping.getPayeeNumber());
		
		return userPayeeMappingRepository.save(existingEntity);		
	}
	
	/**
	 * Finds a UserPayeeMapping for the given id
	 * @param id the id of the entity to be found.
	 * @return an Optional with UserPayeeMapping entity or empty if not found
	 */
	public Optional<UserPayeeMapping> find(String id) {
		return userPayeeMappingRepository.findById(id);
	}

	/**
	 * Deletes the UserPayeeMapping entity for the give ID
	 * 
	 * @param id the id of the UserPayeeMapping entity to be deleted
	 * @throws UserPayeeMappingException if the UserPayeeMapping entity does not exist
	 */
	public void delete(String id) throws UserPayeeMappingException {
		Optional<UserPayeeMapping> optional = userPayeeMappingRepository.findById(id);
		
		if (optional.isEmpty()) {
			logger.info("UserPayeeMapping entity not found for ID {}", id);
			throw new UserPayeeMappingException(UserPayeeMappingExceptionType.ENTITY_NOT_FOUND);
		}
		
		userPayeeMappingRepository.deleteById(id);
	}

}

