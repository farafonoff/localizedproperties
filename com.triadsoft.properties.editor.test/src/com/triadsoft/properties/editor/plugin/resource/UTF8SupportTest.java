package com.triadsoft.properties.editor.plugin.resource;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;

/**
 * This test allow to review UTF-8 character encoding as Korean, 
 * China or Japanesse support
 * @author Triad (flores.leonardo@gmail.com)
 *
 */
public class UTF8SupportTest extends LocalizedPropertiesTest {
	
	protected IFolder utfFolder;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		workspace = ResourcesPlugin.getWorkspace();
		defaultProject = workspace.getRoot().getProject("utf8_test");
		this.createProject(defaultProject);
		System.out.println(workspace.getRoot().getLocation().toFile().toURI());
		if(!defaultProject.exists()){
			System.out.println("no pude encontrar el proyecto");
		}
		utfFolder = defaultProject.getFolder("src");
		this.createFolder(utfFolder);
	}
	
	public void testProjectExistence(){
		assertNotNull(utfFolder);
		assertTrue("No existe la carpeta src",utfFolder.exists());
	}
}
