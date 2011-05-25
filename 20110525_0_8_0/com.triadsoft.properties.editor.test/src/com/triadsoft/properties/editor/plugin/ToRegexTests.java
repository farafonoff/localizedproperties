package com.triadsoft.properties.editor.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.triadsoft.properties.editor.utils.WildcardPathTest;
import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.WildCardPath2;

public class ToRegexTests extends WildcardPathTest {
	private IWildcardPath wp;

	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testJavaFileFileExtensionRegex() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/components.en_US.properties";
		assertTrue("La expresion debería tener coincidencia", wp.match(path, 0));
		wp.setFileExtension("properties");
		wp.setFileName("components");
		wp.setRoot(null);
		wp.setLanguage(null);
		wp.setCountry(null);
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/[a-zA-Z\\-\\_][^/]+/components\\.[a-z]{2}\\_[A-Z]{2}\\.properties"
						.equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileFileExtensionRootRegex() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/components.en_US.properties";
		wp.setFileExtension("properties");
		wp.setFileName("components");
		wp.setRoot("prueba");
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/prueba/components\\.[a-z]{2}\\_[A-Z]{2}\\.properties"
						.equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileFileExtensionRootLangRegex() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/components.en_US.properties";
		wp.setFileExtension("properties");
		wp.setFileName("components");
		wp.setRoot("prueba");
		wp.setLanguage("en");
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/prueba/components\\.en\\_[A-Z]{2}\\.properties".equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileFileExtensionRootCountryRegex() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/components.en_US.properties";
		wp.setFileExtension("properties");
		wp.setFileName("components");
		wp.setRoot("prueba");
		wp.setCountry("US");
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/prueba/components\\.[a-z]{2}\\_US\\.properties".equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileFileExtensionRootLocaleRegex() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/components.en_US.properties";
		wp.setFileExtension("properties");
		wp.setFileName("components");
		wp.setRoot("prueba");
		wp.setLanguage("en");
		wp.setCountry("US");
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/prueba/components\\.en\\_US\\.properties".equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileUnderscoreNameRegexMatch() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/core_components.en_US.properties";
		wp.setFileExtension("properties");
		wp.setFileName("core_components");
		wp.setRoot("prueba");
		wp.setLanguage("en");
		wp.setCountry("US");
		String regex = wp.toRegex();
		assertTrue("La expresiones regulares no coinciden",
				"/prueba/core_components\\.en\\_US\\.properties".equals(regex));
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		assertTrue("La expresion regular debería matchear con el path",
				m.find());
	}

	public void testJavaFileUnderscoreNameLookingUp() {
		wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:/tempos/TemposProject/src/locale/prueba/core_components.en_US.properties";
		// wp.setFileExtension("properties");
		// wp.setFileName("core_components");
		// wp.setRoot("prueba");
		// wp.setLanguage("en");
		// wp.setCountry("US");
		assertTrue("La expresion debería coincidir", wp.match(path, 0));
		assertTrue("El nombre debería ser core_components","core_components".equals(wp.getFileName()));
		assertTrue("El root debería ser prueba","prueba".equals(wp.getRoot()));
		assertTrue("La extension debería ser properties","properties".equals(wp.getFileExtension()));
		assertTrue("El lenguaje debería ser en","en".equals(wp.getLanguage()));
		assertTrue("El pais debería ser US","US".equals(wp.getCountry()));
	}
}