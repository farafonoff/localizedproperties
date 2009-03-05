package com.triadsoft.properties.model;

import java.util.Locale;

public interface IResourceListener {
	/**
	 * Llamado cuando se ha agregado una nueva clave al archivo
	 * de recursos
	 * @param property
	 * @param locale
	 */
	public void entryAdded(Property property, Locale locale);

	/**
	 * Llamado cuando se ha modificado el valor de una propiedad
	 * @param property
	 * @param locale
	 */
	public void entryModified(Property property, Locale locale);

	/**
	 * Se llama cuando se ha borrado una propiedad
	 * @param property
	 * @param locale
	 */
	public void entryDeleted(Property property, Locale locale);
}
