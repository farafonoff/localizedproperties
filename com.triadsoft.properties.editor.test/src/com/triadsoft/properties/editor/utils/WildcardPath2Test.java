package com.triadsoft.properties.editor.utils;

import com.triadsoft.properties.model.utils.WildCardPath2;

public class WildcardPath2Test extends WildcardPathTest {
	public void setUp()throws Exception{
		super.setUp();
	}
	
	public void tearDown()throws Exception{
		super.tearDown();
	}
	
	public void testGetFullPath() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		wp.setCountry("AR");
		wp.setFileExtension("properties");
		wp.setFileName("core_components");
		wp.setRoot("src");
		wp.setLanguage("es");
		String lll = wp.getPath();
		assertTrue("El path deber�a ser", "/src/core_components.es_AR.properties".equals(lll));
	}

	public void testGetPathWithoutCountry() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		//wp.setCountry("AR");
		wp.setFileExtension("properties");
		wp.setFileName("core_components");
		wp.setRoot("src");
		wp.setLanguage("es");
		String lll = wp.getPath();
		assertTrue("El path deber�a ser", "/src/core_components.es_[A-Z]{2}.properties".equals(lll));
	}
}
