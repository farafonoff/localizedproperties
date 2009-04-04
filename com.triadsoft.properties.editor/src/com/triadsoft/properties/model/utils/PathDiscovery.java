package com.triadsoft.properties.model.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.triadsoft.properties.editor.Activator;

/**
 * Se encarga de de obtener a partir del archivo pasado en el constructor todos
 * los locales, junto a los archivos ifile de cada locale. Una vez que termina
 * se puede obtener el resultado en el mapa resources donde esta una entrada por
 * cada para locale, archivo
 * 
 * @author Leonardo Flores (flores.leonardo@triadsoft.com.ar)
 */
public class PathDiscovery {
	private WildcardPath wp = null;
	final Map<Locale, IFile> resources = new HashMap<Locale, IFile>();
	private IPath path;
	private String filename;
	private Locale defaultLocale;

	public IPath getPath() {
		return path;
	}

	public String getFilename() {
		return filename;
	}

	public PathDiscovery(IFile file) {
		wp = getWildcardPath(file);
		if (wp == null) {
			throw new RuntimeException(
					"No se encontró un wildcard path que coincida con "
							+ file.getFullPath().toString());
		}
		wp.parse(file.getFullPath().toString());
		filename = wp.getFileName();
		defaultLocale = wp.getLocale();
		wp.resetPath();
		path = new Path(wp.getPathToRoot() + "/" + wp.getRoot());
		if (file.getWorkspace().getRoot().exists(path)) {
			IResource resource = file.getWorkspace().getRoot().findMember(path);
			if (resource.getType() == IFile.FOLDER) {
				resources.clear();
				FileVisitor fv = new FileVisitor(resources, wp);
				try {
					((IFolder) resource).accept(fv);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Este metodo recorre el listado de Wildcard Paths buscando el que coincida
	 * con el ifile pasado como parametro
	 * 
	 * @param ifile
	 * @return
	 */
	private WildcardPath getWildcardPath(IFile ifile) {
		String[] wildcardPaths = Activator.getWildcardPaths();
		for (int i = 0; i < wildcardPaths.length; i++) {
			WildcardPath wildcardPath = new WildcardPath(wildcardPaths[i]);
			if (wildcardPath.match(ifile.getFullPath().toString())) {
				return wildcardPath;
			}
		}
		return null;
	}

	public Map<Locale, IFile> getResources() {
		return resources;
	}

	public WildcardPath getWildcardPath() {
		return wp;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
