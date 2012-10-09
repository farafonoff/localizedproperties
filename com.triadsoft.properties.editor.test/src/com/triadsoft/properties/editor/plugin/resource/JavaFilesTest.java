package com.triadsoft.properties.editor.plugin.resource;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;
import com.triadsoft.properties.model.ResourceList;

public class JavaFilesTest extends LocalizedPropertiesTest {

	protected IFolder javaFolder;
	protected IFolder webFolder;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		javaFolder = defaultProject.getFolder("src");
		this.createFolder(javaFolder);

		IFolder folder = defaultProject.getFolder("web");
		this.createFolder(folder);
		webFolder = folder.getFolder("WEB-INF");
		this.createFolder(webFolder);
	}

	public void testWebCountryLanguageFiles() {
		assertTrue("No encontré la carpeta java", webFolder != null);
		assertTrue("La carpeta java no es válida", webFolder.exists());

		IFile file = webFolder.getFile("components.es_AR.properties");
		IFile file1 = webFolder.getFile("components.en_US.properties");
		this.createFile(file);
		this.createContent(file);
		this.createFile(file1);
		this.createContent(file1);
		ResourceList list = new ResourceList(file);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser AR", locale.getCountry()
				.equals("AR"));
		assertTrue("El lenguaje del locale debe ser es", locale.getLanguage()
				.equals("es"));
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
		assertTrue("El nombre del archivo debería ser 'components'", list
				.getFileName() != null
				&& list.getFileName().equals("components"));

	}

	public void testJavaCountryLanguageWithUnderscoreFiles() {
		assertTrue("No encontré la carpeta java", webFolder != null);
		assertTrue("La carpeta java no es válida", webFolder.exists());

		IFile file = webFolder.getFile(new Path(
				"core_components.es_AR.properties"));
		IFile file1 = webFolder.getFile(new Path(
				"core_components.en_US.properties"));
		this.createFile(file);
		this.createContent(file);
		this.createFile(file1);
		this.createContent(file1);
		ResourceList list = new ResourceList(file);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser AR", locale.getCountry()
				.equals("AR"));
		assertTrue("El lenguaje del locale debe ser es", locale.getLanguage()
				.equals("es"));
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
		assertTrue("El nombre del archivo debería ser 'core_components'", list
				.getFileName() != null
				&& list.getFileName().equals("core_components"));
	}

	public void testJavaCountryLanguageWithUndescoreVariant() {
		assertTrue("No encontré la carpeta java", webFolder != null);
		assertTrue("La carpeta java no es válida", webFolder.exists());

		IFile file = webFolder.getFile(new Path(
				"core_components1_es_AR_WIN.properties"));
		IFile file1 = webFolder.getFile(new Path(
				"core_components1_en_US_WIN.properties"));
		IFile file2 = webFolder.getFile(new Path(
				"core_components1_en_US.properties"));

		this.createFile(file);
		this.createContent(file);
		this.createFile(file1);
		this.createContent(file1);
		this.createFile(file2);
		this.createContent(file2);
		ResourceList list = new ResourceList(file);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser AR", locale.getCountry()
				.equals("AR"));
		assertTrue("El lenguaje del locale debe ser es", locale.getLanguage()
				.equals("es"));

		Locale[] locales = list.getLocales();
		assertTrue("Se deberían haber encontrado tres locales", locales != null
				&& locales.length == 3);
		// Locale 1
		assertTrue("El pais del locale debe ser US", locales[0].getCountry()
				.equals("US"));
		assertTrue("El languaje del locale debe ser en", locales[0]
				.getLanguage().equals("en"));
		assertTrue("La variante del locale debe ser WIN", locales[0]
				.getVariant().equals("WIN"));
		// Locale 2
		assertTrue("El pais del locale debe ser AR", locales[1].getCountry()
				.equals("AR"));
		assertTrue("El languaje del locale debe ser es", locales[1]
				.getLanguage().equals("es"));
		assertTrue("La variante del locale debe ser WIN", locales[1]
				.getVariant().equals("WIN"));

		// Locale 3
		assertTrue("El pais del locale debe ser US", locales[2].getCountry()
				.equals("US"));
		assertTrue("El languaje del locale debe ser es", locales[2]
				.getLanguage().equals("en"));

		assertTrue("La variante del locale debe ser nula", locales[2]
				.getVariant() == null
				|| locales[2].getVariant().length() == 0);

		// ----------------
		assertTrue("El nombre del archivo debería ser 'core_components1'", list
				.getFileName() != null
				&& list.getFileName().equals("core_components1"));
	}
}
