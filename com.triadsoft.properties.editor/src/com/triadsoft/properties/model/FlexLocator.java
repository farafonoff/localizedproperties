package com.triadsoft.properties.model;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class FlexLocator implements IResourceLocator{

	/**
	 * @see com.triadsoft.properties.model.IResourceLocator#getFileName(org.eclipse.core.resources.IFile)
	 */
	public String getFileName(IFile file,Locale locale) {
		return file.getName();
	}

	/**
	 * @see com.triadsoft.properties.model.IResourceLocator#getLocaleDir(java.util.Locale)
	 */
	public String getLocaleDir(Locale locale) {
		return locale.getLanguage()+"_"+locale.getCountry();
	}

	/**
	 * @see com.triadsoft.properties.model.IResourceLocator#getLocalePath(org.eclipse.core.resources.IFile)
	 */
	public IPath getLocalePath(IFile file) {
		return file.getFullPath().removeLastSegments(2);
	}
}
