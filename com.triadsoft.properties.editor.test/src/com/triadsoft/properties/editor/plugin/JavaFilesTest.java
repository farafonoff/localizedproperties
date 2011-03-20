package com.triadsoft.properties.editor.plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;
import com.triadsoft.properties.model.ResourceList;

public class JavaFilesTest extends LocalizedPropertiesTest {

	private IFolder javaFolder;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		IPath path = new Path("javaFiles");
		javaFolder = project.getFolder(path);
		if (!javaFolder.exists()) {
			try {
				javaFolder.create(false, false, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	protected void createContent(IFile file) {
		if (!file.exists()) {
			assertTrue("El archivo " + file.getName() + "no existe", false);
		}
		String content = "clave.contenido1";
		InputStream stream = new ByteArrayInputStream(content.getBytes());
		try {
			file.appendContents(stream, true, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
			assertTrue("No pude crear el contenido", false);
		}
	}

	public void testJavaCountryLocaleFiles() {
		assertTrue("No encontré la carpeta java", javaFolder != null);
		assertTrue("La carpeta java no es válida", javaFolder.exists());

		IFile file = javaFolder
				.getFile(new Path("components.es_AR.properties"));
		IFile file1 = javaFolder
				.getFile(new Path("components.en_US.properties"));
		this.createFile(file);
		this.createContent(file);
		this.createFile(file1);
		this.createContent(file1);
		ResourceList list = new ResourceList(file);
		assertTrue("Debe haber un locale por default",
				list.getDefaultLocale() != null);
		Locale locale = list.getDefaultLocale();
		assertTrue("El pais del locale debe ser AR",locale.getCountry().equals("AR"));
		assertTrue("El lenguaje del locale debe ser es",locale.getLanguage().equals("es"));
		Locale[] locales = list.getLocales();
		assertTrue("Se deberían haber encontrado dos locales",locales != null && locales.length==2);
		assertTrue("El pais del locale debe ser US",locales[0].getCountry().equals("US"));
		assertTrue("El languaje del locale debe ser en",locales[0].getLanguage().equals("en"));
		assertTrue("El pais del locale debe ser AR",locales[1].getCountry().equals("AR"));
		assertTrue("El languaje del locale debe ser es",locales[1].getLanguage().equals("es"));
	}
}
