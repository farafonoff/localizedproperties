package com.triadsoft.properties.excel;

import com.triadsoft.properties.ExcelMessages;
import com.triadsoft.properties.editor.extensions.IPropertiesImport;
import com.triadsoft.properties.model.Property;

public class ExcelImport implements IPropertiesImport {

	private static final String IMPORT_DESCRIPTION_KEY = "ExcelImport.description";
	private static final String IMPORT_NAME_KEY = "ExcelImport.name";
	private static final String IMPORT_MENU_NAME_KEY = "ExcelImport.menu";

	public ExcelImport() {
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return ExcelMessages.getString(IMPORT_DESCRIPTION_KEY); //$NON-NLS-1$
	}

	public String getMenuName() {
		return ExcelMessages.getString(IMPORT_MENU_NAME_KEY); //$NON-NLS-1$
	}

	public String getName() {
		return ExcelMessages.getString(IMPORT_NAME_KEY); //$NON-NLS-1$
	}

	/**
	 * TODO:This method must to be implemented
	 * This method return an array of Property objects to import
	 * @see Property
	 */
	public Property[] importProperties() {
		return new Property[0];
	}
}