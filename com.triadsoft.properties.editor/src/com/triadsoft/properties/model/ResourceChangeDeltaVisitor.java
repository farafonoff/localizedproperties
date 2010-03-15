package com.triadsoft.properties.model;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.properties.model.utils.WildcardPath;

public class ResourceChangeDeltaVisitor implements IResourceDeltaVisitor {
	private Map<Integer, IFile> changed;
	private WildcardPath wp;

	public ResourceChangeDeltaVisitor(Map<Integer, IFile> changed,
			WildcardPath path) {
		this.changed = changed;
		this.wp = path;
		this.wp.replace(WildcardPath.FILENAME_WILDCARD, wp.getFileName());
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource.isDerived()) {
			return false;
		}

		if (resource.getType() == IResource.FOLDER) {
			return true;
		}

		if (delta.getKind() == IResourceDelta.ADDED
				&& resource.getType() == IResource.FILE
				&& wp.match(resource.getFullPath().toString())) {
			changed.put(IResourceDelta.ADDED, (IFile) resource);
			return true;
		}

		if (delta.getKind() == IResourceDelta.REMOVED
				&& resource.getType() == IResource.FILE
				&& wp.match(resource.getFullPath().toString())) {
			changed.put(IResourceDelta.REMOVED, (IFile) resource);
			return true;
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
