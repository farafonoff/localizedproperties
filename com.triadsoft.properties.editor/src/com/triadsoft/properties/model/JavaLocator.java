package com.triadsoft.properties.model;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public class JavaLocator implements IResourceLocator {

	public String getFileName(IFile file, Locale locale) {
		String fileName = file.getName().substring(0,
				file.getName().indexOf(file.getFileExtension())-1); 
		if(fileName.indexOf(".") != -1){
			fileName = fileName.substring(0, fileName.indexOf("."));
		}
		return fileName+"_"+locale.getLanguage()+"_"+locale.getCountry()+".properties";
	}

	public String getLocaleDir(Locale locale) {
		//En java no influye en el path el locale
		//solo se aplica al nombre del archivo
		return "";
	}

	public IPath getLocalePath(IFile file) {
		return file.getFullPath().removeLastSegments(1);
	}
}
