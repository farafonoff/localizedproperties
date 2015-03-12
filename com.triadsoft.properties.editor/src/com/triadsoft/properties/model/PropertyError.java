package com.triadsoft.properties.model;

public class PropertyError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6716038141578500427L;

	public static final int INVALID_KEY = 0;
	public static final int VOID_VALUE = 1;
	public static final int UK_TEXT = 2;

	@SuppressWarnings("unused")
	private int type = -1;

	public PropertyError(int errorType, String message) {
		super(message);
		this.type = errorType;
	}
}
