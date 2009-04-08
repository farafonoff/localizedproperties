package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class FileVisitor implements IResourceVisitor {

	private Map<Locale, IFile> locales;
	private WildcardPath wp;

	public FileVisitor(Map<Locale, IFile> locales, WildcardPath wp) {
		this.locales = locales;
		this.wp = wp;
		System.out.println("Default Locale: " + wp.getLocale());
	}

	public boolean visit(IResource resource) throws CoreException {
		Pattern p = Pattern.compile(wp.getLocaleRegex());
		Matcher m = p.matcher(resource.getFullPath().toString());
		if (resource.getType() == IFile.FOLDER) {
			return true;
		} else if (resource.getType() == IFile.FILE && m.find()) {
			if (wp.parse(resource.getFullPath().toString())) {
				locales.put(wp.getLocale(), (IFile) resource);
				return true;
			}
			return false;
		}
		return false;
	}
}
