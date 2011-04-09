package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

public interface IFilesDiscoverer {

	public static final String PATHDISCOVERY_UNKNOWN_WILDCARD_MESSAGE = "pathdiscovery.unknownWildcard.message";
	public static final String PATHDISCOVERY_UNKNOWN_FILENAME_MESSAGE = "pathdiscovery.unknownFilename.message";

	/**
	 * Devuelve el path ya parseado como el path desde el proyecto hasta el
	 * directorio que contiene al archivo de recursos
	 * 
	 * @return IPath con el path parseado
	 */
	public abstract IPath getPath();

	/**
	 * Devuelve el nombre del archivo sin extension.
	 * 
	 * @return String Nombre del srchivo
	 */
	public abstract String getFilename();

	public abstract void searchFiles();

	/**
	 * Devuelve un mapa con los recursos encontrados dentro del path,
	 * organizados por su locale
	 * 
	 * @return Map<Locale,IFile>
	 */
	public abstract Map<Locale, IFile> getResources();

	/**
	 * Devuelve el objeto WildcardPath que se uso para descubir los datos de la
	 * ubicacion de los recursos
	 * 
	 * @return Devuelve el wilcard path encontrado a partir del archivo
	 */
	public abstract IWildcardPath getWildcardPath();

	/**
	 * Devuelve el locale contenido en el archivo pasado en el constructor
	 * 
	 * @return Locale del archivo abierto
	 */
	public abstract Locale getDefaultLocale();

}