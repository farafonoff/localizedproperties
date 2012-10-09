package com.triadsoft.properties.editor.plugin;

import junit.framework.TestCase;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

public class DefaultsPreferencesTest extends TestCase {

	public static final String FLEX_WILDCARD = "/{root}/{lang}_{country}/{filename}.{fileextension}";
	public static final String LANG_DOT_COUNTRY_WILDCARD = "/{root}/{lang}.{country}/{filename}.{fileextension}";
	public static final String JOOMLA_WILDCARD = "/{root}/{lang}-{country}/{lang}-{country}.{filename}.{fileextension}";
	public static final String LANG_SLASH_COUNTRY_FILENAME_WILDCARD = "/{root}/{lang}/{country}/{filename}.{fileextension}";
	public static final String JAVA_FULL = "/{root}/{filename}(_{lang})3(_{country})2(_{variant})1.{fileextension}";
	public static final String JAVA_DOUBLE_DOT_WILDCARD = "/{root}/{filename}(.{lang})2(.{country})1.{fileextension}";
	public static final String JAVA_DOT_WILDCARD = "/{root}/{filename}(.{lang})2(_{country})1.{fileextension}";
	public static final String ALEJANDRA_WILDCARD = "/{root}/{filename}(_{lang})2(_{country})1.{fileextension}";
	

	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testWildcardDefaults() {
		String[] wps = LocalizedPropertiesPlugin.getWildcardPaths();
		assertTrue("No se encontro el wp para flex",
				FLEX_WILDCARD
						.equals(wps[0]));
		assertTrue("No se encontro el wp para java con punto",
				JAVA_DOT_WILDCARD
						.equals(wps[1]));
		assertTrue("No se encontro el wp para java con doble punto",
				JAVA_DOUBLE_DOT_WILDCARD
						.equals(wps[2]));
		assertTrue("No se encontro el directorio lang.country",
				LANG_DOT_COUNTRY_WILDCARD
						.equals(wps[3]));
		assertTrue("No se encontro el wp para el directorio lang/country/filename.fileextension",
				LANG_SLASH_COUNTRY_FILENAME_WILDCARD
						.equals(wps[4]));
		assertTrue("No se encontro el wp para una variante de Java lang_country",
				JAVA_FULL
						.equals(wps[5]));
		assertTrue("No se encontro el wp para Joomla",
				JOOMLA_WILDCARD.equals(wps[6]));
		//It's a simplification of JAVA_DOT_WILDCARD
//		assertTrue("No se encontro el wp para filename.country.extension",
//				SIMPLE_WITH_COUNTRY_WILDCARD.equals(wps[7]));
	}
}
