package com.triadsoft.test;

import java.util.Locale;

import org.junit.Test;

import com.triadsoft.properties.model.utils.WildcardPath;

import junit.framework.TestCase;

/**
 * Prueba
 * 
 * @author Leonardo Flores()
 * 
 */
public class WildcardPathTest extends TestCase {
	private static final String FLEX_PROPERTIES = "/{root}/{lang}_{country}/{filename}.{fileextension}";
	private static final String JAVA_PROPERTIES = "/{root}/{filename}.{lang}_{country}.{fileextension}";

	@Test
	public void testFlex() {
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\en_US\\component.properties");
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale", "locale"
				.equals(wp.getRoot()));
	}

	@Test
	public void testFlexWithUnderscore() {
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\en_US\\core_component.properties");
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale", "locale"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJava() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties");
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaDefault() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties");
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaWithUnderscore() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties");
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testReplacement() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.replace(WildcardPath.ROOT_WILDCARD, "locale").replace(
				WildcardPath.FILENAME_WILDCARD, "alert").replace(
				WildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		wp.replace(Locale.getDefault());
	}

	@Test
	public void testJavaWithoutCountry() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp
				.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en.properties");
		assertTrue("No pude encontrar el nombre del archivo",
				wp.getFileName() != null);
		assertTrue("No pude encontrar el lenguaje", wp.getLanguage() != null);
	}
}
