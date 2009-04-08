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
 * cada para locale, archivo,etc. Básicamente lo que recibe es un coleccion de
 * wildcard path y recorre cada uno de ellos hasta descubrir el match con el
 * path name del archivo pasado como parámetro en el constructor.
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * @see WildcardPath
 */
public class PathDiscovery {
	private WildcardPath wp = null;
	final Map<Locale, IFile> resources = new HashMap<Locale, IFile>();
	private IPath path;
	private String filename;
	private Locale defaultLocale;

	/**
	 * Devuelve el path ya parseado como el path desde el proyecto hasta el
	 * directorio que contiene al archivo de recursos
	 * 
	 * @return
	 */
	public IPath getPath() {
		return path;
	}

	/**
	 * Devuelve el nombre del archivo sin extension.
	 * 
	 * @return String Nombre del srchivo
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * El constructor recibe como parámetro un archivo, el cual se usa para
	 * descubrir datos como directorio root, locale, nombre del archivo, etc
	 * 
	 * @param file
	 *            Archivo a analizar
	 */
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

	/**
	 * Devuelve un mapa con los recursos encontrados dentro del path,
	 * organizados por su locale
	 * 
	 * @return Map<Locale,IFile>
	 */
	public Map<Locale, IFile> getResources() {
		return resources;
	}

	/**
	 * Devuelve el objeto WildcardPath que se uso para descubir los datos de la
	 * ubicacion de los recursos
	 * 
	 * @return
	 */
	public WildcardPath getWildcardPath() {
		return wp;
	}

	/**
	 * Devuelve el locale contenido en el archivo pasado en el constructor
	 * 
	 * @return Locale del archivo abierto
	 */
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
