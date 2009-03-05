package com.triadsoft.properties.model;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public interface IResourceLocator {

	/**
	 * Devuelve el nombre del archivo.En algunos casos el locale forma parte del 
	 * nombre del archivo como es el caso de los resource bundle de java.
	 * @param file
	 * @return
	 */
	public String getFileName(IFile file,Locale locale);
	
	/**
	 * Este metodo debe devolver el path donde se encuantran los archivos de
	 * recursos. Ej. Para Flex es locale, para java será el paquete donde se
	 * encuentra el path
	 * @param file
	 * @return
	 */
	public IPath getLocalePath(IFile file);

	/**
	 * Devuelve el directorio segun el locale. Ej. En el caso de Flex el
	 * directorio donde se encuentran los archivos de recursos es parte del
	 * path.
	 * 
	 * @param locale
	 * @return String Con el directorio ej. en_US
	 */
	public String getLocaleDir(Locale locale);
}
