package com.triadsoft.properties.model.visitors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.LocalizedPropertiesLog;
import com.triadsoft.properties.model.utils.WildCardPath2;

/**
 * Este file visitor, intenta descubrir los archivos que tienen el mismo nombre
 * y extension que el del wildcard path. De ésta manera se descubren archivos
 * candidatos a matchear con el wp
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class FileDiscoverer implements IResourceVisitor {
	private IWildcardPath wp;
	private String filename;
	private String extension;
	private String root;
	private int offset;

	private List<IFile> files;

	public FileDiscoverer(List<IFile> files, WildCardPath2 wp, String filename,
			String extension, String root, int offset) {
		this.files = files;
		this.root = root;
		this.wp = wp;
		this.filename = filename;
		this.extension = extension;
		this.offset = offset;
	}

	public boolean visit(IResource resource) throws CoreException {
		String filepath = resource.getFullPath().toFile().getAbsolutePath();
		if (resource.getType() == IResource.FOLDER) {
			return true;
		} else if (resource.getType() == IResource.FILE && wp.match(filepath) && !resource.isDerived()) {
			//Si tengo informacion del root pero el last path no coincide
			//entonces lo descarto
//			if(root != null && !resource.getParent().getLocation().lastSegment().equals(root) ){
//				return false;
//			}
			// Si existe match entonces tengo que descubrir a que nivel lo hizo
			int index = 0;
			while (!wp.match(filepath, index)
					&& index < IWildcardPath.MAXIMUM_OPTIONALS) {
				index++;
			}
			// Si no encontre match retorno falso
			if (index == IWildcardPath.MAXIMUM_OPTIONALS) {
				return false;
			}
			// Parseo para obtener el nombre
			wp.parse(filepath, index);
			if (wp.getFileName() != null && wp.getFileName().equals(filename)
					&& wp.getFileExtension() != null
					&& wp.getFileExtension().equals(extension)
					&& wp.getRoot().equals(root)) {
				files.add((IFile) resource);
				return true;
			}
			// sino coinciden entonces no es el archivo correcto
		} else if (resource.getType() == IResource.PROJECT) {
			return true;
		}
		return false;
	}
}
