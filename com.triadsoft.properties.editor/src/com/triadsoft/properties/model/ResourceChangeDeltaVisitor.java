package com.triadsoft.properties.model;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.properties.model.utils.IWildcardPath;

public class ResourceChangeDeltaVisitor implements IResourceDeltaVisitor {
	private Map<Integer, IFile> changed;
	private IWildcardPath wp;
	private String filename;
	private String fileextension;

	public ResourceChangeDeltaVisitor(Map<Integer, IFile> changed,
			IWildcardPath path, String filename, String fileextension) {
		this.changed = changed;
		this.wp = path;
		this.filename = filename;
		this.fileextension = fileextension;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource.isDerived()) {
			return false;
		}

		if (resource.getType() == IResource.FOLDER) {
			return true;
		}
		String filepath = resource.getFullPath().toString();
		if ((delta.getKind() == IResourceDelta.ADDED || delta.getKind() == IResourceDelta.REMOVED) 
				&& resource.getType() == IResource.FILE
				&& (wp.match(filepath, 0) || wp.match(filepath, 1)
						|| wp.match(filepath, 2) || wp.match(filepath, 3) || wp
						.match(filepath, 4))) {
			int index = 0;
			while (!wp.parse(filepath, index) && index < 5) {
				index++;
			}
			if (wp.getFileName() != null && wp.getFileName().equals(filename)
					&& wp.getFileExtension() != null
					&& wp.getFileExtension().equals(fileextension)) {
				changed.put(delta.getKind(), (IFile) resource);
				return true;
			}
			return false;
		}

		// only interested in content changes
		if ((delta.getFlags() & IResourceDelta.CONTENT) == 0
				&& !resource.isDerived()) {
			return true;
		}

		if (delta.getKind() == IResourceDelta.CHANGED) {
			return false;
		}

		// // only interested in files with the "properties" extension
		// if (resource.getType() == IResource.FILE
		// && "properties".equalsIgnoreCase(resource.getFileExtension())) {
		// changed.add((IFile) resource);
		// }
		return true;
	}
}
