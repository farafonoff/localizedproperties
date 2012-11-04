package com.triadsoft.common.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class gives services to translate texts
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class LocalizedPropertiesMessages {
	private static final String BUNDLE_NAME = "com.triadsoft.properties.editor.localizedProperties";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static String getString(String key, Object[] params) {
		String resource = getString(key);
		String parametrized = resource;
		if (key.equals(resource)) {
			return key;
		}
		for (int i = 0; i < params.length; i++) {
			parametrized = resource.replaceAll("\\{" + i + "\\}",
					params[i].toString());
		}
		return parametrized;
	}

	// public static String getString(String key) {
	// try {
	// return RESOURCE_BUNDLE.getString(key);
	// } catch (MissingResourceException e) {
	// return key;
	// }
	// }
	//
	// public static String getString(String key, Object[] params) {
	// String resource = getString(key);
	// String parametrized = resource;
	// if (key.equals(resource)) {
	// return key;
	// }
	// for (int i = 0; i < params.length; i++) {
	// parametrized = resource.replaceAll("\\{" + i + "\\}",
	// params[i].toString());
	// }
	// return parametrized;
	// }
}
