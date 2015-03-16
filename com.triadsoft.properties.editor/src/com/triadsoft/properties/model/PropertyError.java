package com.triadsoft.properties.model;

public class PropertyError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6716038141578500427L;

	public static final int INVALID_KEY = 0;
	public static final int VOID_VALUE = 1;
	public static final int UK_TEXT = 2;

	private int type = -1;

	public PropertyError(int errorType, String message) {
		super(message);
		this.setType(errorType);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
