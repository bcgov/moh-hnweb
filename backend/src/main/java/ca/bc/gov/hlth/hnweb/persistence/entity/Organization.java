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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;

		return true;
	}

}