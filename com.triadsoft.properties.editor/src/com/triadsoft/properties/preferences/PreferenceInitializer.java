package com.triadsoft.properties.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Clase de inicializacion de los wildcard paths por default
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	// private static final String[] DEFAULT_WILDCARD_PATHS = new String[] {
	// "/{root}/{lang}_{country}/{filename}.{fileextension}",
	// "/{root}/WEB-INF/{filename}.{lang}_{country}.{fileextension}",
	// "/{root}/{filename}.{lang}_{country}.{fileextension}",
	// "/{root}/{filename}.{lang}.{country}.{fileextension}",
	// "/{root}/{lang}.{country}/{filename}.{fileextension}",
	// "/{root}/{lang}/{country}/{filename}.{fileextension}",
	// "/{filename}.{lang}.{fileextension}" };

	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		/** Ahora todo se carga por el archivo preferences.ini */
		// IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// String defaultWildcardPath = store
		// .getDefaultString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES);
		// store.setDefault(PreferenceConstants.WILDCARD_PATHS_PREFERENCES,
		// defaultWildcardPath);
	}

	// private String join(String[] strings, String separator) {
	// StringBuffer buffer = new StringBuffer(strings[0]);
	// for (int i = 1; i < strings.length; i++) {
	// buffer.append(separator).append(strings[i]);
	// }
	// return buffer.toString();
	// }

}
