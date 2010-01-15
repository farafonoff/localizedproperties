package com.triadsoft.test;

import junit.framework.TestCase;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

public class PathDiscoveryTest extends TestCase {

	private IWorkspace workspace;
	private IWorkspaceRoot workspaceRoot;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		IPath path = new Path("c:/workspace_java/"); 
		if(path.isEmpty()){
			ResourcesPlugin.getPlugin().getLog().log(new Status(Status.ERROR,"Lalala","Mensaje"));
		}
		workspace = ResourcesPlugin.getWorkspace();
		workspaceRoot = workspace.getRoot();
	}

	public void testJavaFiles() {
		IPath path = new Path("tests");
		//IContainer container = IWorkspaceRoot.getContainerForLocation(path);
		IContainer container = workspaceRoot.getContainerForLocation(path);
		assertTrue("No encontré el container", container != null);
		assertTrue("El container no es una carpeta", container.exists());
	}
}
