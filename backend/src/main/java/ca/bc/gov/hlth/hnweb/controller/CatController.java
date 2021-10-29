package ca.bc.gov.hlth.hnweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ca.bc.gov.hlth.hnweb.model.CatFact;
import ca.bc.gov.hlth.hnweb.service.CatService;

/**
 * TODO (weskubo-cgi) Remove this class. It's just for unit testing demonstration purposes.
 */
@RequestMapping("/cats")
@RestController
public class CatController {
	
	@Autowired
	private CatService catService;
	
	@GetMapping("/fact")
	public ResponseEntity<CatFact> getFact() {

		try {
			CatFact fact = catService.getFact();
			
			ResponseEntity<CatFact> response = ResponseEntity.ok(fact);

			return response;	
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad /sample/cats request", e);
		}
		
	}

}
