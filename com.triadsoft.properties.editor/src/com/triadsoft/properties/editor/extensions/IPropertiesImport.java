package com.triadsoft.properties.editor.extensions;

import com.triadsoft.properties.model.Property;

/**
 * This is an interface to modelate an import extension point
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public interface IPropertiesImport {
	/**
	 * This is the friendly name to the extension. Ej: Import to Excel
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * This method return a description to the extension. Ej: Import all
	 * properties to an Excel File
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * This method receive from extension an array of property objects to import
	 * to editor
	 * @param properties Array of Property object
	 * @return
	 * @see Property
	 */
	public Property[] importProperties();

	/**
	 * This text will be show in contextual menu to call the implementation of
	 * action
	 * 
	 * @return String Localized Text
	 */
	public String getMenuName();
}
