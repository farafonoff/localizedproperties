package com.triadsoft.properties.editor.extensions;

/**
 * This is the common interface for import/export extensions.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public interface IPropertiesIE {
	/**
	 * This is the friendly name to the extension. Ej: Import to Excel,Export to
	 * Excel
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * This method return a description to the extension. Ej: Import all,Export
	 * all properties to an Excel File
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * This text will be show in contextual menu to call the implementation of
	 * action
	 * 
	 * @return String Localized Text
	 */
	public String getMenuName();
}
