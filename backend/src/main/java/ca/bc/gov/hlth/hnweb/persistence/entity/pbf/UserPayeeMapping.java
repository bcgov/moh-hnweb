package ca.bc.gov.hlth.hnweb.persistence.entity.pbf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for table user_payee_mapping which contains Keycloak Users to MSP Payee Number mappings
 *
 */
@Entity
@Table(name = "user_payee_mapping")
public class UserPayeeMapping {

	@Id
	@Column(name = "user_guid")
	private String userGuid;

	@Column(name = "msp_payee_number", nullable = false)
	private String payeeNumber;

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public String getPayeeNumber() {
		return payeeNumber;
	}

	public void setPayeeNumber(String payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	@Override
	public String toString() {
		return "UserPayeeMapping [userGuid=" + userGuid + ", payeeNumber=" + payeeNumber + "]";
	}



}