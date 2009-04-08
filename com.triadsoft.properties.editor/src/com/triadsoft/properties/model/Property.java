package com.triadsoft.properties.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Property {
	private String key;
	private Map<Locale, String> values = new HashMap<Locale, String>();
	private Map<Locale, Error> errors = new HashMap<Locale, Error>();
	private Boolean invalidValue = false;

	public Boolean getInvalidValue() {
		return invalidValue;
	}

	public void setInvalidValue(Boolean invalidValue) {
		this.invalidValue = invalidValue;
	}

	public Property(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue(Locale locale) {
		return values.get(locale);
	}

	public void setValue(Locale locale, String value) {
		if (value == null) {
			invalidValue = true;
		}
		this.values.put(locale, value);
	}

	public void addError(Locale locale, Error error) {
		errors.put(locale, error);
	}

	public Map<Locale, Error> getErrors() {
		return errors;
	}
}
