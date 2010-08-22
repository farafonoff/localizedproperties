package com.triadsoft.test;

import java.util.Locale;
import java.util.StringTokenizer;

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
	private static final String JOOMLA_INI = "/{root}/{lang}-{country}/{lang}-{country}.{filename}.{fileextension}";

	@Test
	public void testFlex() {
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\en_US\\component.properties");
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale",
				"locale".equals(wp.getRoot()));
	}

	@Test
	public void testFlexWithUnderscore() {
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\en_US\\core_component.properties");
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale",
				"locale".equals(wp.getRoot()));
	}

	@Test
	public void testJava() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties");
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaDefault() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties");
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaWithUnderscore() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties");
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testReplacement() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.replace(WildcardPath.ROOT_WILDCARD, "locale")
				.replace(WildcardPath.FILENAME_WILDCARD, "alert")
				.replace(WildcardPath.FILE_EXTENSION_WILDCARD, "properties");
		wp.replace(Locale.getDefault());
	}

	@Test
	public void testJavaWithoutCountry() {
		WildcardPath wp = new WildcardPath(JAVA_PROPERTIES);
		wp.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en.properties");
		assertTrue("No pude encontrar el nombre del archivo",
				wp.getFileName() != null);
		assertTrue("No pude encontrar el lenguaje", wp.getLanguage() != null);
	}

	@Test
	public void testJoomlaLanguageIniFiles() {
		WildcardPath wp = new WildcardPath(JOOMLA_INI);
		assertTrue(
				"El wildcard no coincide",
				wp.match("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini"));
		wp.parse("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini");
		assertTrue("No encontré el lenguage", wp.getLanguage() != null);
		assertTrue("No encontré el pais", wp.getCountry() != null);
		assertTrue("El lenguaje no es ingles", wp.getLanguage().equals("en"));
		assertTrue("El pais no es Gran Bretaña", wp.getCountry().equals("GB"));
		assertTrue("El nombre del archivo no es com_content", wp.getFileName()
				.equals("com_content"));
		assertTrue("La extension del archivo no es ini", wp.getFileExtension()
				.equals("ini"));
		assertTrue("El root no es language", wp.getRoot().equals("language"));

	}

	@Test
	public void testJoomlaJavaMatchFiles() {
		WildcardPath joowp = new WildcardPath(JOOMLA_INI);
		WildcardPath jawp = new WildcardPath(JAVA_PROPERTIES);
		assertTrue(
				"El wildcard no coincide",
				joowp.match("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini"));

		assertTrue(
				"El wildcard de java no debe coincidir",
				jawp.match("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini"));

	}

	@Test
	public void testFileTokenizer() {
		String text = "L/test_ini_files/language/en-GB/en-GB.com_frontpage.ini";
		StringTokenizer tokenizer = new StringTokenizer(text, "/");
		assertTrue("Encontre 5 elemnetos", tokenizer.countTokens() == 5);
		tokenizer = new StringTokenizer(text, "/.-_");
		System.out.println("encontre " + tokenizer.countTokens());
		assertTrue("Encontre 12 elementos", tokenizer.countTokens() == 12);
		
		while (tokenizer.hasMoreElements()) {
			System.out.println(tokenizer.nextElement());
		}
		
		
	}
}
