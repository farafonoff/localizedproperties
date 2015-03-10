package com.triadsoft.properties.editor.plugin.resource;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;
import com.triadsoft.properties.model.ResourceList;

public class FlexFilesTest extends LocalizedPropertiesTest {
	private IFolder flexFolder;
	private IFolder esArFolder;
	private IFolder enUSFolder;

	public void setUp() throws Exception {
		super.setUp();
		flexFolder = defaultProject.getFolder("locale");
		this.createFolder(flexFolder);
		esArFolder = defaultProject.getFolder("locale\\es_AR");
		enUSFolder = defaultProject.getFolder("locale\\en_US");
		this.createFolder(esArFolder);
		this.createFolder(enUSFolder);
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFlexCountryLanguage() {
		IFile enUSFile = enUSFolder.getFile("component.properties");
		IFile esARFile = esArFolder.getFile("component.properties");
		this.createFile(enUSFile);
		this.createFile(esARFile);
		ResourceList list = new ResourceList(enUSFile);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser 'US'", locale.getCountry()
				.equals("US"));
		assertTrue("El lenguaje del locale debe ser 'en'", locale.getLanguage()
				.equals("en"));
		Locale[] locales = list.getLocales();
		assertTrue("Se deberían haber encontrado dos locales", locales != null
				&& locales.length == 2);
		assertTrue("El pais del locale debe ser US", locales[0].getCountry()
				.equals("US"));
		assertTrue("El languaje del locale debe ser en", locales[0]
				.getLanguage().equals("en"));
		assertTrue("El pais del locale debe ser AR", locales[1].getCountry()
				.equals("AR"));
		assertTrue("El languaje del locale debe ser es", locales[1]
				.getLanguage().equals("es"));
		assertTrue("El nombre del archivo no deberia ser null",
				list.getFileName() != null);
		assertTrue("El nombre del archivo debería ser 'component'", list
				.getFileName().equals("component"));
	}

	public void testFlexFileWithUnderScore() {
		IFile enUSFile = enUSFolder.getFile("core_component.properties");
		IFile esARFile = esArFolder.getFile("core_component.properties");
		this.createFile(enUSFile);
		this.createFile(esARFile);
		ResourceList list = new ResourceList(enUSFile);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser 'US'", locale.getCountry()
				.equals("US"));
		assertTrue("El lenguaje del locale debe ser 'en'", locale.getLanguage()
				.equals("en"));
		Locale[] locales = list.getLocales();
		assertTrue("Se deberían haber encontrado dos locales", locales != null
				&& locales.length == 2);
		assertTrue("El pais del locale debe ser US", locales[0].getCountry()
				.equals("US"));
		assertTrue("El languaje del locale debe ser en", locales[0]
				.getLanguage().equals("en"));
		assertTrue("El pais del locale debe ser AR", locales[1].getCountry()
				.equals("AR"));
		assertTrue("El languaje del locale debe ser es", locales[1]
				.getLanguage().equals("es"));
		assertTrue("El nombre del archivo no debe ser null",
				list.getFileName() != null);
		assertTrue("El nombre del archivo debería ser 'core_component'", list
				.getFileName().equals("core_component"));
	}
}
