package com.triadsoft.properties.model.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class FileVisitor implements IResourceVisitor {

	private List files;
	private WildcardPath wp;

	public FileVisitor(List files, String wildcardPath) {
		this.files = files;
		wp = new WildcardPath(wildcardPath);
	}

	public boolean visit(IResource resource) throws CoreException {
		Pattern p = Pattern.compile(wp.getWildcardpath());
		Matcher m = p.matcher(resource.getFullPath().toString());
		if (resource.getType() == IFile.FILE
				&& m.find()) {
			wp.parse(resource.getFullPath().toString());
			files.add(resource);
			return true;
		} else if (resource.getType() == IFile.FOLDER) {
			return true;
		}
		return false;
	}
}
