package ca.bc.gov.hlth.hnweb.persistence.entity.pbf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity for table bcsc_payee_mapping which contains BCSC Users to MSP Payee Number mappings
 *
 */
@Entity
@Table(name = "bcsc_payee_mapping")
public class BcscPayeeMapping {

	@Id
	@Column(name = "bcsc_guid")
	private String bcscGuid;

	@Column(name = "msp_payee_number", nullable = false)
	private String payeeNumber;

	public String getBcscGuid() {
		return bcscGuid;
	}

	public void setBcscGuid(String bcscGuid) {
		this.bcscGuid = bcscGuid;
	}

	public String getPayeeNumber() {
		return payeeNumber;
	}

	public void setPayeeNumber(String payeeNumber) {
		this.payeeNumber = payeeNumber;
	}

	@Override
	public String toString() {
		return "BcscPayeeMapping [bcscGuid=" + bcscGuid + ", payeeNumber=" + payeeNumber + "]";
	}

}