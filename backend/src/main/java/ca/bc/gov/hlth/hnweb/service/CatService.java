package ca.bc.gov.hlth.hnweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.hlth.hnweb.model.CatFact;

/**
 * TODO (weskubo-cgi) Remove this class. It's just for unit testing demonstration purposes.
 */
@Service
public class CatService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public CatFact getFact() {
		return restTemplate.getForEntity("https://catfact.ninja/fact", CatFact.class).getBody();		
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
