package ca.bc.gov.hlth.hnweb.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.rest.bulletin.BulletinModel;
import ca.bc.gov.hlth.hnweb.persistence.entity.Bulletin;
import ca.bc.gov.hlth.hnweb.service.BulletinService;

/**
 * Handles requests related to bulletins.
 */
@RequestMapping("/bulletins")
@RestController
public class BulletinController {

	@Autowired
	private BulletinService bulletinService;

	/**
	 * Retrieves the list of current Bulletins.
	 * 
	 * @return The list of Bulletins.
	 */
	@GetMapping
	public ResponseEntity<List<BulletinModel>> getBulletins() {
		List<BulletinModel> bulletins = convertBulletins(bulletinService.getActiveBulletins());
		ResponseEntity<List<BulletinModel>> responseEntity = ResponseEntity.ok(bulletins);
		return responseEntity;
	}

	private List<BulletinModel> convertBulletins(List<Bulletin> bulletins) {
		List<BulletinModel> bulletinModels = new ArrayList<>();
		bulletins.forEach(bulletin -> {
			BulletinModel model = new BulletinModel();
			model.setId(bulletin.getBulletinId());
			model.setStartDate(Instant.ofEpochMilli(bulletin.getStartDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
			model.setEndDate(Instant.ofEpochMilli(bulletin.getEndDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
			model.setContent(bulletin.getContent());

			bulletinModels.add(model);

		});
		return bulletinModels;
	}

}
