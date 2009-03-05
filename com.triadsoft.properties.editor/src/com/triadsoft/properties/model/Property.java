package com.triadsoft.properties.model;

public class Property {
	private String key;
	private String value;
	private String secondValue;

	public Property(String key, String value) {
		this(key,value,null);
	}
	
	public Property(String key, String value,String secondValue) {
		this.key = key;
		this.value = value;
		this.secondValue = secondValue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}
}
