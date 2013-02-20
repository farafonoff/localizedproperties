package com.triadsoft.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import com.triadsoft.properties.model.PropertiesFile;

public class NewPropertiesTest extends TestCase {
	private static String PATH = "./src/com/triadsoft/properties/";
	protected InputStream simpleProperties;
	protected InputStream doubleDotProperties;
	protected InputStream ÒufloProperties;

	/** */
	protected InputStream languageProperties;

	@Override
	protected void setUp() throws Exception {
		simpleProperties = new FileInputStream(PATH + "components.properties");
		doubleDotProperties = new FileInputStream(PATH
				+ "components_double_dot.properties");
		ÒufloProperties = new FileInputStream(PATH
				+ "components_Òuflo.properties");
		languageProperties = new FileInputStream(PATH + "language.properties");
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testSimpleProperties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			props.load(simpleProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 64);
	}

	@Test
	public void testDoubleDotProperties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			props.load(doubleDotProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 64);
	}

	@Test
	public void test—ufloProperties() {
		assertTrue("No se pudo cargar el archivo", ÒufloProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			props.load(ÒufloProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 64);
	}

	@Test
	public void testLanguageProperties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			props.load(languageProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 678);
	}

	@Test
	public void testLanguage_es_AR_Properties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		File es_AR = new File(PATH + "language.es_AR.properties");
		props = new PropertiesFile(es_AR);
		
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 678);
		String name = props.getProperty("label.form.recoveryPassword");
		System.out.println(name);
		props.setProperty("mi.nueva.clave", "…s es un prueba");
		try {
			props.save();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLanguage_en_US_Properties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			InputStream en_US = new FileInputStream(PATH
					+ "language.en_US.properties");
			props.load(en_US);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 678);
	}

	@Test
	public void testLanguage_short_Properties() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			InputStream en_US = new FileInputStream(PATH
					+ "language_short.properties");
			props.load(en_US);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 678);
	}

	@Test
	public void test_en_GB_com_bannersIni() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			InputStream en_GB = new FileInputStream(PATH
					+ "en-GB.com_banners.ini");
			props.load(en_GB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 37);
	}

	@Test
	public void test_en_GB_com_contactIni() {
		assertTrue("No se pudo cargar el archivo", simpleProperties != null);
		PropertiesFile props = new PropertiesFile();
		try {
			InputStream en_GB = new FileInputStream(PATH
					+ "en-GB.com_contact.ini");
			props.load(en_GB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.keySet().size());
		assertTrue("No se encontraron todas las claves esperadas", props
				.keySet().size() == 92);
	}
}
