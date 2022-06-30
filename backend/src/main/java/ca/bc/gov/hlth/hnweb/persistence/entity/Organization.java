package ca.bc.gov.hlth.hnweb.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Organization {

	@Id
	private String organization;

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organizationId) {
		this.organization = organizationId;
	}

}