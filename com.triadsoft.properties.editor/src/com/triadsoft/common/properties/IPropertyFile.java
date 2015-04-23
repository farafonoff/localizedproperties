package com.triadsoft.common.properties;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

/**
 * Interface that allow to make refactoring on Property model.
 * I made it to make changes on model when avoid PropertyCategory
 * persistance model.
 * @author Triad (flores.leonardo@gmail.com)
 * @since 0.8.3
 */
public interface IPropertyFile {
	public void save(File template) throws IOException, CoreException;
	
	//public void save(boolean escapedUnicode) throws IOException, CoreException;

	public Object setProperty(String key, String value);

	public String[] getKeys();
	
	public File getFile();
}
