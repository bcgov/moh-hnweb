package ca.bc.gov.hlth.hnweb.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusEnum {
	@JsonProperty("error") ERROR,
	@JsonProperty("success") SUCCESS,
	@JsonProperty("warning") WARNING
}
