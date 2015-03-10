package com.triadsoft.properties.model.utils;

import java.util.Locale;

import org.eclipse.core.resources.IFile;

public interface IWildcardPath {

	public static final String COUNTRY_REGEX = "[A-Z]{2}";
	public static final String LANGUAGE_REGEX = "[a-z]{2}";

	public static final String OPTIONAL_PARAMETERS = "\\([a-zA-Z0-9\\.\\_\\{\\}\\-]*\\)\\d";

	/**
	 * It supports all letters, undescores
	 */
	public static final String TEXT_REGEX = "[a-zA-Z\\-\\_][^/]+";
	/**
	 * It's use to add a variant of language
	 */
	public static final String VARIANT_REGEX = "[A-Z]+";

	/** Constantes para los wildcards */
	public static final String ROOT_WILDCARD = "{root}";
	public static final String FILENAME_WILDCARD = "{filename}";
	public static final String FILE_EXTENSION_WILDCARD = "{fileextension}";
	public static final String COUNTRY_WILDCARD = "{country}";
	public static final String LANGUAGE_WILDCARD = "{lang}";
	public static final String VARIANT_WILDCARD = "{variant}";
	
	/** Regex for wildcards removal - issue 86*/
	public static final String REMOVAL_REGEX = "[^\\}]?\\{[%1$s]*\\}";


	/**
	 * Es el numero de parametros opcionales que ser&aacute;n soportados. Éste
	 * numero sirve para poner un limite en la busqueda de las opciones, y para
	 * validar el límite de opciones cuando se ingresa un nuevo wildcard path.
	 */
	public static final int MAXIMUM_OPTIONALS = 5;

	public String getWildcardpath();

	public String getFileName();

	public String getFileExtension();

	public String getCountry();

	public String getLanguage();

	public Locale getLocale();

	public String getRoot();

	/**
	 * Este metodo se encarga de obtener del path pasado como parï¿½metro los
	 * datos correspondientes a cada wildcard
	 */
	public Boolean parse(String filepath);

	/**
	 * This method must to use information about optional parameters.
	 * (.{country})1
	 * 
	 * @param offset
	 * @return
	 */
	public Boolean parse(String filepath, int offset);

	public void resetPath();

	/**
	 * This method return true if the file path match with the wildcard path
	 * loaded into the WildcardPath object
	 *
	 * @param filepath
	 *            The file path string to compare
	 * @return java.lang.Boolean
	 */
	public Boolean match(String filepath);

	/**
	 * This method return true if the file path match with the wildcard path
	 * loaded into the WildcardPath object
	 *
	 * @param filepath
	 *            The file path string to compare
	 * @return java.lang.Boolean
	 * @see #match(int offset)
	 * @deprecated It will be replaced by match(int offset)
	 */
	// public Boolean match(String filepath, boolean withLanguage,
	// boolean withCountry);
	//	
	/**
	 * This method will looking for matchs
	 * 
	 * @param offset
	 * @return
	 */
	public Boolean match(String filepath, int offset);

	/**
	 * Permite reemplazar el locale en el wildcard path
	 *
	 * @param locale
	 * @return
	 */
	// public IWildcardPath replace(Locale locale);
	/**
	 * Permite reemplazar el wildcard pasado por el valor
	 * 
	 * @param wildcard
	 * @param value
	 * @param replace
	 * @return
	 */
	// public IWildcardPath replace(String wildcard, String value, boolean
	// replace);
	/**
	 * Permite reemplazar el wildcard pasado por el valor
	 * 
	 * @param wildcard
	 * @param value
	 * @param replace
	 * @return
	 */
	// public IWildcardPath replace(String wildcard, String value);
	/**
	 * Devuelve el path hasta previo al descubrimiento del ROOT
	 * 
	 * @return
	 */
	public String getPathToRoot();

	public String getPath(int offset);

	public String getFilePath(IFile ifile, Locale locale);

	public String toRegex(int offset);

	public String toRegex();

	public void setLanguage(String language);

	public void setCountry(String country);

	public void setFileExtension(String fileExtension);

	public void setFileName(String fileName);

	public void setRoot(String root);

	public Object clone() throws CloneNotSupportedException;
}