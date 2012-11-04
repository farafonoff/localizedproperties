package com.triadsoft.properties.model;

import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.properties.model.utils.IWildcardPath;

public class ResourceChangeDeltaVisitor implements IResourceDeltaVisitor {
	private Map<Locale, IFile> changed;
	private IWildcardPath wp;
	private String filename;
	private String fileextension;
	private Map<Locale, IFile> added;
	private Map<Locale, IFile> removed;

	public ResourceChangeDeltaVisitor(Map<Locale, IFile> added,
			Map<Locale, IFile> removed, Map<Locale, IFile> changed,
			IWildcardPath path, String filename, String fileextension) {
		this.added = added;
		this.removed = removed;
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
		if ((delta.getKind() == IResourceDelta.ADDED
				|| delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.CHANGED)
				&& resource.getType() == IResource.FILE && wp.match(filepath)) {
			int index = 0;
			while (!wp.parse(filepath, index)
					&& index < IWildcardPath.MAXIMUM_OPTIONALS) {
				index++;
			}
			if (wp.getFileName() != null && wp.getFileName().equals(filename)
					&& wp.getFileExtension() != null
					&& wp.getFileExtension().equals(fileextension)) {
				if (delta.getKind() == IResourceDelta.ADDED) {
					added.put(wp.getLocale(), (IFile) resource);
				} else if (delta.getKind() == IResourceDelta.REMOVED) {
					removed.put(wp.getLocale(), (IFile) resource);
				} else if (!resource.isDerived()) {
					changed.put(wp.getLocale(), (IFile) resource);
				}
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
