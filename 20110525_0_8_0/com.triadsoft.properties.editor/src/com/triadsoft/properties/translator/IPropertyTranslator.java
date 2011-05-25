package com.triadsoft.properties.translator;

import java.util.Locale;

/**
 * Interfase que modela un servicio que se encargar&aacute; 
 * de traducir los textos encontrados en el editor.
 * @author Leonardo Flores (flores.leonardo@gmail.com)
 */
public interface IPropertyTranslator {
	/**
	 * Devuelve un nombre descriptivo para el servicio que será mostrado
	 * en el listado de servicios diponibles
	 * @return
	 */
	public String getName();
	
	/**
	 * Devuelve una descripcion para el servicio y será mostrado en mas info
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Devuelve un link la pagina de informacion sobre el servicio
	 * @return
	 */
	public String getInfoURL();
	
	/**
	 * M&eacute;todo que debe testear si el servicio está activo
	 * y devolver su estado. No se programar&aacute; nada con la url devuelta 
	 * porque se considera que la url no es la direcci&oacute;n del servicio, 
	 * sino que es un direcci&oacute;n de informacion 
	 * @return
	 */
	public Boolean isAlive();
	
	/**
	 * M&eacute;todo encargado de realizar la traduccion
	 * @param text
	 * @param origin Locale con el el lenguaje y el pais de origen. Se debe tener en cuenta que el locale enviado puede no tener el pais, y se podr&iacute;a usar solo el lenguaje
	 * @param destination Locale con el el lenguaje y el pais de destino. Se debe tener en cuenta que el locale enviado puede no tener el pais, y se podr&iacute;a usar solo el lenguaje
	 * @return
	 */
	public String translate(String text,Locale origin,Locale destination);
}