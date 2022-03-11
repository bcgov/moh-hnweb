package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.hlth.hnweb.BaseControllerTest;
import ca.bc.gov.hlth.hnweb.model.rest.bulletin.BulletinModel;
import ca.bc.gov.hlth.hnweb.persistence.entity.Bulletin;
import ca.bc.gov.hlth.hnweb.persistence.repository.BulletinRepository;

public class BulletinControllerTest extends BaseControllerTest {

	@Autowired
	private BulletinController bulletinController;
	
	@Autowired
	private BulletinRepository bulletinRepository;

	@Test
	public void testGetBulletins_success() throws Exception {
		createBulletins();

        ResponseEntity<List<BulletinModel>> response = bulletinController.getBulletins();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BulletinModel> bulletins = response.getBody();
		assertEquals(3, bulletins.size());
        
	}
	
	private void createBulletins() {
		Bulletin bulletin1 = new Bulletin();
		bulletin1.setBulletinId(1l);
		bulletin1.setStartDate(new Date());
		bulletin1.setEndDate(new Date());
		bulletin1.setContent("This is the oldest bulletin. It has a <a href='http://google.ca'>hyperlink</a>.");
		
		bulletinRepository.save(bulletin1);
		
		Bulletin bulletin2 = new Bulletin();
		bulletin2.setBulletinId(2l);
		bulletin2.setStartDate(new Date());
		bulletin2.setEndDate(new Date());
		bulletin2.setContent("This is another bulletin.");
		
		bulletinRepository.save(bulletin2);
		
		Bulletin bulletin3 = new Bulletin();
		bulletin3.setBulletinId(3l);
		bulletin3.setStartDate(new Date());
		bulletin3.setEndDate(new Date());
		bulletin3.setContent("This is the newest bulletin.");

		bulletinRepository.save(bulletin3);
	
	}
	
	
}


