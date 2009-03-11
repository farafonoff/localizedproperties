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

/**
 * Se encarga de de obtener a partir del archivo pasado en el constructor
 * todos los locales, junto a los archivos ifile de cada locale.
 * Una vez que termina se puede obtener el resultado en el mapa resources
 * donde esta una entrada por cada para locale, archivo
 * @author Leonardo Flores (flores.leonardo@triadsoft.com.ar)
 */
public class PathDiscovery {
	private WildcardPath wp = null;
	final Map<Locale, IFile> resources = new HashMap<Locale, IFile>();

	public PathDiscovery(IFile file) {
		wp = new WildcardPath(
				"/{root}/{lang}_{country}/{filename}.{fileextension}");
		if (wp.match(file.getFullPath().toString())) {
			wp.parse(file.getFullPath().toString());
			wp.resetPath();
			IPath path = new Path(wp.getPathToRoot() + "/" + wp.getRoot());
			System.out.println(path.toString());
			if (file.getWorkspace().getRoot().exists(path)) {
				IResource resource = file.getWorkspace().getRoot().findMember(
						path);
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
	}

	public Map<Locale, IFile> getResources() {
		return resources;
	}
}
