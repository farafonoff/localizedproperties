package com.triadsoft.properties.editor.ui.test;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTest;

public class PathDiscoveryTest extends LocalizedPropertiesTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	public void testJavaFiles() {
		assertTrue("El proyecto no está creado", project != null);
		IPath path = new Path("tests");
		// IContainer container = IWorkspaceRoot.getContainerForLocation(path);
		IFolder folder = project.getFolder(path);
		if (!folder.exists()) {
			try {
				folder.create(false, false, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		assertTrue("No encontré el container", folder != null);
		assertTrue("El container no es una carpeta", folder.exists());
	}
}
