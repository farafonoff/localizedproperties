package com.triadsoft.properties.editor.plugin.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

import com.triadsoft.properties.editor.plugin.DefaultsPreferencesTest;
import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;
import com.triadsoft.properties.model.utils.IFilesDiscoverer;
import com.triadsoft.properties.model.utils.NewPathDiscovery;

public class PathDiscoveryNewImplTest extends LocalizedPropertiesTest {

	protected IFilesDiscoverer discoverer;
	private IFolder flexFolder;
	private IFolder joomlaFolder;
	private IFolder joomlaEsARFolder;
	private IFolder joomlaEnUSFolder;
	private IFolder flexEsArFolder;
	private IFolder flexEnUSFolder;

	protected IFolder javaFolder;
	protected IFolder webFolder;

	public void setUp() throws Exception {
		super.setUp();
		this.setUpJava();
		this.setUpFlex();
		this.setUpWeb();
		this.setUpJoomla();
	}

	private void setUpJava() {
		javaFolder = defaultProject.getFolder("src");
		this.createFolder(javaFolder);

		IFolder folder = defaultProject.getFolder("web");
		this.createFolder(folder);
		webFolder = folder.getFolder("WEB-INF");
		this.createFolder(webFolder);
	}

	private void setUpFlex() {
		flexFolder = defaultProject.getFolder("flexlocale");
		this.createFolder(flexFolder);
		flexEsArFolder = flexFolder.getFolder("es_AR");
		this.createFolder(flexEsArFolder);
		flexEnUSFolder = flexFolder.getFolder("en_US");
		this.createFolder(flexEnUSFolder);
	}

	private void setUpJoomla() {
		joomlaFolder = defaultProject.getFolder("joomla");
		this.createFolder(joomlaFolder);
		joomlaEsARFolder = joomlaFolder.getFolder("es-AR");
		joomlaEnUSFolder = joomlaFolder.getFolder("en-US");
		this.createFolder(joomlaEsARFolder);
		this.createFolder(joomlaEnUSFolder);
	}

	private void setUpWeb() {

	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testJavaFileImplementation() {
		IFile file = javaFolder.getFile("components.es_AR.properties");
		IFile file1 = javaFolder.getFile("components.en_US.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path java no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}

	@Test
	public void testJavaFileWithoutLanguageImplementation() {
		IFile file = javaFolder.getFile("components.AR.properties");
		IFile file1 = javaFolder.getFile("components.US.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path java no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}

	@Test
	public void testJavaFileWithoutCountryImplementation() {
		IFile file = javaFolder.getFile("components.es.properties");
		IFile file1 = javaFolder.getFile("components.en.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path java no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}

	@Test
	public void testFlexImplementation() {
		IFile file = flexEnUSFolder.getFile("component.properties");
		IFile file1 = flexEsArFolder.getFile("component.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path Flex no coincide",
				DefaultsPreferencesTest.FLEX_WILDCARD.equals(lll));
	}

	@Test
	public void testJoomlaImplementation() {
		IFile file = joomlaEnUSFolder.getFile(new Path("en-US.mod_login.ini"));
		IFile file1 = joomlaEsARFolder.getFile(new Path("es-AR.mod_login.ini"));
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path joomla no coincide",
				DefaultsPreferencesTest.JOOMLA_WILDCARD.equals(lll));
	}

	@Test
	public void testWebFileImplementation() {
		IFile file = webFolder.getFile("components.es_AR.properties");
		IFile file1 = webFolder.getFile("components.en_US.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file1);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path web no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}

	@Test
	public void testWebFileWithoutLanguageImplementation() {
		IFile file = webFolder.getFile("components.AR.properties");
		IFile file1 = webFolder.getFile("components.US.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file1);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path web no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}

	@Test
	public void testWebFileWithoutCountryImplementation() {
		IFile file = webFolder.getFile("components.es.properties");
		IFile file1 = webFolder.getFile("components.en.properties");
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file1);
		assertTrue("El wildcardpath no debería ser nulo", discoverer
				.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("El path web no coincide",
				DefaultsPreferencesTest.JAVA_DOT_WILDCARD.equals(lll));
	}
}
