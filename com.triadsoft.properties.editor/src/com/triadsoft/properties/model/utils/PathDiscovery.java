package com.triadsoft.properties.model.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * 
 * @author Leonardo Flores (flores.leonardo@triadsoft.com.ar)
 */
public class PathDiscovery {
	private WildcardPath wp = null;

	public PathDiscovery(IFile file) {
		wp = new WildcardPath(
				"/{root}/{lang}_{country}/{filename}.{fileextension}");
		if (wp.match(file.getFullPath().toString())) {
			wp.parse(file.getFullPath().toString());
			wp.resetPath();
			System.out.println(wp.replaceDiscoveryLocale().getPath());
			System.out.println(wp.getPathToRoot());
			IPath path = new Path(wp.getPathToRoot() + "/" + wp.getRoot());
			System.out.println(path.toString());
			if (file.getWorkspace().getRoot().exists(path)) {
				IResource resource = file.getWorkspace().getRoot().findMember(path);
				if (resource.getType() == IFile.FOLDER) {
					final List<IResource> resources = new LinkedList<IResource>();
					FileVisitor fv = new FileVisitor(resources, wp
							.getPath());
					try {
						((IFolder) resource).accept(fv);
						for (Iterator iterator = resources.iterator(); iterator
								.hasNext();) {
							IResource resource2 = (IResource) iterator.next();
							System.out.println(resource2.getFullPath().toString());
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
