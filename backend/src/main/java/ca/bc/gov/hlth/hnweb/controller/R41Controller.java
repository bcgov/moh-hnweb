package ca.bc.gov.hlth.hnweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.hlth.hnweb.model.R41Response;

@RestController
public class R41Controller {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(R41Controller.class);

	@GetMapping("/r41")
	public List<R41Response> getR41(@RequestParam(name = "phn", required = false) List<String> phns) {
		logger.info("getR41 request: {} ", ArrayUtils.toString(phns));
		List<R41Response> r41Responses = new ArrayList<R41Response>();
		if (phns == null) {
			return r41Responses;	
		}
		phns.forEach(phn -> {
			r41Responses.add(new R41Response("HJMB001I SUCCESFULLY COMPLETED", String.format("HRPB115E PHN:%s NOT FOUND", phn)));	
		});
		logger.info("getR41 response: {} ", ArrayUtils.toString(r41Responses));
		return r41Responses;
	}

}
