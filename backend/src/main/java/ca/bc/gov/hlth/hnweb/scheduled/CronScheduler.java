package ca.bc.gov.hlth.hnweb.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.hlth.hnweb.persistence.repository.OrganizationRepository;

@Component
public class CronScheduler {
	private static final Logger logger = LoggerFactory.getLogger(CronScheduler.class);
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Scheduled(cron = "${cron.refresh-org-view}")
	@Transactional
	public void refreshOrganizationView() {
		logger.info("Refreshing materialized view: ORGANIZATION");
	    organizationRepository.refreshMaterializedView();
	}

}