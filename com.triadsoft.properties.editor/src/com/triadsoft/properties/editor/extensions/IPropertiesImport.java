package com.triadsoft.properties.editor.extensions;

import com.triadsoft.properties.model.Property;

/**
 * This is an interface to modelate an import extension point
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @since 0.8.0
 */
public interface IPropertiesImport extends IPropertiesIE {
	/**
	 * This method receive from extension an array of property objects to import
	 * to editor
	 * 
	 * @param properties
	 *            Array of Property object
	 * @return
	 * @see Property
	 */
	public Property[] importProperties();
}
