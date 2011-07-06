package com.triadsoft.properties.editor.extensions;

import com.triadsoft.properties.model.Property;

/**
 * This interface is to define a sign to export extension plugins
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @since 0.8.0
 */
public interface IPropertiesExport extends IPropertiesIE {

	/**
	 * This method send to the extension an array of Property object. Property
	 * is a bean that contains a key property
	 * 
	 * @param properties
	 * @return
	 */
	public void exportProperties(Property[] properties);
}
