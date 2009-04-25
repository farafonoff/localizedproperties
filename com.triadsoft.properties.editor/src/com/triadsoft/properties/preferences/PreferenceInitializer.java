package com.triadsoft.properties.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.triadsoft.properties.editor.Activator;

/**
 * Clase de inicializacion de los wildcard paths por default
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static final String[] DEFAULT_WILDCARD_PATHS = new String[] {
			"/{root}/{lang}_{country}/{filename}.{fileextension}",
			"/{root}/WEB-INF/{filename}.{lang}_{country}.{fileextension}",
			"/{root}/{filename}.{lang}_{country}.{fileextension}",
			"/{root}/{filename}.{lang}.{country}.{fileextension}",
			"/{root}/{lang}.{country}/{filename}.{fileextension}",
			"/{root}/{lang}/{country}/{filename}.{fileextension}",
			"/{filename}.{lang}.{fileextension}"};

	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.WILDCARD_PATHS_PREFERENCES, join(
				DEFAULT_WILDCARD_PATHS, "|"));
	}

	private String join(String[] strings, String separator) {
		StringBuffer buffer = new StringBuffer(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			buffer.append(separator).append(strings[i]);
		}
		return buffer.toString();
	}
}
