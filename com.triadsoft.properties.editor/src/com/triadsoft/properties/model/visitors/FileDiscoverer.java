package com.triadsoft.properties.model.visitors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.properties.model.utils.IWildcardPath;
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
		if (resource.getType() == IResource.FOLDER && root != null
				&& resource.getName().equals(root)) {
			return true;
		} else if (resource.getType() == IResource.FOLDER && root != null
				&& !resource.getName().equals(root)) {
			return false;
		} else if (resource.getType() == IResource.FOLDER && root == null) {
			return true;
		} else if (resource.getType() == IResource.FILE
				&& (wp.match(filepath, offset) || 
					wp.match(filepath, offset+1) || 
					wp.match(filepath, offset+2))) {
			// Si el archivo coincide con el wp, entonces lo parseo
			// para ver si el wp me devuelve el filename y extension esperado
			int index = offset;
			while(!wp.parse(filepath, index) && index<5){
				index++;
			}
			
			if (wp.getFileName() != null && wp.getFileName().equals(filename)
					&& wp.getFileExtension() != null
					&& wp.getFileExtension().equals(extension)) {
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
