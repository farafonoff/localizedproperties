package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Clase Visitor que se encarga de obtener los archivos que cumplen con el
 * nombre y la ubicacion del recurso consultado en el metodo visit
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class FileVisitor implements IResourceVisitor {

	private Map<Locale, IFile> locales;
	private WildcardPath wp;

	public FileVisitor(Map<Locale, IFile> locales, WildcardPath wp) {
		this.locales = locales;
		this.wp = wp;
	}

	/**
	 * Metodo que revisa el nombre y la ubicacion del recurso
	 * 
	 * @param resource
	 *            Recurso que será evaluado
	 * @return True si el archivo puede ser evaluado o FALSE en caso que no se
	 *         deba revisar. El unico caso en que se devuelve false es cuando el recurso es
	 *         un archivo y no cumple con el nombre esperado.En caso que sea una
	 *         carpeta devuelve true para que puede evaluar los archivos
	 *         internos.
	 */
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
