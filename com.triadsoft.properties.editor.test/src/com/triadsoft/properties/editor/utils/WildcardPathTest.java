package com.triadsoft.properties.editor.utils;

import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.junit.Test;

import com.triadsoft.properties.editor.plugin.DefaultsPreferencesTest;
import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.WildCardPath2;
import com.triadsoft.properties.utils.LocalizedPropertiesLog;

/**
 * Prueba de la nueva version del WildCardPath Este test hace pruebas unitarias
 * sobre los wildcardpath Falta hacer una test de integracion para hacer las
 * pruebas cuando existe mas de uno
 * 
 * @author Leonardo Flores()
 * 
 */
public class WildcardPathTest extends TestCase {
	protected static final String WEB_PROPERTIES = "/{root}/{filename}(.{lang})2(_{country})1.{fileextension}";
	protected static final String JAVA_PROPERTIES_WITHOUT_ROOT = "{filename}(.{lang})2(_{country})1.{fileextension}";

	@Test
	public void testJava() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaConNumero() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaConNumeroUnix() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "/tempos/TemposProject/src/locale/prueba/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaDefault() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path, 2));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	@Test
	public void testJavaWithUnderscore() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
	}

	// @Test
	// public void testReplacement() {
	// WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
	// wp.replace(IWildcardPath.ROOT_WILDCARD, "locale")
	// .replace(IWildcardPath.FILENAME_WILDCARD, "alert")
	// .replace(IWildcardPath.FILE_EXTENSION_WILDCARD, "properties");
	// wp.replace(Locale.getDefault());
	// }

	@Test
	public void testJavaWithoutCountry() {
		IWildcardPath wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en.properties";
		assertTrue("El path debería coincidir", wp.match(path, 1));
		wp.parse(path);
		assertTrue("El nombre del archivo debe ser component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje deberia ser en", "en".equals(wp.getLanguage()));
		assertTrue("El country deberia ser null", wp.getCountry() == null);
	}

	@Test
	public void testJavaWithoutCountryAndLanguage() {
		IWildcardPath wp = new WildCardPath2(
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path, 2));
		wp.parse(path);
		assertTrue("El nombre del archivo debe ser component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje deberia ser null", wp.getLanguage() == null);
		assertTrue("El country deberia ser null", wp.getCountry() == null);
	}

	@Test
	public void testFileTokenizer() {
		String text = "L/test_ini_files/language/en-GB/en-GB.com_frontpage.ini";
		StringTokenizer tokenizer = new StringTokenizer(text, "/");
		assertTrue("Encontre 5 elementos", tokenizer.countTokens() == 5);
		tokenizer = new StringTokenizer(text, "/.-_");
		//LocalizedPropertiesLog.debug("encontre " + tokenizer.countTokens());
		System.out.println("encontre " + tokenizer.countTokens());
		assertTrue("Encontre 12 elementos", tokenizer.countTokens() == 12);

		while (tokenizer.hasMoreElements()) {
			//LocalizedPropertiesLog.debug("" + tokenizer.nextElement());
			System.out.println("" + tokenizer.nextElement());
		}
	}

	@Test
	public void testJoomlaLanguageIniFiles() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JOOMLA_WILDCARD);
		String filepath = "c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini";
		assertTrue("El wildcard no coincide", wp.match(filepath));
		wp.parse(filepath);
		assertTrue("El lenguaje tiene que ser 'en'", "en".equals(wp
				.getLanguage()));
		assertTrue("El pais tiene que ser 'GB'", "GB".equals(wp.getCountry()));
		assertTrue("El nombre del archivo no es com_content", "com_content"
				.equals(wp.getFileName()));
		assertTrue("La extension del archivo no es ini", "ini".equals(wp
				.getFileExtension()));
		assertTrue("El root no es language", "language".equals(wp.getRoot()));
	}

	@Test
	public void testJoomlaLanguageIniFilesUnix() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.JOOMLA_WILDCARD);
		String filepath = "/tempos/test_ini_files/language/en-GB/en-GB.com_content.ini";
		assertTrue("El wildcard no coincide", wp.match(filepath));
		wp.parse(filepath);
		assertTrue("El lenguaje tiene que ser 'en'", "en".equals(wp
				.getLanguage()));
		assertTrue("El pais tiene que ser 'GB'", "GB".equals(wp.getCountry()));
		assertTrue("El nombre del archivo no es com_content", "com_content"
				.equals(wp.getFileName()));
		assertTrue("La extension del archivo no es ini", "ini".equals(wp
				.getFileExtension()));
		assertTrue("El root no es language", "language".equals(wp.getRoot()));
	}

	// @Test
	// public void testJoomlaJavaMatchFiles() {
	// IWildcardPath joowp = new WildcardPath(JOOMLA_INI);
	// IWildcardPath jawp = new WildcardPath(JAVA_PROPERTIES);
	// assertTrue(
	// "El wildcard no coincide",
	// joowp.match("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini"));
	//
	// assertTrue(
	// "El wildcard de java no debe coincidir",
	// jawp.match("c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini"));
	//
	// }

	@Test
	public void testFlex() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.FLEX_WILDCARD);
		String filepath = "c:\\tempos\\TemposProject\\src\\locale\\en_US\\component.properties";
		assertTrue("Deberia coincidir con el properties de flex", wp
				.match(filepath));
		wp.parse(filepath);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale", "locale"
				.equals(wp.getRoot()));
	}

	@Test
	public void testFlexUnixFile() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.FLEX_WILDCARD);
		String filepath = "/tempos/TemposProject/src/locale/en_US/component.properties";
		assertTrue("Deberia coincidir con el properties de flex", wp
				.match(filepath));
		wp.parse(filepath);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale", "locale"
				.equals(wp.getRoot()));
	}

	@Test
	public void testFlexWithUnderscore() {
		WildCardPath2 wp = new WildCardPath2(
				DefaultsPreferencesTest.FLEX_WILDCARD);
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
	public void testWebInf() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF", "WEB-INF".equals(wp
				.getRoot()));
	}

	@Test
	public void testWebInfConNumero() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF", "WEB-INF".equals(wp
				.getRoot()));
	}

	@Test
	public void testWebInfConNumeroUnix() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "/tempos/TemposProject/src/web/WEB-INF/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF", "WEB-INF".equals(wp
				.getRoot()));
	}

	@Test
	public void testWebInfDefault() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path, 2));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es WEB-INF", "WEB-INF".equals(wp
				.getRoot()));
	}

	@Test
	public void testWebInfWithUnderscore() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF", "WEB-INF".equals(wp
				.getRoot()));
	}

	@Test
	public void testJavaWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaConNumeroWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaConNumeroUnixWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "/tempos/TemposProject/src/locale/prueba/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2", "component2"
				.equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaDefaultWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path, 2));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component", "component"
				.equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaWithUnderscoreWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", wp.getRoot() == null);
	}

	@Test
	public void testJavaWithVariant() {
		WildCardPath2 wp = new WildCardPath2(DefaultsPreferencesTest.JAVA_FULL);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component_en_US_XX.properties";
		assertTrue("El path debería coincidir", wp.match(path, 0));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("Deberia encontrar la carpeta prueba como root", wp
				.getRoot() != null);
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
		assertTrue("No se encontro el variant", wp.getVariant() != null);
		assertTrue("El variant no es XX", "XX".equals(wp.getVariant()));

	}

	@Test
	public void testJavaWithVariantWithout() {
		WildCardPath2 wp = new WildCardPath2(DefaultsPreferencesTest.JAVA_FULL);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component_en_US.properties";
		assertTrue("El path no debería coincidir", !wp.match(path, 0));
		assertTrue("El path debería coincidir", wp.match(path, 1));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("Deberia encontrar la carpeta prueba como root", wp
				.getRoot() != null);
		assertTrue("La carpeta root no es prueba", "prueba"
				.equals(wp.getRoot()));
		assertTrue("Se encontro el variant cuando no debería",
				wp.getVariant() == null);
	}
}
