package com.triadsoft.properties;

import java.io.File;

import junit.framework.TestCase;

import com.triadsoft.common.properties.PropertyCategory;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.common.properties.PropertyFile1;

public class PropertiesParserTest extends TestCase {

	protected static final String KEYWORD_REGEX = "[\\w.]*=";
	protected static final String COMMENT_REGEX = "#[\\w.\\sáéíóú$#%&()][^\\=\\$]+[\\n$]";

	protected static final String COMPONENT_TEST_PROPERTY = "component.test";
	private PropertyFile pf;
	private PropertyFile1 properties;

	public void setUp() throws Exception {
		super.setUp();
		String contents = "#Default Category\n";
		File file = new File(
				"C://localizedproperties_trunk/com.triadsoft.properties.editor.test/src/com/triadsoft/properties/components.properties");

		pf = new PropertyFile(file, "UTF-8", new String[] { "=" });
		properties = new PropertyFile1(getClass().getResourceAsStream(
				"components.properties"));
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFileLoading() {
		assertTrue("No se encontraros hijos, y debería haber al menos uno", pf
				.getChildren().length > 0);
		assertTrue("El encodign debería ser UTF-8", pf.getEncoding().equals(
				"UTF-8"));
		assertTrue("Debería haber al menos una categoría como default", pf
				.getDefaultCategory() != null);
		assertTrue("Debería haber al menos una propiedad",
				pf.getEntries() != null);
		assertTrue("Debería haber al menos una propiedad",
				pf.getEntries().length > 0);
		assertTrue("Se debería haber encontrado 7 categorias y se encontraron "
				+ pf.getChildren().length, pf.getChildren().length == 7);
	}

	public void testFileContent() {
		PropertyCategory cat = null;
		for (int i = 0; i < pf.getChildren().length; i++) {
			PropertyCategory category = (PropertyCategory) pf.getChildren()[i];
			if (category.getName().startsWith("Otro comentario")) {
				cat = category;
				break;
			}
		}

		assertTrue("No se encontro la categoría esperada", cat != null);
		String name = cat.getName();
		assertTrue(
				"No se encontró el comentario completo",
				name
						.equals("Otro comentario para probar\nun comentario multilinea"));

		assertTrue("No existe la propiedad buscada " + COMPONENT_TEST_PROPERTY,
				pf.getPropertyEntry(COMPONENT_TEST_PROPERTY) != null);
		assertTrue("La propiedad " + COMPONENT_TEST_PROPERTY
				+ " no tiene valor", pf.getPropertyEntry(
				COMPONENT_TEST_PROPERTY).getValue() != null);
		String text = pf.getPropertyEntry(COMPONENT_TEST_PROPERTY).getValue();
		assertTrue("El texto no comienza de la manera esperada", text
				.startsWith("Version 0.8.0"));
		// assertTrue("No se encontro el texto esperado", text
		// .indexOf("and refilling of") > -1);
	}

	public void testPropertiesContent() {
		String text = properties.getProperty(COMPONENT_TEST_PROPERTY);
		assertTrue("El texto no comienza de la manera esperada", text
				.startsWith("Version 0.8.0"));
		assertTrue("No se encontro el texto esperado", text
				.indexOf("and refilling of") > -1);
	}

	public void testNewFileLoading() {
		assertTrue("No se encontraros hijos, y debería haber al menos uno",
				properties.getCategories().size() > 0);
		// assertTrue("El encodign debería ser UTF-8", pf.getEncoding().equals(
		// "UTF-8"));
		assertTrue("Debería haber al menos una categoría como default",
				properties.getDefaultCategory() != null);
		// assertTrue("Debería haber al menos una propiedad",
		// properties.getEntries() != null);
		// assertTrue("Debería haber al menos una propiedad",
		// pf.getEntries().length > 0);
		// assertTrue("Se debería haber encontrado 7 categorias y se encontraron "
		// + pf.getChildren().length, pf.getChildren().length == 7);
	}

	public void testPropertiesRich() {

		// PropertiesRich rich = new PropertiesRich(getClass()
		// .getResourceAsStream("components.properties"));
		// assertTrue("Debería haber al menos una propiedad",
		// rich.keySet().size() > 0);
		// String text = rich.getProperty(COMPONENT_TEST_PROPERTY);
		// assertTrue("El texto no comienza de la manera esperada", text
		// .startsWith("Version 0.8.0"));
		// assertTrue("No se encontro el texto esperado", text
		// .indexOf("and refilling of") > -1);
	}
}
