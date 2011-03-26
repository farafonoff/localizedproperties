package com.triadsoft.properties.editor.utils;

import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.junit.Test;

import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.WildCardPath2;
import com.triadsoft.properties.model.utils.WildcardPath;

/**
 * Prueba de la nueva version del WildCardPath Este test hace pruebas unitarias
 * sobre los wildcardpath Falta hacer una test de integracion para hacer las
 * pruebas cuando existe mas de uno
 * 
 * @author Leonardo Flores()
 * 
 */
public class WildcardPathTest extends TestCase {
	private static final String FLEX_PROPERTIES = "/{root}/{lang}_{country}/{filename}.{fileextension}";
	private static final String JAVA_PROPERTIES = "/{root}/{filename}.{lang}_{country}.{fileextension}";
	private static final String WEB_PROPERTIES = "/{root}/{filename}.{lang}_{country}.{fileextension}";
	private static final String JOOMLA_INI = "/{root}/{lang}-{country}/{lang}-{country}.{filename}.{fileextension}";
	private static final String JAVA_PROPERTIES_WITHOUT_ROOT = "{filename}.{lang}_{country}.{fileextension}";

	@Test
	public void testJava() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaConNumero() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"component2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaConNumeroUnix() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "/tempos/TemposProject/src/locale/prueba/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"componnt2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaDefault() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
	}

	@Test
	public void testJavaWithUnderscore() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba",
				"prueba".equals(wp.getRoot()));
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
		IWildcardPath wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo debe ser component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje deberia ser en", "en".equals(wp.getLanguage()));
		assertTrue("El country deberia ser null", wp.getCountry() == null);
	}

	@Test
	public void testJavaWithoutCountryAndLanguage() {
		IWildcardPath wp = new WildCardPath2(JAVA_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo debe ser component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje deberia ser null", wp.getLanguage() == null);
		assertTrue("El country deberia ser null", wp.getCountry() == null);
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

	@Test
	public void testJoomlaLanguageIniFiles() {
		WildcardPath wp = new WildcardPath(JOOMLA_INI);
		String filepath = "c:\\tempos\\test_ini_files\\language\\en-GB\\en-GB.com_content.ini";
		assertTrue("El wildcard no coincide", wp.match(filepath));
		wp.parse(filepath);
		assertTrue("El lenguaje tiene que ser 'en'",
				"en".equals(wp.getLanguage()));
		assertTrue("El pais tiene que ser 'GB'", "GB".equals(wp.getCountry()));
		assertTrue("El nombre del archivo no es com_content",
				"com_content".equals(wp.getFileName()));
		assertTrue("La extension del archivo no es ini",
				"ini".equals(wp.getFileExtension()));
		assertTrue("El root no es language", "language".equals(wp.getRoot()));
	}

	@Test
	public void testJoomlaLanguageIniFilesUnix() {
		WildcardPath wp = new WildcardPath(JOOMLA_INI);
		String filepath = "/tempos/test_ini_files/language/en-GB/en-GB.com_content.ini";
		assertTrue("El wildcard no coincide", wp.match(filepath));
		wp.parse(filepath);
		assertTrue("El lenguaje tiene que ser 'en'",
				"en".equals(wp.getLanguage()));
		assertTrue("El pais tiene que ser 'GB'", "GB".equals(wp.getCountry()));
		assertTrue("El nombre del archivo no es com_content",
				"com_content".equals(wp.getFileName()));
		assertTrue("La extension del archivo no es ini",
				"ini".equals(wp.getFileExtension()));
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
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		String filepath = "c:\\tempos\\TemposProject\\src\\locale\\en_US\\component.properties";
		assertTrue("Deberia coincidir con el properties de flex",
				wp.match(filepath));
		wp.parse(filepath);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es locale",
				"locale".equals(wp.getRoot()));
	}

	@Test
	public void testFlexUnixFile() {
		WildcardPath wp = new WildcardPath(FLEX_PROPERTIES);
		String filepath = "/tempos/TemposProject/src/locale/en_US/component.properties";
		assertTrue("Deberia coincidir con el properties de flex",
				wp.match(filepath));
		wp.parse(filepath);
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
	public void testWebInf() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF",
				"WEB-INF".equals(wp.getRoot()));
	}

	@Test
	public void testWebInfConNumero() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"component2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF",
				"WEB-INF".equals(wp.getRoot()));
	}

	@Test
	public void testWebInfConNumeroUnix() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "/tempos/TemposProject/src/web/WEB-INF/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"component2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF",
				"WEB-INF".equals(wp.getRoot()));
	}

	@Test
	public void testWebInfDefault() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root no es WEB-INF",
				"WEB-INF".equals(wp.getRoot()));
	}

	@Test
	public void testWebInfWithUnderscore() {
		WildCardPath2 wp = new WildCardPath2(WEB_PROPERTIES);
		String path = "c:\\tempos\\TemposProject\\src\\web\\WEB-INF\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es WEB-INF",
				"WEB-INF".equals(wp.getRoot()));
	}

	@Test
	public void testJavaWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaConNumeroWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"component2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaConNumeroUnixWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "/tempos/TemposProject/src/locale/prueba/component2.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));

		wp.parse(path);
		assertTrue("El nombre del archivo no es component2",
				"component2".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaDefaultWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es component",
				"component".equals(wp.getFileName()));
		assertTrue("No debe tener lenguaje", wp.getLanguage() == null);
		assertTrue("No debe tener pais", wp.getCountry() == null);
		assertTrue("La carpeta root debe ser null", wp.getRoot() == null);
	}

	@Test
	public void testJavaWithUnderscoreWithoutRoot() {
		WildCardPath2 wp = new WildCardPath2(JAVA_PROPERTIES_WITHOUT_ROOT);
		String path = "c:\\tempos\\TemposProject\\src\\locale\\prueba\\core_component.en_US.properties";
		assertTrue("El path debería coincidir", wp.match(path));
		wp.parse(path);
		assertTrue("El nombre del archivo no es core_component",
				"core_component".equals(wp.getFileName()));
		assertTrue("El lenguaje no es en", "en".equals(wp.getLanguage()));
		assertTrue("El pais no es US", "US".equals(wp.getCountry()));
		assertTrue("La carpeta root no es prueba", wp.getRoot() == null);
	}
}
