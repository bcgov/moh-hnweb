package ca.bc.gov.hlth.hnweb.model.rest.auditreport;

public class AuditOrganization {

	private String id;

	private String name;

	public AuditOrganization(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
