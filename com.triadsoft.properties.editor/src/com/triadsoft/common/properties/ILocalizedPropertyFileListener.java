package com.triadsoft.common.properties;

import java.util.Locale;

/**
 * Interface para los listeners que reciben además de la clave y valor, el
 * locale para poder identificar el idioma donde ocurrio el cambio
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public interface ILocalizedPropertyFileListener {
	void valueChanged(String key, String value, Locale locale);

	void addKey(String key);
}
