package com.triadsoft.properties.editor.plugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Path;
import org.junit.Test;

import com.triadsoft.properties.model.utils.IFilesDiscoverer;
import com.triadsoft.properties.model.utils.NewPathDiscovery;

public class PathDiscoveryNewImplTest extends JavaFilesTest {
	protected IFilesDiscoverer discoverer;
	private IFolder flexFolder;
	private IFolder joomlaFolder;
	private IFolder joomlaEsARFolder;
	private IFolder joomlaEnUSFolder;
	private IFolder flexEsArFolder;
	private IFolder flexEnUSFolder;

	public void setUp() throws Exception {
		super.setUp();
		this.setUpJava();
		this.setUpFlex();
		this.setUpWeb();
		this.setUpJoomla();
	}
	
	private void setUpJava(){
		
	}

	private void setUpFlex(){
		flexFolder = project.getFolder("flexlocale");
		this.createFolder(flexFolder);
		flexEsArFolder =  flexFolder.getFolder("es_AR");
		this.createFolder(flexEsArFolder);
		flexEnUSFolder =  flexFolder.getFolder("en_US");
		this.createFolder(flexEnUSFolder);
	}
	
	private void setUpJoomla(){
		joomlaFolder = project.getFolder("joomla");
		this.createFolder(joomlaFolder);
		joomlaEsARFolder = joomlaFolder.getFolder("es-AR");
		joomlaEnUSFolder = joomlaFolder.getFolder("en-US");
		this.createFolder(joomlaEsARFolder);
		this.createFolder(joomlaEnUSFolder);
	}
	
	private void setUpWeb(){
		
	}
	

	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testJavaFileImplementation() {
		IFile file = webFolder.getFile(new Path("components.es_AR.properties"));
		IFile file1 = webFolder
				.getFile(new Path("components.en_US.properties"));
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo",
				discoverer.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("/{root}/WEB-INF/{filename}.{lang}_{country}.{fileextension}"
				.equals(lll));
	}

	@Test
	public void testFlexImplementation() {
		IFile file = flexEnUSFolder.getFile(new Path("component.properties"));
		IFile file1 = flexEsArFolder.getFile(new Path("component.properties"));
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo",
				discoverer.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("/{root}/{lang}_{country}/{filename}.{fileextension}"
				.equals(lll));
	}

	@Test
	public void testJoomlaImplementation() {
		IFile file = joomlaEnUSFolder.getFile(new Path("en-US.mod_login.ini"));
		IFile file1 = joomlaEsARFolder.getFile(new Path("es-AR.mod_login.ini"));
		this.createFile(file);
		this.createFile(file1);
		discoverer = new NewPathDiscovery(file);
		assertTrue("El wildcardpath no debería ser nulo",
				discoverer.getWildcardPath() != null);
		String lll = discoverer.getWildcardPath().toString();
		assertTrue("/{root}/{lang}-{country}/{lang}-{country}.{filename}.{fileextension}"
				.equals(lll));
	}
}
