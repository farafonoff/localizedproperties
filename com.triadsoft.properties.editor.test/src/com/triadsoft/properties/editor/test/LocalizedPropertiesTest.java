package com.triadsoft.properties.editor.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class LocalizedPropertiesTest extends TestCase {
	protected IWorkspace workspace;
	protected IProject project;

	public void setUp() throws Exception {
		workspace = ResourcesPlugin.getWorkspace();
		project = workspace.getRoot().getProject("My Project");
		IWorkspaceRunnable operation = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				int fileCount = 10;
				project.create(null);
				project.open(null);
				for (int i = 0; i < fileCount; i++) {
					IFile file = project.getFile("File" + i);
					file.create(null, IResource.NONE, null);
				}
			}
		};
		workspace.run(operation, null);
		super.setUp();
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}
