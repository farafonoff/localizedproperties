package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Clase Visitor que se encarga de obtener los archivos que cumplen con el
 * nombre y la ubicacion del recurso consultado en el metodo visit
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class FileVisitor implements IResourceVisitor {

	private Map<Locale, IFile> locales;
	private WildcardPath wp;
	private WildcardPath comparator;

	public FileVisitor(Map<Locale, IFile> locales, WildcardPath wp) {
		this.locales = locales;
		this.wp = wp;
		this.comparator = new WildcardPath(wp.getWildcardpath());
	}

	/**
	 * Metodo que revisa el nombre y la ubicacion del recurso
	 * 
	 * @param resource
	 *            Recurso que ser� evaluado
	 * @return True si el archivo puede ser evaluado o FALSE en caso que no se
	 *         deba revisar. El unico caso en que se devuelve false es cuando el
	 *         recurso es un archivo y no cumple con el nombre esperado.En caso
	 *         que sea una carpeta devuelve true para que puede evaluar los
	 *         archivos internos.
	 */
	public boolean visit(IResource resource) throws CoreException {
		// Pattern p = Pattern.compile(wp.getLocaleRegex());
		// Matcher m = p.matcher(resource.getFullPath().toString());

		if (resource.getType() == IFile.FOLDER) {
			return true;
		} else if (resource.getType() == IFile.PROJECT) {
			return true;
		} else if (resource.getType() == IFile.FILE) {
			IFile file = (IFile) resource;
			comparator.parse(file.getFullPath().toString());
			boolean isFilename = comparator.getFileName().equals(
					wp.getFileName());
			boolean isFileExtension = comparator.getFileExtension().equals(
					wp.getFileExtension());

			if (isFilename && isFileExtension) {
				comparator.resetPath();
				if (comparator.parse(resource.getFullPath().toString())) {
					if (comparator.getLocale() == null) {
						locales.put(new Locale("xx", "XX"), (IFile) resource);
					} else {
						locales.put(comparator.getLocale(), (IFile) resource);
					}
					return true;
				}
			}
			return false;
		}
		return false;
	}
}
