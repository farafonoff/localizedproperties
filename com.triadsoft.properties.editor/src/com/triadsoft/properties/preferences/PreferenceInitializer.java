package com.triadsoft.properties.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

/**
 * Clase de inicializacion de los wildcard paths por default
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		/** Ahora todo se carga por el archivo preferences.ini */
		System.out.println("Recuperando los defaults");
	}
}
