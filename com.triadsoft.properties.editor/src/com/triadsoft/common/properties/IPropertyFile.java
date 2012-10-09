package com.triadsoft.common.properties;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

public interface IPropertyFile {

	public PropertyElement[] getChildren();

	/**
	 * Devuelve true, si existe una categoria con este nombre
	 * 
	 * @param categoryName
	 * @return Booleano TRUE o FALSE
	 */
	public boolean existCategory(String categoryName);

	/**
	 * Devuelve verdadero si existe la propiedad en el archivo
	 * 
	 * @param propertyName
	 * @return Booleano TRUE o FALSE
	 */
	public boolean exist(String propertyName);

	/**
	 * Convierte el PropertyFile en texto
	 * 
	 * @return un String con todo el contenido del archivo de propiedades
	 */
	public String asText();

	/**
	 * Devuelve una categoria a partir de su nombre
	 * 
	 * @param categoryName
	 *            Nombre de la categoria buscada
	 * @return PropertyCategory encontrada, null si no la encuentra
	 */
	public PropertyCategory getCategoryByName(String categoryName);

	/**
	 * Devuelve la categoria a partir del key buscado
	 * 
	 * @param entryKey
	 * @return null si no existe la entrada
	 */
	public PropertyCategory getCategoryFromEntry(String entryKey);

	/**
	 * Devuelve el objeto PropertyEntry a partir de la clave
	 * 
	 * @param entryKey
	 *            String que identifica a la entrada
	 * @return ProperyEntry
	 */
	public PropertyEntry getPropertyEntry(String entryKey);

	/**
	 * @see com.triadsoft.common.properties.PropertyElement#getLine()
	 */
	public int getLine();

	/**
	 * Devuelve las propiedades contenidas en el archivo
	 * 
	 * @return
	 */
	public PropertyEntry[] getEntries();

	/**
	 * Devuelve un array de string con todas las clave contenidas en el archivo
	 * 
	 * @return
	 */
	public String[] getKeys();

	/**
	 * Persiste los cambios hechos en el archivo
	 * 
	 * @throws IOException
	 * @throws CoreException
	 */
	public void save() throws IOException, CoreException;

}
