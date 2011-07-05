package com.triadsoft.properties.editor.extensions;

import com.triadsoft.properties.model.Property;

/**
 * This interface is to define a sign to export extension plugins
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public interface IPropertiesExport {
	/**
	 * This is the friendly name to the extension. Ej: Export to Excel
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * This method return a description to the extension. Ej: Export all
	 * properties to an Excel File
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * This method send to the extension an array of Property object. Property
	 * is a bean that contains a key property
	 * 
	 * @param properties
	 * @return
	 */
	public void exportProperties(Property[] properties);

	/**
	 * This text will be show in contextual menu to call the implementation of
	 * action
	 * 
	 * @return String Localized Text
	 */
	public String getMenuName();
}
