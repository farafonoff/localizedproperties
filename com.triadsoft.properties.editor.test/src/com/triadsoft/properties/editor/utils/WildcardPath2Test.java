package com.triadsoft.properties.editor.utils;

import com.triadsoft.properties.editor.plugin.DefaultsPreferencesTest;
import com.triadsoft.properties.model.utils.WildCardPath2;

public class WildcardPath2Test extends WildcardPathTest {
	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetFullPath() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		wp.setCountry("AR");
		wp.setFileExtension("properties");
		wp.setFileName("core_components");
		wp.setRoot("src");
		wp.setLanguage("es");
		String lll = wp.getPath();
		assertTrue("El path debería ser",
				"/src/core_components.es_AR.properties".equals(lll));
	}

	public void testGetPathWithoutCountry() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		// wp.setCountry("AR");
		wp.setFileExtension("properties");
		wp.setFileName("core_components");
		wp.setRoot("src");
		wp.setLanguage("es");
		String lll = wp.getPath();
		assertTrue("El path debería ser",
				"/src/core_components.es_[A-Z]{2}.properties".equals(lll));
	}
}
