package com.triadsoft.properties.model.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.visitors.FileVisitor;

/**
 * Se encarga de de obtener a partir del archivo pasado en el constructor todos
 * los locales, junto a los archivos ifile de cada locale. Una vez que termina
 * se puede obtener el resultado en el mapa resources donde esta una entrada por
 * cada para locale, archivo,etc. B�sicamente lo que recibe es un coleccion de
 * wildcard path y recorre cada uno de ellos hasta descubrir el match con el
 * path name del archivo pasado como par�metro en el constructor.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see WildcardPath
 * @deprecated Se tiene que usar NewPathDiscovery
 */
public class PathDiscovery implements IFilesDiscoverer {
	private WildcardPath wp = null;
	final Map<Locale, IFile> resources = new HashMap<Locale, IFile>();
	private IPath path;
	private String filename;
	private Locale defaultLocale;
	private IFile file;

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#getPath()
	 */
	public IPath getPath() {
		return path;
	}

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#getFilename()
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * El constructor recibe como par�metro un archivo, el cual se usa para
	 * descubrir datos como directorio root, locale, nombre del archivo, etc
	 * 
	 * @param file
	 *            Archivo a analizar
	 */
	public PathDiscovery(IFile file) {
		this.file = file;
		wp = getWildcardPath(file);
		if (wp == null) {
			throw new RuntimeException(LocalizedPropertiesPlugin.getString(
					PATHDISCOVERY_UNKNOWN_WILDCARD_MESSAGE, new String[] { file
							.getFullPath().toString() }));
		}
		this.searchFiles();
	}

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#searchFiles()
	 */
	public void searchFiles() {
		wp.parse(file.getFullPath().toString());
		if (wp.getFileName() == null) {
			throw new RuntimeException(LocalizedPropertiesPlugin.getString(
					PATHDISCOVERY_UNKNOWN_FILENAME_MESSAGE, new String[] { file
							.getFullPath().toString() }));
		}
		filename = wp.getFileName();
		defaultLocale = wp.getLocale();
		if (defaultLocale == null) {
			defaultLocale = StringUtils.getKeyLocale();
		}
		wp.resetPath();
		if (wp.getPathToRoot() != null && wp.getRoot() != null) {
			path = new Path(wp.getPathToRoot() + "/" + wp.getRoot());
		} else if (wp.getRoot() != null) {
			path = new Path(wp.getRoot());
		} else {
			path = file.getFullPath().removeLastSegments(1);
		}
		if (file.getWorkspace().getRoot().exists(path)) {
			IResource resource = file.getWorkspace().getRoot().findMember(path);
			resources.clear();
			if (resource.getType() == IFile.FOLDER
					|| resource.getType() == IFile.PROJECT) {
				FileVisitor fv = new FileVisitor(resources, wp);
				try {
					((IContainer) resource).accept(fv);
				} catch (CoreException e) {
					LocalizedPropertiesLog.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Este metodo recorre el listado de Wildcard Paths buscando el que coincida
	 * con el ifile pasado como parametro
	 * 
	 * @param ifile
	 * @return Devuelve el objeto WilcardPath que coincide con el path del
	 *         archivo
	 */
	private WildcardPath getWildcardPath(IFile ifile) {
		String[] wildcardPaths = LocalizedPropertiesPlugin.getWildcardPaths();
		for (int i = 0; i < wildcardPaths.length; i++) {
			WildcardPath wildcardPath = new WildcardPath(wildcardPaths[i]);
			if (wildcardPath.match(ifile.getFullPath().toString())) {
				return wildcardPath;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#getResources()
	 */
	public Map<Locale, IFile> getResources() {
		return resources;
	}

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#getWildcardPath()
	 */
	public IWildcardPath getWildcardPath() {
		return wp;
	}

	/* (non-Javadoc)
	 * @see com.triadsoft.properties.model.utils.IFilesDiscoverer#getDefaultLocale()
	 */
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
