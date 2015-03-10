package com.triadsoft.properties.excel;

import com.triadsoft.properties.ExcelMessages;
import com.triadsoft.properties.editor.extensions.IPropertiesExport;
import com.triadsoft.properties.model.Property;

/**
 * This is a scafold to export Excel implementation.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class ExcelExport implements IPropertiesExport {

	private static final String MENU_NAME_KEY = "ExcelExport.menu";
	private static final String NAME_KEY = "ExcelExport.name";
	private static final String DESCRIPTION_KEY = "ExcelExport.description";

	public ExcelExport() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * TODO:This method must to be implemented This method receive an array of
	 * properties to export
	 */
	public void exportProperties(Property[] properties) {
		// Make your action here
	}

	public String getDescription() {
		return ExcelMessages.getString(DESCRIPTION_KEY); //$NON-NLS-1$
	}

	public String getName() {
		return ExcelMessages.getString(NAME_KEY); //$NON-NLS-1$
	}

	public String getMenuName() {
		return ExcelMessages.getString(MENU_NAME_KEY); //$NON-NLS-1$
	}
}