package ca.bc.gov.hlth.hnweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.hlth.hnweb.persistence.entity.Bulletin;
import ca.bc.gov.hlth.hnweb.persistence.repository.BulletinRepository;

@Service
public class BulletinService {
	
	@Autowired
	private BulletinRepository bulletinRepository;
	
	public List<Bulletin> getActiveBulletins() {
		return bulletinRepository.findActiveBulletins();
	}

}
