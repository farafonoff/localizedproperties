package com.triadsoft.properties.model.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.visitors.FileDiscoverer;

/**
 * Nueva Implementacion del path discovery Se busca mejorar la lógica de
 * busqueda por una forma mas inteligente.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see WildcardPath2
 */
public class NewPathDiscovery implements IFilesDiscoverer {

	protected IFile file;
	protected IWildcardPath wp;
	protected String filename;
	protected Locale defaultLocale;
	protected Map<Locale, IFile> resources;
	private int offset;

	public NewPathDiscovery(IFile file) {
		this.file = file;
		wp = this.findWildcardPath();
		if (wp == null) {
			return;
		}
		filename = wp.getFileName();
		defaultLocale = wp.getLocale();
		this.searchFiles();
	}

	public int getOffset() {
		return offset;
	}

	/**
	 * El metodo tiene que buscar los wildcard path que cumplan con el patron
	 * tratando de encontrar uno solo, aplicando cierta inteligencia para
	 * encontrarlos
	 * 
	 * @return
	 */
	protected IWildcardPath findWildcardPath() {
		if (file == null) {
			return null;
		}
		offset = 0;
		// Ahora tengo que buscar los wp que cumplan con el archivo
		// Primero busco con el match completo
		IWildcardPath[] wps = this.getWPS(offset);
		while (wps.length == 0 && offset < 5) {
			offset++;
			wps = this.getWPS(offset);
		}
		if (wps.length == 0) {
			throw new RuntimeException(LocalizedPropertiesPlugin.getString(
					PATHDISCOVERY_UNKNOWN_WILDCARD_MESSAGE, new String[] { file
							.getFullPath().toString() }));
		}

		// Si encontré un solo wp entonces lo devuelvo
		if (wps.length == 1) {
			return wps[0];
		}
		// Si recibí mas de uno de los wp,entonces tengo que buscar para
		// discernir
		// cual de ellos es, y una manera es probando si los archivos que trae
		// cada uno
		// coinciden con los datos obtenidos (filename,fileextension,root)
		int max = 0;
		int maxIndex = -1;
		for (int i = 0; i < wps.length; i++) {
			// Voy a probar cual de ellos trae la mayor cantidad de resultados
			IFile[] files = getFilesFromWp(wps[i]);
			if (files.length > max) {
				max = files.length;
				maxIndex = i;
			}
		}
		if (maxIndex > -1) {
			return wps[maxIndex];
		}
		return null;
	}

	protected IWildcardPath[] getWPS(int offset) {
		List<WildCardPath2> wpd = new LinkedList<WildCardPath2>();
		String[] wildcardPaths = LocalizedPropertiesPlugin.getWildcardPaths();
		// Primero busco los wp que cumplan completamente con el
		for (int i = 0; i < wildcardPaths.length; i++) {
			WildCardPath2 wildcardPath = new WildCardPath2(wildcardPaths[i]);
			String filepath = file.getFullPath().toString();
			if (wildcardPath.match(filepath, offset)
					&& wildcardPath.parse(filepath, offset)) {
				wpd.add(wildcardPath);
			}
		}
		return wpd.toArray(new WildCardPath2[wpd.size()]);
	}

	protected IFile[] getFilesFromWp(IWildcardPath wp) {
		final List<IFile> files = new LinkedList<IFile>();
		IProject project = file.getProject();
		String filename = wp.getFileName();
		String extension = wp.getFileExtension();
		String root = wp.getRoot();
		WildCardPath2 wp2;
		try {
			wp2 = (WildCardPath2) ((WildCardPath2) wp).clone();
			FileDiscoverer fd = new FileDiscoverer(files, wp2, filename,
					extension, root, offset);
			files.clear();
			project.accept(fd);
		} catch (CloneNotSupportedException e1) {
			LocalizedPropertiesLog.error("No se pudo clonar el wildcardpath",
					e1);
		} catch (CoreException e) {
			LocalizedPropertiesLog
					.error("Ocurrio un error buscando los archivos que coincidan con el wildcardpath",
							e);
		}
		return files.toArray(new IFile[files.size()]);
	}

	public IPath getPath() {
		return null;
	}

	public String getFilename() {
		return filename;
	}

	public void searchFiles() {
		if (wp == null) {
			return;
		}
		IFile[] files = getFilesFromWp(wp);
		if (resources == null) {
			resources = new HashMap<Locale, IFile>();
		}
		WildCardPath2 wp2 = new WildCardPath2(wp.getWildcardpath());
		for (int i = 0; i < files.length; i++) {
			wp2.parse(files[i].getFullPath().toFile().getAbsolutePath(), offset);
			Locale locale = null;
			if (wp2.getLanguage() == null && wp2.getCountry() != null) {
				locale = new Locale(wp2.getLanguage(), wp2.getCountry());
			} else if (wp2.getLanguage() != null && wp2.getCountry() == null) {
				locale = new Locale(wp2.getLanguage());
				// Situacion extraña porque no tiene sentido poner un pais sin
				// lenguaje
				// pero por las dudas esta bien soportarla
			} else if (wp2.getLanguage() == null && wp2.getCountry() == null) {
				locale = new Locale("xx", wp2.getCountry());
			}
			if (locale != null) {
				resources.put(locale, files[i]);
			}
		}
	}

	public Map<Locale, IFile> getResources() {
		return resources;
	}

	public IWildcardPath getWildcardPath() {
		return wp;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}
}
