package ca.bc.gov.hlth.hnweb.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
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
	public void testGetBulletins() throws Exception {
		createBulletins();

        ResponseEntity<List<BulletinModel>> response = bulletinController.getBulletins();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BulletinModel> bulletins = response.getBody();
        
        // Check the number of valid records
		assertEquals(2, bulletins.size());
		
		// Check the sort
		assertEquals(2l, bulletins.get(0).getId());
		assertEquals(1l, bulletins.get(1).getId());
	}

	private void createBulletins() {
		Calendar futureEndDate = Calendar.getInstance();
		futureEndDate.add(Calendar.MONTH, 12);

		// Valid - older
		Bulletin bulletin1 = new Bulletin();
		bulletin1.setBulletinId(1l);
		
		Calendar startDate1 = Calendar.getInstance();
		startDate1.add(Calendar.MONTH, -2);
		bulletin1.setStartDate(startDate1.getTime());
		bulletin1.setEndDate(futureEndDate.getTime());
		bulletin1.setContent("This is an older bulletin.");

		bulletinRepository.save(bulletin1);
		
		// Valid - newest
		Bulletin bulletin2 = new Bulletin();
		bulletin2.setBulletinId(2l);
		Calendar startDate2 = Calendar.getInstance();
		startDate2.add(Calendar.MONTH, -1);
		bulletin2.setStartDate(startDate2.getTime());
		bulletin2.setEndDate(futureEndDate.getTime());
		bulletin2.setContent("This is the newest bulletin. It has a <a href='http://google.ca'>hyperlink</a>.");		

		bulletinRepository.save(bulletin2);
		
		// Not valid - past
		Bulletin bulletin3 = new Bulletin();
		bulletin3.setBulletinId(3l);
		
		Calendar startDate3 = Calendar.getInstance();
		startDate3.add(Calendar.MONTH, -2);
		bulletin3.setStartDate(startDate3.getTime());
		
		Calendar endDate3 = Calendar.getInstance();
		endDate3.add(Calendar.MONTH, -1);
		bulletin3.setEndDate(endDate3.getTime());
		bulletin3.setContent("This is a past bulletin.");

		bulletinRepository.save(bulletin3);
		
		// Not valid - future
		Bulletin bulletin4 = new Bulletin();
		bulletin4.setBulletinId(3l);
		
		Calendar startDate4 = Calendar.getInstance();
		startDate4.add(Calendar.MONTH, 1);
		bulletin4.setStartDate(startDate4.getTime());
		
		Calendar endDate4 = Calendar.getInstance();
		endDate4.add(Calendar.MONTH, 2);
		bulletin4.setEndDate(endDate4.getTime());
		bulletin4.setContent("This is a future bulletin.");

		bulletinRepository.save(bulletin3);
	}
		
}
