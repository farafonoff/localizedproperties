package com.triadsoft.properties.editor.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class LocalizedPropertiesTest extends TestCase {
	protected IWorkspace workspace;
	protected IProject defaultProject;

	public void setUp() throws Exception {
		super.setUp();
		workspace = ResourcesPlugin.getWorkspace();
		defaultProject = workspace.getRoot().getProject("My Project");
		this.createProject(defaultProject);
	}

	protected void createProject(final IProject project) {
		if (!project.exists()) {
			IWorkspaceRunnable operation = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					project.create(null);
					project.open(null);
				}
			};
			try {
				workspace.run(operation, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	protected void createFile(IFile file) {
		if (!file.exists()) {
			String contents = "#Default Category\n";
			InputStream stream = new ByteArrayInputStream(contents.getBytes());
			try {
				file.create(stream, true, null);
			} catch (CoreException e) {
				assertTrue("No se pudo crear el archivo", false);
				e.printStackTrace();
			}
		}
	}

	protected void createFolder(IFolder folder) {
		if (!folder.exists()) {
			try {
				folder.create(false, true, null);
			} catch (CoreException e) {
				assertTrue("No se pudo crear la carpeta", false);
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
}
