package com.triadsoft.properties;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExcelMessages {
	private static final String BUNDLE_NAME = "com.triadsoft.properties.excel_messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
