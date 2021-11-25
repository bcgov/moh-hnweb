package ca.bc.gov.hlth.hnweb.converter.fixedwidth;

import ca.bc.gov.hlth.hnweb.model.fixedwidth.FixedWidthDefaults;

public abstract class BaseFixedWidthConverter {
	
	protected FixedWidthDefaults fwDefaults;

	public BaseFixedWidthConverter(FixedWidthDefaults fwDefaults) {
		super();
		this.fwDefaults = fwDefaults;
	}

	public abstract String getTranCode();
}
