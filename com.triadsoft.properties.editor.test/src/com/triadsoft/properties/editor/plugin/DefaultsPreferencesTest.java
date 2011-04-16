package com.triadsoft.properties.editor.plugin;

import junit.framework.TestCase;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

public class DefaultsPreferencesTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testWildcardDefaults() {
		String[] wps = LocalizedPropertiesPlugin.getWildcardPaths();
		assertTrue("No se encontro el wp para flex",
				"/{root}/{lang}_{country}/{filename}.{fileextension}"
						.equals(wps[0]));
		assertTrue("No se encontro el wp para java completo",
				"/{root}/{filename}(.{lang})2(_{country})1.{fileextension}"
						.equals(wps[1]));
		assertTrue("No se encontro el wp para java con punto",
				"/{root}/{filename}(.{lang})2(.{country})1.{fileextension}"
						.equals(wps[2]));
		assertTrue("No se encontro el directorio lang.country",
				"/{root}/{lang}.{country}/{filename}.{fileextension}"
						.equals(wps[3]));
		assertTrue("No se encontro el wp para el directorio lang/country/filename.fileextension",
				"/{root}/{lang}/{country}/{filename}.{fileextension}"
						.equals(wps[4]));
		assertTrue("No se encontro el wp para una variante de Java lang_country",
				"/{root}/{filename}(_{lang})2(_{country})1.{fileextension}"
						.equals(wps[5]));
		assertTrue("No se encontro el wp para Joomla",
				"/{root}/{lang}-{country}/{lang}-{country}.{filename}.{fileextension}".equals(wps[6]));
		assertTrue("No se encontro el wp para filename.country.extension",
				"/{root}/{filename}.{country}.{fileextension}".equals(wps[7]));
	}
}
