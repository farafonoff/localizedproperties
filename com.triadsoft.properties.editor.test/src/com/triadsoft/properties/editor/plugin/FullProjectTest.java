package com.triadsoft.properties.editor.plugin;

import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;
import com.triadsoft.properties.model.ResourceList;
import com.triadsoft.properties.model.utils.StringUtils;
import com.triadsoft.properties.utils.LocalizedPropertiesLog;

public class FullProjectTest extends LocalizedPropertiesTest {

	protected IProject alexandra_test;
	protected IFolder alexandra_folder;
	protected IProject grails_test;
	protected IProject leandro_test;
	protected IProject ss3_web;
	private IFile alexandra_file;
	private IFile alexandra_file1;
	private IFolder grails_folder;
	private IFile grails_file;
	private IFile grails_file1;
	private IFolder ss3_folder;
	private IFile ss3_file;
	private IFile ss3_file1;
	private IFile ss3_file2;
	private IFolder leandro_folder;
	private IFile leandro_file1;
	private IFile leandro_file;
	private IFile leandro_file2;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.setUpAlexandra();
		this.setUpGrails();
		this.setUpLeandro();
		this.setUpSS3();
	}

	private void setUpSS3() {
		ss3_web = workspace.getRoot().getProject("ss3_web");
		createProject(ss3_web);
		ss3_folder = ss3_web.getFolder("/src/");
		createFolder(ss3_folder);
		ss3_file = ss3_folder.getFile("components.properties");
		ss3_file1 = ss3_folder.getFile("components.es_AR.properties");
		ss3_file2 = ss3_folder.getFile("components.en_US.properties");
		createFile(ss3_file);
		createFile(ss3_file1);
		createFile(ss3_file2);
	}

	protected void setUpAlexandra() {
		alexandra_test = workspace.getRoot().getProject("alexandra_test");
		createProject(alexandra_test);
		alexandra_folder = alexandra_test.getFolder("/src/");
		createFolder(alexandra_folder);
		alexandra_file = alexandra_folder.getFile("totranslate_en.properties");
		alexandra_file1 = alexandra_folder.getFile("totranslate_fr.properties");
		createFile(alexandra_file);
		createFile(alexandra_file1);
	}

	protected void setUpGrails() {
		grails_test = workspace.getRoot().getProject("grails_test");
		createProject(grails_test);
		grails_folder = grails_test.getFolder("/i18n/");
		createFolder(grails_folder);
		grails_file = grails_folder.getFile("GrailsFile_en_US.properties");
		grails_file1 = grails_folder.getFile("GrailsFile_es_AR.properties");
		createFile(grails_file);
		createFile(grails_file1);
	}

	protected void setUpLeandro() {
		leandro_test = workspace.getRoot().getProject("leandro_test");
		createProject(leandro_test);
		leandro_folder = leandro_test.getFolder("/i18n/");
		createFolder(leandro_folder);
		leandro_file = leandro_folder.getFile("teste.properties");
		leandro_file1 = leandro_folder.getFile("teste_en.properties");
		leandro_file2 = leandro_folder.getFile("teste_pt.properties");
		createFile(leandro_file);
		createFile(leandro_file1);
		createFile(leandro_file2);
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAlexandraJavaEnglishFile() {
		ResourceList list = new ResourceList(alexandra_file);
		Locale locale = list.getDefaultLocale();
		assertTrue("El languaje default deberia ser 'en'", "en".equals(locale
				.getLanguage()));
		LocalizedPropertiesLog.debug("'" + locale.getCountry() + "'");
		assertTrue("El country default deberia ser 'vacio' y fue '"
				+ locale.getCountry() + "'", locale.getCountry().equals(""));
		assertTrue("El filename debería ser totranslate", "totranslate"
				.equals(list.getFileName()));
		assertTrue("Se deberian haber obtenido dos locales",
				list.getLocales().length == 2);
		assertTrue("Se deberia haber obtenido un locale 'fr'", list
				.getPropertyFile(new Locale("fr")) != null);
		assertTrue("Se deberia haber obtenido un locale 'en'", list
				.getPropertyFile(new Locale("en")) != null);
	}

	public void testAlexandraJavaFrenchFile() {
		ResourceList list = new ResourceList(alexandra_file1);
		Locale locale = list.getDefaultLocale();
		assertTrue("El languaje default deberia ser 'fr'", "fr".equals(locale
				.getLanguage()));
		LocalizedPropertiesLog.debug("'" + locale.getCountry() + "'");
		assertTrue("El country default deberia ser 'vacio' y fue '"
				+ locale.getCountry() + "'", locale.getCountry().equals(""));
		assertTrue("El filename debería ser totranslate", "totranslate"
				.equals(list.getFileName()));
		assertTrue("Se deberian haber obtenido dos locales",
				list.getLocales().length == 2);
		assertTrue("Se deberia haber obtenido un locale 'fr'", list
				.getPropertyFile(new Locale("fr")) != null);
		assertTrue("Se deberia haber obtenido un locale 'en'", list
				.getPropertyFile(new Locale("en")) != null);
	}

	public void testGrailsJavaDefaultFiles() {
		ResourceList list = new ResourceList(grails_file);
		Locale locale = list.getDefaultLocale();
		assertTrue("El languaje default deberia ser 'en'", "en".equals(locale
				.getLanguage()));
		assertTrue("El country default deberia ser 'US'", locale.getCountry()
				.equals("US"));
		assertTrue("El filename debería ser GrailsFile", "GrailsFile"
				.equals(list.getFileName()));
		assertTrue("Se deberian haber obtenido dos locales",
				list.getLocales().length == 2);
		assertTrue("Se deberia haber obtenido un locale 'en'", list
				.getPropertyFile(new Locale("en", "US")) != null);
		assertTrue("Se deberia haber obtenido un locale 'es'", list
				.getPropertyFile(new Locale("es", "AR")) != null);
	}

	public void testLeandroJavaDefaultFiles() {
		ResourceList list = new ResourceList(leandro_file);
		Locale locale = list.getDefaultLocale();
		assertTrue("El languaje default deberia ser 'xx'", StringUtils
				.getKeyLocale().getLanguage().equals(locale.getLanguage()));
		assertTrue("El country default deberia ser 'XX'", locale.getCountry()
				.equals(StringUtils.getKeyLocale().getCountry()));
		assertTrue("El filename debería ser GrailsFile", "teste".equals(list
				.getFileName()));
		assertTrue("Se deberian haber obtenido 3 locales y se obtuvieron "
				+ list.getLocales().length, list.getLocales().length == 3);
		assertTrue("Se deberia haber obtenido un locale 'en'", list
				.getPropertyFile(new Locale("en")) != null);
		assertTrue("Se deberia haber obtenido un locale 'es'", list
				.getPropertyFile(new Locale("pt")) != null);
		assertTrue("Se deberia haber obtenido un locale default", list
				.getPropertyFile(StringUtils.getKeyLocale()) != null);

	}

	public void testLeandroJavaEnglishFiles() {
		ResourceList list = new ResourceList(leandro_file1);
		Locale locale = list.getDefaultLocale();
		assertTrue("El languaje default deberia ser 'en'", "en".equals(locale
				.getLanguage()));
		assertTrue("El country default deberia ser vacio y fue '"
				+ locale.getCountry() + "'", locale.getCountry().equals(""));
		assertTrue("El filename debería ser teste", "teste".equals(list
				.getFileName()));
		assertTrue("Se deberian haber obtenido 3 locales pero hubo "
				+ list.getLocales().length, list.getLocales().length == 3);
		assertFalse("No deberia existir un locale con pais",
				list.getLocales()[0].getCountry() == null);
		assertTrue("Se deberia haber obtenido un locale 'en'", list
				.getPropertyFile(new Locale("en")) != null);
		assertTrue("Se deberia haber obtenido un locale 'es'", list
				.getPropertyFile(new Locale("pt")) != null);
		assertTrue("Se deberia haber obtenido un locale default", list
				.getPropertyFile(StringUtils.getKeyLocale()) != null);

	}
}
