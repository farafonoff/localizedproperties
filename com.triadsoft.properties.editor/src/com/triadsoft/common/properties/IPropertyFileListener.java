/**
 * Created on Sep 14, 2004
 */
package com.triadsoft.common.properties;

/**
 * Interfase que modela un listener para los archivos de properties
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public interface IPropertyFileListener {
	/**
	 * @param category
	 * @param entry
	 */
	void keyChanged(PropertyCategory category, PropertyEntry entry);

	void valueChanged(PropertyCategory category, PropertyEntry entry);

	void nameChanged(PropertyCategory category);

	void entryAdded(PropertyCategory category, PropertyEntry entry);

	void entryRemoved(PropertyCategory category, PropertyEntry entry);

	void categoryAdded(PropertyCategory category);

	void categoryRemoved(PropertyCategory category);

	void fileChanged(PropertyFile propertyFile);
}